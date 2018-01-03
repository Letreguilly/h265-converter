package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoFolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@Slf4j
@Service
public class VideoConversionControler {

    private File ffmpegFile;

    @Autowired
    private BinaryControler binaryService;

    @PostConstruct
    private void initd() {
        this.ffmpegFile = binaryService.getBinaryFile("ffmpeg");
    }


    public void convertLocalFolder(VideoFolder videoFolder){

    }

    public void convertLocalVideo(Video video) throws InterruptedException {

        String command = "";
        command += ffmpegFile.getAbsolutePath();
        command += " -i \"" + video.getPath();
        command += "\" -c:v libx265 -preset medium -x265-params crf=23 -c:a aac -strict experimental -b:a 128k -v quiet \"";
        command += "C:\\Users\\letre\\Desktop\\output" + "\n";

        log.info("convert video : " + video.getName() + "with command : " + command);

        /*try {
            Process p;

            if (video.getOutputFile().exists() == true) {
                System.out.println("skip file already exist: " + video.getName());
            } else {


                System.out.println("Convert : " + video.getName());
                p = Runtime.getRuntime().exec(command);
                p.waitFor(1, TimeUnit.DAYS);
                p.destroy();


            }

        } catch (IOException e) {
            log.error("exception happened - here's what I know: ");
            e.printStackTrace();
        }*/
    }
}
