package info.usmans.blog2.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

/**
 * @author usman.
 */
@Path("/blog")
public class BlogService {
    @GET
    @Path("/cat")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> categories() {
        Category ct1 = new Category();
        ct1.setId(1);
        ct1.setName("Java");

        List<Category> cat = new LinkedList<>();
        cat.add(ct1);

        return cat;
    }
}
