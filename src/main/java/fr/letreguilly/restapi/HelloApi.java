package fr.letreguilly.restapi;

/**
 * Created by letreguilly on 17/07/17.
 */
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/hello")
@Component
public class HelloApi {

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        return Response.ok().entity(new String("{\"Hello Api\" : 1}")).build();
    }
}
