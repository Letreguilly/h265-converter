package fr.letreguilly.restapi;

import fr.letreguilly.persistence.service.NodeService;
import fr.letreguilly.persistence.service.VideoFolderService;
import fr.letreguilly.business.NodeControler;
import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.service.VideoService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/info")
@Component
public class InfoApi {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private VideoFolderService folderService;

    @Autowired
    private VideoService videoService;

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        InfoResponse infoResponse = new InfoResponse();
        infoResponse.setNodeList(nodeService.getAllNodes());
        infoResponse.setVideoFolderList(folderService.getAllFolders());
        infoResponse.setVideoList(videoService.getAllVideo());

        return Response.ok().entity(infoResponse).build();
    }

    @Data
    public static class InfoResponse {
        private List<Node> nodeList = new ArrayList();
        private List<VideoFolder> videoFolderList = new ArrayList();
        private List<Video> videoList = new ArrayList();
    }
}
