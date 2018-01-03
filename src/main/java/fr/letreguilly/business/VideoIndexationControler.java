package fr.letreguilly.business;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoExtension;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class VideoIndexationControler {

    @Autowired
    private VideoService videoService;


    public List<Video> indexLocalFolder(VideoFolder folder) {

        //setup
        List<Video> indexedVideo = new ArrayList();
        String localFolderFilePath = folder.getNodePathMap().get(Cluster.localNode.getName());

        //check if directory exist etc.
        if (localFolderFilePath != null) {
            File localFolder = new File(localFolderFilePath);

            if (localFolder.exists() && localFolder.canRead() && localFolder.isDirectory()) {
                //get file list for directory
                List<File> directoryToIndexFileList = FileControler.listFile(localFolder);

                //filter by date
                if (folder.getLastIndexationDate() != null) {
                    directoryToIndexFileList = FileControler.filterByLastModificationDate(directoryToIndexFileList, folder.getLastIndexationDate());
                }

                //convert to video
                List<Video> videoList = VideoIndexationControler.convertFilesToVideo(directoryToIndexFileList, folder, localFolder);
                this.videoService.save(videoList);

            } else if (localFolder.exists() == false) {
                log.error("directory " + localFolder.getAbsolutePath() + " doesn't exist");
            } else if (localFolder.canRead() == false) {
                log.error("access denied : directory " + localFolder.getAbsolutePath());
            } else if (localFolder.isDirectory() == false) {
                log.error("directory " + localFolder.getAbsolutePath() + " is not a directory");
            }
        }

        return indexedVideo;
    }


    public static List<Video> convertFilesToVideo(List<File> fileList, VideoFolder videoFolder, File baseFolder) {
        List<Video> videoList = new ArrayList();

        for (File f : fileList) {
            Optional<Video> video = VideoIndexationControler.convertFileToVideo(f, videoFolder, baseFolder);
            if (video.isPresent()) {
                videoList.add(video.get());
            }
        }

        return videoList;
    }

    public static Optional<Video> convertFileToVideo(File videoFile, VideoFolder videoFolder, File BaseFolder) {
        //optional result
        Optional<Video> videoOptional = Optional.empty();

        // get video extension eg mkv
        String extension = FilenameUtils.getExtension(videoFile.getName());

        //convert file to video object
        if (EnumUtils.isValidEnum(VideoExtension.class, extension)) {
            Video video = new Video(videoFolder);

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
