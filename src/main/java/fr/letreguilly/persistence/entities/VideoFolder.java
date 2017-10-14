package fr.letreguilly.persistence.entities;


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

}
