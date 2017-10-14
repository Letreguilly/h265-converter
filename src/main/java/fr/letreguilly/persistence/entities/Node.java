package fr.letreguilly.persistence.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "node")
public class Node {
    @Id
    private Long id;

    private String name;

    private String IPAddress;
}
