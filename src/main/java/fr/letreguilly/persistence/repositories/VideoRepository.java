package fr.letreguilly.persistence.repositories;

import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoCodec;
import fr.letreguilly.persistence.entities.VideoFolder;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VideoRepository extends ElasticsearchCrudRepository<Video, Long> {

    List<Video> findFirstByCodecNotAndVideoFolder_NameIn(VideoCodec codec, List<String> videoFolder);

    List<Video> findFirstByCodecNotInAndVideoFolder_NameIn(List<VideoCodec> codec, List<VideoFolder> videoFolder);

}
