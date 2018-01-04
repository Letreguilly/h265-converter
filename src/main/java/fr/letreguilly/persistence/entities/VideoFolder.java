package fr.letreguilly.persistence.entities;


import fr.letreguilly.Cluster;
import fr.letreguilly.utils.helper.NumberUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.File;
import java.io.IOException;
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

    public VideoFolder(Long id, String name, String nodeName, String path) {
        this.id = id;
        this.name = name;
        this.nodePathMap.put(nodeName, path);
        this.id = NumberUtils.stringToLong(name);
    }

    public void setIndexedNow() {
        this.lastIndexationDate = Calendar.getInstance().getTime();
    }

    public void addNode(String nodeName, String path) {
        this.nodePathMap.put(nodeName, path);
    }

    public boolean isLocal() {
        return this.nodePathMap.containsKey(Cluster.localNode.getName());
    }

    public File getLocalFile() throws IOException {
        if (this.isLocal()) {
            File localFile = new File(this.getNodePathMap().get(Cluster.localNode.getName()));

            if (localFile.exists()) {
                return localFile;
            } else {
                throw new IOException("video folder " + this.getName() + " is present on the database but not present on the node " + Cluster.localNode.getName());
            }
        } else {
            throw new IOException("video folder " + this.getName() + " is not present on node " + Cluster.localNode.getName());
        }

    }

}
