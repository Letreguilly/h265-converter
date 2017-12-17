package fr.letreguilly.restapi;

import fr.letreguilly.business.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/folder")
@Component
public class FolderApi {

    @Autowired
    private FolderService folderService;

    @GET
    @Produces({"application/json"})
    public Response getFolders() {
        return Response.ok().entity(folderService.getAllFolders()).build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Response getFolderById(@PathParam("id") long id) {
        return Response.ok().entity(folderService.getVideoFolderById(id)).build();
    }

    @GET
    @Path("add")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response addFolder(@QueryParam("nodeName")String nodeName, @QueryParam("folderName") String name, @QueryParam("path") String path) {
        folderService.addFolder(nodeName, name, path);
        return Response.ok().build();
    }

    @GET
    @Path("/addLocalFolder")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response addFolderToLocalNode( @QueryParam("name") String name, @QueryParam("path") String path) {
        folderService.addFolder(name, path);
        return Response.ok().build();
    }
}