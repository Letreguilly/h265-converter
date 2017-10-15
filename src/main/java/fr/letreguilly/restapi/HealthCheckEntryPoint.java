package fr.letreguilly.restapi;

/**
 * Created by letreguilly on 17/07/17.
 */
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/he")
@Component
public class HealthCheckEntryPoint {

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        return Response.ok().entity(new String("{\"Alive\" : 1}")).build();
    }
}
