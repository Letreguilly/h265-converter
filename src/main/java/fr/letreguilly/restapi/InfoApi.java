package fr.letreguilly.restapi;

import fr.letreguilly.business.FolderService;
import fr.letreguilly.business.NodeService;
import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.VideoFolder;
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
    private FolderService folderService;

    @GET
    @Produces({"application/json"})
    public Response getInfo() {
        InfoResponse infoResponse = new InfoResponse();
        infoResponse.setNodeList(nodeService.getAllNodes());
        infoResponse.setVideoFolderList(folderService.getAllFolders());

        return Response.ok().entity(infoResponse).build();
    }

    @Data
    public static class InfoResponse {
        private List<Node> nodeList = new ArrayList();
        private List<VideoFolder> videoFolderList = new ArrayList();
    }
}
