package fr.letreguilly.restapi;

import fr.letreguilly.business.NodeService;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.restapi.rest_entities.RestVideoFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Path("/folders")
@Component
public class FolderEntryPoint {

    @Autowired
    private NodeService nodeService;

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        return Response.ok().entity(nodeService.getAllFolders()).build();
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response addFolder (RestVideoFolder folder) {
        nodeService.addFolder(folder.getName(), folder.getPath());
        return Response.ok().build();
    }

}
