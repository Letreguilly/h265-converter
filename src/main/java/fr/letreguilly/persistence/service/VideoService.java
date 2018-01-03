package fr.letreguilly.persistence.service;

import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.repositories.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public Video getById(Long videoId) {
        return this.videoRepository.findOne(videoId);
    }

    public List<Video> getAllVideo() {
        List<Video> videoList = new ArrayList();
        videoRepository.findAll().forEach(videoList::add);

        return videoList;
    }

    public Video save(Video video) {
        return this.videoRepository.save(video);
    }

    public List<Video> save(List<Video> video) {
        List<Video> savedVideo = new ArrayList();
        video.forEach(video1 -> savedVideo.add(this.save(video1)));

        return savedVideo;
    }
}
