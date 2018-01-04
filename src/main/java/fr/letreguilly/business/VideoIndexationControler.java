package fr.letreguilly.business;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoExtension;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.service.VideoFolderService;
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

    @Autowired
    private VideoFolderService videoFolderService;

    public List<Video> indexAllLocalFolder() {
        List<Video> videoList = new ArrayList();

        for (VideoFolder videoFolder : videoFolderService.getAllFoldersByNode(Cluster.localNode.getName())) {
            try {
                videoList.addAll(this.indexLocalFolder(videoFolder));
            } catch (Exception e) {
                log.error("can not index local folder : " + videoFolder.getName() + " on node " + Cluster.localNode.getName(), e);
            }
        }

        return videoList;
    }

    public List<Video> indexLocalFolder(VideoFolder folder) {

        //setup
        List<Video> indexedVideo = new ArrayList();
        String localFolderFilePath = folder.getNodePathMap().get(Cluster.localNode.getName());

        //check if directory exist etc.
        if (localFolderFilePath != null) {
            File localFolder = new File(localFolderFilePath);

            if (localFolder.exists() && localFolder.canRead() && localFolder.isDirectory() && localFolder.lastModified() > folder.getLastIndexationDate().getTime()) {
                log.info("index folder " + localFolderFilePath + " on node " + Cluster.localNode.getName());

                //get file list for directory
                List<File> directoryToIndexFileList = this.listFile(localFolder);

                //filter by date
                if (folder.getLastIndexationDate() != null) {
                    directoryToIndexFileList = this.filterByLastModificationDate(directoryToIndexFileList, folder.getLastIndexationDate());
                }

                //convert to video
                indexedVideo = this.convertFilesToVideo(directoryToIndexFileList, folder, localFolder);
                this.videoService.save(indexedVideo);

                //save last indexation date
                folder.setIndexedNow();
                this.videoFolderService.save(folder);

                //print indexed video
                indexedVideo.forEach(video -> log.info("index video " + localFolderFilePath + video.getPath()));

            } else if (localFolder.exists() == false) {
                log.error("directory " + localFolder.getAbsolutePath() + " doesn't exist");
            } else if (localFolder.canRead() == false) {
                log.error("access denied : directory " + localFolder.getAbsolutePath());
            } else if (localFolder.isDirectory() == false) {
                log.error("directory " + localFolder.getAbsolutePath() + " is not a directory");
            } else if(localFolder.lastModified() <= folder.getLastIndexationDate().getTime()){
                log.info("no modification on folder " + localFolderFilePath + " since last indexation");
            }
        }

        return indexedVideo;
    }


    private List<Video> convertFilesToVideo(List<File> fileList, VideoFolder videoFolder, File baseFolder) {
        List<Video> videoList = new ArrayList();

        for (File f : fileList) {
            Optional<Video> video = this.indexVideo(f, videoFolder, baseFolder);
            if (video.isPresent()) {
                videoList.add(video.get());
            }
        }

        return videoList;
    }

    private Optional<Video> indexVideo(File videoFile, VideoFolder videoFolder, File BaseFolder) {
        //optional result
        Optional<Video> videoOptional = Optional.empty();

        // get video extension eg mkv
        String extension = FilenameUtils.getExtension(videoFile.getName());

        //convert file to video object
        if (EnumUtils.isValidEnum(VideoExtension.class, extension)) {
            Video video = new Video(videoFolder);

            //various data
            video.setName(videoFile.getName());
            video.setExtension(VideoExtension.valueOf(extension));
            video.setSize(videoFile.length());
            video.setPath(videoFile.getAbsolutePath().substring(BaseFolder.getAbsolutePath().length()));

            //md5
            try {
                FileInputStream fis = new FileInputStream(videoFile);
                String md5 = DigestUtils.md5DigestAsHex(fis);
                video.setMd5(md5);
            } catch (IOException e) {
                log.error("can not determine md5 sum for file " + videoFile.getAbsolutePath());
            }

            //codec


            videoOptional = Optional.of(video);
        }

        return videoOptional;
    }

    /**
     * recursive function to list all file present in a directory
     *
     * @param directory the directory to list
     * @return the file list
     */
    private List<File> listFile(File directory) {  // or listFile(VideoFolder folder) and then File directory = NodeService.getNodeFolderFile(folder);
        List<File> fileList = new ArrayList();

        if (directory.canRead() && directory.isDirectory()) {

            File[] currentDirectoryFiles = directory.listFiles();

            for (int i = 0; i < currentDirectoryFiles.length; ++i) {
                if (currentDirectoryFiles[i].isDirectory()) {
                    fileList.addAll(this.listFile(currentDirectoryFiles[i]));
                } else {
                    fileList.add(currentDirectoryFiles[i]);
                }
            }
        }

        return fileList;
    }

    /**
     * return file modified after the lastmodificationdate parameter
     *
     * @param filesToFilter        the list of file to filter
     * @param lastModificationDate the last modification date
     * @return
     */
    private List<File> filterByLastModificationDate(List<File> filesToFilter, Date lastModificationDate) {
        List<File> filteredFiles = new ArrayList();

        for (File f : filesToFilter) {
            if (f.lastModified() > lastModificationDate.getTime()) {
                filteredFiles.add(f);
            }
        }

        return filteredFiles;
    }

}
