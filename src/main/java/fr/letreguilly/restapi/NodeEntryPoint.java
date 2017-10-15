package fr.letreguilly.restapi;

import fr.letreguilly.business.NodeService;
import fr.letreguilly.persistence.entities.Node;
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

    @GET
    @Path("/folders")
    @Produces({"application/json"})
    public Response getFolders(Node node) {
        return Response.ok().entity(nodeService.getAllFoldersByNode(node)).build();
    }
}
