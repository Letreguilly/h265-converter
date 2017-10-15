package fr.letreguilly.persistence.repositories;

import fr.letreguilly.persistence.entities.VideoFolder;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface VideoFolderRepository extends ElasticsearchCrudRepository<VideoFolder, Long> {

}
