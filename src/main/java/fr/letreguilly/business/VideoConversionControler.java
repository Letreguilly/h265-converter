package fr.letreguilly.business;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.service.VideoService;
import fr.letreguilly.utils.helper.ProccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class VideoConversionControler {

    private File ffmpegFile;

    private CompletableFuture<Optional<String>> currentTask = null;

    private Video currentVideo;

    @Value("${app.crf}")
    private String crf;

    @Value("${app.preset}")
    private String preset;

    @Autowired
    private BinaryControler binaryControler;

    @Autowired
    private VideoService videoService;

    @PostConstruct
    private void load() {
        this.ffmpegFile = binaryControler.getBinaryFile("ffmpeg");
    }

    public void convertionLoop() throws ExecutionException, InterruptedException {
        //print previous result and reset task
        if (currentTask != null && currentTask.isDone()) {
            //log.info("conversion result : " + currentTask.get().get());

            if(currentVideo.getFile().get().delete() == false){
                log.error("failed to delete file " + currentVideo.getFile().get().getAbsolutePath());
            }
            this.videoService.delete(currentVideo);
        }

        //convert next video
        if (currentTask == null || currentTask.isDone()) {
            List<Video> videosToConvert = videoService.getVideoToConvert();

            if (videosToConvert != null && videosToConvert.size() > 0) {
                Video videoToConvert = videosToConvert.get(RandomUtils.nextInt(0, videosToConvert.size()));
                this.convertLocalVideo(videoToConvert);
            } else {
                log.info("no video to convert");
            }
        } else {
            log.info("conversion in progress");
        }
    }

    public void convertLocalVideo(Video video) {

        Optional<File> optionalFile = video.getFile();
        this.currentVideo = video;

        if (optionalFile.isPresent()) {
            String command = "";
            command += ffmpegFile.getAbsolutePath();
            command += " -i \"" + optionalFile.get().getAbsolutePath();
            command += "\" -c:v libx265 -preset " + preset + " -x265-params crf=" + crf + " -c:a aac -b:a 128k  \"";
            command += optionalFile.get().getAbsolutePath() + ".h265.mkv\"\n";

            log.info("convert video : " + video.getName() + " with command : " + command);

            currentTask = ProccessUtils.execCommandAsync(command);
        } else {
            log.error("failed to convert video " + video.getName() + " on node " + Cluster.localNode.getName() + " file not found");
        }


    }
}
