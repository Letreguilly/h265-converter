package fr.letreguilly.restapi;

import fr.letreguilly.persistence.service.VideoFolderService;
import fr.letreguilly.persistence.entities.VideoFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/folder")
@Component
public class FolderApi {

    @Autowired
    private VideoFolderService folderService;

    @GET
    @Produces({"application/json"})
    public Response getFolders() {
        return Response.ok().entity(folderService.getAllFolders()).build();
    }

    @GET
    @Path("{nodeName}")
    @Produces({"application/json"})
    public Response getFoldersByNodeName(@PathParam("nodeName") String nodeName) {
        return Response.ok().entity(folderService.getAllFoldersByNode(nodeName)).build();
    }

    @GET
    @Path("add")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response addFolder(@QueryParam("nodeName") String nodeName, @QueryParam("folderName") String name, @QueryParam("path") String path) {
        Optional<VideoFolder> localFolder = folderService.addFolder(nodeName, name, path);

        if (localFolder.isPresent() && localFolder.get().isLocal()) {
            return Response.ok().build();
        }else {
            return Response.serverError().build();
        }
    }
}