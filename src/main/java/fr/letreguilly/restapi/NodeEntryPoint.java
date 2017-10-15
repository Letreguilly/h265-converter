package fr.letreguilly.restapi;

import fr.letreguilly.business.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/nodes")
@Component
public class NodeEntryPoint {

    @Autowired
    private NodeService nodeService;

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        return Response.ok().entity(nodeService.getAllNodes()).build();
    }
}
