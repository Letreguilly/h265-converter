package fr.letreguilly.persistence.entities;


import fr.letreguilly.utils.helper.NumberUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "folder")
public class VideoFolder {
    @Id
    private Long id;

    private List<String> path = new ArrayList();

    private String name;

    private List<Node> nodes = new ArrayList();

    public VideoFolder (String name, String path, Node node) {

        this.name = name;
        this.path.add(path);
        this.nodes.add(node);
        this.id = NumberUtils.stringToLong(name);
    }

    public void addNode (Node node, String path) {
        this.path.add(path);
        this.nodes.add(node);
    }

    //Node class
    /*public void addFolder (String name, String path) {
        //if new folder:
        VideoFolder newFolder = new VideoFolder(name, path, this);
        //if existing folder:
        Long folderId = NumberUtils.stringToLong(name);
        //get folder where id = folderId -> folder.addNode
    }*/

}
