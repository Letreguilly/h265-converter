package fr.letreguilly.persistence.service;

import fr.letreguilly.Cluster;
import fr.letreguilly.business.NodeControler;
import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.repositories.VideoFolderRepository;
import fr.letreguilly.utils.helper.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoFolderService {

    @Autowired
    private VideoFolderRepository videoFolderRepository;

    @Autowired
    private NodeService nodeService;

    public List<VideoFolder> getAllFolders() {
        List<VideoFolder> clusterVideoFolders = new ArrayList();
        videoFolderRepository.findAll().forEach(folder -> clusterVideoFolders.add(folder));
        return clusterVideoFolders;
    }

    public List<VideoFolder> getAllFoldersByNode(String nodeName) {
        List<VideoFolder> nodeVideoFolders = new ArrayList();

        videoFolderRepository.findAll().forEach(folder -> {
            if (folder.getNodePathMap().containsKey(nodeName)) {
                nodeVideoFolders.add(folder);
            }
        });

        return nodeVideoFolders;
    }

    public VideoFolder getByName(String name) {
        return this.videoFolderRepository.findFirstByName(name);
    }

    public Optional<VideoFolder> addFolder(String nodeName, String name, String path) {
        Long folderId = NumberUtils.stringToLong(name);
        VideoFolder existingFolder = videoFolderRepository.findOne(folderId);
        Node node = this.nodeService.getNodeByName(nodeName);
        Optional<VideoFolder> optionalVideoFolder = Optional.empty();

        if (existingFolder == null) {  // create a new folder
            VideoFolder newFolder = new VideoFolder(folderId, name, nodeName, path);
            log.info("Add new folder " + path + " on node " + nodeName);
            optionalVideoFolder = Optional.of(videoFolderRepository.save(newFolder));
        } else {
            // check if there is already a path on this node
            if (existingFolder.getNodePathMap().containsKey(node.getName()) == false) {
                existingFolder.addNode(nodeName, path);
                log.info("Add folder " + path + " on node " + nodeName);
                optionalVideoFolder = Optional.of(videoFolderRepository.save(existingFolder));
            } else {
                log.warn("This folder has already been added on this node.");
            }
        }
        return optionalVideoFolder;
    }

    public VideoFolder getVideoFolderById(long id) {
        return videoFolderRepository.findOne(id);
    }

    public VideoFolder save(VideoFolder videoFolder) {
        return this.videoFolderRepository.save(videoFolder);
    }
}
