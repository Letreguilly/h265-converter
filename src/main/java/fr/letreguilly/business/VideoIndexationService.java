package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.VideoExtension;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.repositories.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class VideoIndexationService {

    @Autowired
    private VideoRepository videoRepository;


   /*@PostConstruct
    public void init() {
        File baseFolder = new File("D:\\Video");
        this.indexFolder(baseFolder);
    }*/

    public List<Video> indexFolder(File folderToIndex) {
        return this.indexFolder(folderToIndex, null);
    }

    public List<Video> indexFolder(File folderToIndex, Date lastIndexationDate) {
        List<Video> indexedVideo = new ArrayList();

        if (folderToIndex.exists() && folderToIndex.canRead() && folderToIndex.isDirectory()) {
            //get file list for directory
            List<File> directoryToIndexFileList = FileService.listFile(folderToIndex);

            //filter by date
            if (lastIndexationDate != null) {
                directoryToIndexFileList = FileService.filterByLastModificationDate(directoryToIndexFileList, lastIndexationDate);
            }

            //convert to video
            List<Video> videoList = VideoIndexationService.convertFilesToVideo(directoryToIndexFileList, folderToIndex);
            videoRepository.save(videoList);


        } else if (folderToIndex.exists() == false) {
            log.error("directory " + folderToIndex.getAbsolutePath() + " doesn't exist");
        } else if (folderToIndex.canRead() == false) {
            log.error("access denied : directory " + folderToIndex.getAbsolutePath());
        } else if (folderToIndex.isDirectory() == false) {
            log.error("directory " + folderToIndex.getAbsolutePath() + " is not a directory");
        }

        return indexedVideo;
    }


    public static List<Video> convertFilesToVideo(List<File> fileList, File baseFolder) {
        List<Video> videoList = new ArrayList();

        for (File f : fileList) {
            Optional<Video> video = VideoIndexationService.convertFileToVideo(f, baseFolder);
            if (video.isPresent()) {
                videoList.add(video.get());
            }
        }

        return videoList;
    }

    public static Optional<Video> convertFileToVideo(File videoFile, File BaseFolder) {
        //optional result
        Optional<Video> videoOptional = Optional.empty();

        // get video extension eg mkv
        String extension = FilenameUtils.getExtension(videoFile.getName());

        //convert file to video object
        if (EnumUtils.isValidEnum(VideoExtension.class, extension)) {
            Video video = new Video();

            video.setName(videoFile.getName());
            video.setExtension(VideoExtension.valueOf(extension));
            video.setSize(videoFile.length());
            video.setPath(videoFile.getAbsolutePath().substring(BaseFolder.getAbsolutePath().length()));

            try {
                FileInputStream fis = new FileInputStream(videoFile);
                String md5 = DigestUtils.md5DigestAsHex(fis);
                video.setMd5(md5);
            } catch (IOException e) {
                log.error("can not determine md5 sum for file " + videoFile.getAbsolutePath());
            }

            videoOptional = Optional.of(video);
        }

        return videoOptional;
    }
}
