package fr.letreguilly.persistence.repositories;

import fr.letreguilly.persistence.entities.Node;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface VideoRepository extends ElasticsearchCrudRepository<Node.Video, Long> {

}
