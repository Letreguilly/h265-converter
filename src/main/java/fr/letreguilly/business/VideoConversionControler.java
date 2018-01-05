package fr.letreguilly.business;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.service.VideoService;
import fr.letreguilly.utils.helper.ProccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BinaryControler binaryControler;

    @Autowired
    private VideoService videoService;

    @PostConstruct
    private void load() {
        this.ffmpegFile = binaryControler.getBinaryFile("ffmpeg");
    }

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    private void convertionLoop() throws ExecutionException, InterruptedException {
        //print previous result and reset task
        if(currentTask != null && currentTask.isDone()){
            log.info("conversion result : " + currentTask.get().get());
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

        if (optionalFile.isPresent()) {
            String command = "";
            command += ffmpegFile.getAbsolutePath();
            command += " -i \"" + optionalFile.get().getAbsolutePath();
            command += "\" -c:v libx265 -preset medium -x265-params crf=23 -c:a aac -strict experimental -b:a 128k -v quiet \"";
            command += optionalFile.get().getAbsolutePath() + ".h265.mkv\"\n";

            log.info("convert video : " + video.getName() + " with command : " + command);

            currentTask = ProccessUtils.execCommandAsync(command);
        } else {
            log.error("failed to convert video " + video.getName() + " on node " + Cluster.localNode.getName() + " file not found");
        }


    }
}
