package fr.letreguilly.persistence.repositories;

import fr.letreguilly.persistence.entities.Node;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface NodeRepository extends ElasticsearchCrudRepository<Node, Long> {
    Node findByName(String name);
}
