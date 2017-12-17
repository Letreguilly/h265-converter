package fr.letreguilly.restapi;

import fr.letreguilly.business.NodeService;
import fr.letreguilly.persistence.entities.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/node")
@Component
public class NodeApi {

    @Autowired
    private NodeService nodeService;

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        return Response.ok().entity(nodeService.getAllNodes()).build();
    }

    @GET
    @Path("{name}")
    @Produces({"application/json"})
    public Response getNodeByName(@PathParam("name") String name) {
        Node node = nodeService.getNodeByName(name);

        if (node != null) {
            return Response.ok().status(200).entity(node).build();
        } else {
            return Response.ok().status(404).build();
        }
    }
}
