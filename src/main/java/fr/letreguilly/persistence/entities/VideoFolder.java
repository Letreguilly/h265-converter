package fr.letreguilly.persistence.entities;


import fr.letreguilly.utils.helper.NumberUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "folder")
public class VideoFolder {
    @Id
    private Long id;

    private String name;

    //Key: node, value: path
    private Map<Long, String> nodePathMap = new HashMap();

    public VideoFolder () {
    }

    public VideoFolder (String name, Long node, String path) {
        this.name = name;
        this.nodePathMap.put(node, path);
        this.id = NumberUtils.stringToLong(name);
    }

    public void addNode (Long node, String path) {
        this.nodePathMap.put(node, path);
    }

}
