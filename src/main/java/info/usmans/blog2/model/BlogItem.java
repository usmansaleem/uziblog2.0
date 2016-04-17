package info.usmans.blog2.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Represents a blog item
 * @author usman.
 */
public class BlogItem {
    private long id;
    private String title;
    private String body;
    private List<Category> categories;
    private String blogSection;
    private Date createdOn;
    private Date modifiedOn;

    private String createDay;
    private String createMonth;
    private String createYear;

    public BlogItem() {
    }

    public BlogItem(long id, String title, String body, List<Category> categories, String blogSection, Date createdOn, Date modifiedOn) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.categories = categories;
        this.blogSection = blogSection;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdon) {
        this.createdOn = createdon;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getBlogSection() {
        return this.blogSection;
    }

    public void setBlogSection(String blogSection) {
        this.blogSection = blogSection;
    }

    public String getCreateDay() {
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        if(this.createdOn != null) {
            return dd.format(this.createdOn);
        } else {
            return dd.format(new Date());
        }
    }

    public String getCreateMonth() {
        SimpleDateFormat dd = new SimpleDateFormat("MMM");
        if(this.createdOn != null) {
            return dd.format(this.createdOn);
        } else {
            return dd.format(new Date());
        }
    }

    public String getCreateYear() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy");
        if(this.createdOn != null) {
            return dd.format(this.createdOn);
        } else {
            return dd.format(new Date());
        }
    }
}
