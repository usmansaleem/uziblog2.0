package info.usmans.blog2.rest;

import info.usmans.blog2.model.BlogItem;
import info.usmans.blog2.model.Category;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author usman.
 */
@Path("/blog")
public class BlogService {
    private static final String BLOG_ITEM_BY_SECTION_COUNT_QUERY = "select count(id) from blog_item WHERE blog_section_name = ?";
    private static final String BLOG_CATEGORIES_QUERY = "select id, category_name from blog_categories";
    private static final String BLOG_ITEM_BY_SECTION_QUERY = "SELECT id, title, body, create_time, modified_time FROM blog_item WHERE blog_section_name = ? ORDER BY create_time DESC LIMIT ? OFFSET ?";
    private static final String CATEGORY_ID_QUERY = "SELECT category_id FROM blog_item_categories WHERE blog_item_id=?";


    @Resource(mappedName = "java:/uziblogds")
    private DataSource _ds;

    private boolean refreshCategories;
    private boolean refreshBlogCount;

    private Map<String, Category> categoriesMap = new LinkedHashMap<>();
    private int blogCount;

    public BlogService() {
        refreshCategories = true;
        refreshBlogCount = true;
    }


    @GET
    @Path("/listCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> categories() {
        if (refreshCategories) {
            fetchCategories();
        }
        Collection<Category> categoryCollection = this.categoriesMap.values();
        if (categoryCollection instanceof List) {
            return (List<Category>) categoryCollection;
        } else {
            return new ArrayList<>(this.categoriesMap.values());
        }
    }

    @PostConstruct
    private void fetchCategories() {
        try (Connection con = _ds.getConnection(); PreparedStatement ps = con
                .prepareStatement(BLOG_CATEGORIES_QUERY)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(rs.getInt(1), rs.getString(2));
                    this.categoriesMap.put(String.valueOf(category.getId()), category);
                }
                refreshCategories = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/blogCount/{blogSection}")
    @Produces(MediaType.APPLICATION_JSON)
    public int blogCount(@PathParam("blogSection") String blogSection, @QueryParam("categoryId") List<Integer> categoryIds) {
        int blogCount = 0;
        try (Connection con = _ds.getConnection(); PreparedStatement ps = con
                .prepareStatement(BLOG_ITEM_BY_SECTION_COUNT_QUERY)) {
            ps.setString(1, blogSection);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                blogCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            blogCount = 0;
        }
        return blogCount;
    }

    @GET
    @Path("/blogItems/{blogSection}/{pageNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BlogItem> blogItem(@PathParam("pageNumber") int pageNumber, @PathParam("blogSection") String blogSection, @QueryParam("categoryId") List<Integer> categoryIds ) {
        List<BlogItem> blogItemList = new LinkedList<>();
        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        if (blogSection == null || blogSection.trim().length() == 0) {
            blogSection = "Main";
        }

        int limit = 10;
        int offset = (pageNumber * limit) - limit;

        try (Connection con = _ds.getConnection(); PreparedStatement ps = con
                .prepareStatement(BLOG_ITEM_BY_SECTION_QUERY); PreparedStatement ps2 = con
                .prepareStatement(CATEGORY_ID_QUERY)) {
            ps.setString(1, blogSection);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BlogItem blogItem = new BlogItem();
                    blogItem.setId(rs.getLong(1));
                    blogItem.setTitle(rs.getString(2));
                    Clob clob = rs.getClob(3);
                    blogItem.setBody(clob.getSubString(1, (int) clob.length()));
                    blogItem.setCreatedOn(new Date(rs.getTimestamp(4).getTime()));
                    blogItem.setModifiedOn(new Date(rs.getTimestamp(5).getTime()));

                    //Categories
                    blogItem.setCategories(new LinkedList<>());
                    ps2.clearParameters();
                    ps2.setLong(1, blogItem.getId());
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) {
                            Category category = this.categoriesMap.get(String.valueOf(rs2.getLong(1)));
                            if (category != null) {
                                blogItem.getCategories().add(category);
                            }
                        }
                    }

                    blogItemList.add(blogItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return blogItemList;

    }
}
