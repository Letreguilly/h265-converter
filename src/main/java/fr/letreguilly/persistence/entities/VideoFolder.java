package fr.letreguilly.persistence.entities;


import fr.letreguilly.Cluster;
import fr.letreguilly.utils.helper.NumberUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.File;
import java.util.*;

@Data
@Document(indexName = "folder")
public class VideoFolder {
    @Id
    private Long id;

    private String name;

    private Date lastIndexationDate;

    //Key: node name, value: path
    private Map<String, String> nodePathMap = new HashMap();

    public VideoFolder() {
    }

    public VideoFolder(String name, String nodeName, String path) {
        this.name = name;
        this.nodePathMap.put(nodeName, path);
        this.id = NumberUtils.stringToLong(name);
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setIndexedNow(){
        this.lastIndexationDate = Calendar.getInstance().getTime();
    }

    public void addNode(String nodeName, String path) {
        this.nodePathMap.put(nodeName, path);
    }

    public boolean isLocal(){
        return this.nodePathMap.containsKey(Cluster.localNode.getName());
    }

}
