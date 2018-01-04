package fr.letreguilly.restapi;

import fr.letreguilly.business.VideoIndexationControler;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.service.VideoFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/indexation")
@Component
public class IndexationApi {
    @Autowired
    private VideoIndexationControler indexationControler;

    @Autowired
    private VideoFolderService videoFolderService;

    @GET
    @Path("/all")
    @Produces({"application/json"})
    public Response indexAllLocalFolder() {
        List<Video> indexedVideo = indexationControler.indexAllLocalFolder();

        return Response.ok().entity(indexedVideo).build();
    }

    @GET
    @Path("/folder")
    @Produces({"application/json"})
    public Response indexAllLocalFolder(@QueryParam("name") String name) {
        VideoFolder videoFolder = videoFolderService.getByName(name);

        if (videoFolder != null) {
            List<Video> indexedVideo = indexationControler.indexLocalFolder(videoFolder);
            return Response.ok().entity(indexedVideo).build();
        } else {
            return Response.ok().status(404).build();
        }
    }
}
