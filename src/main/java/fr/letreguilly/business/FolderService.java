package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.repositories.VideoFolderRepository;
import fr.letreguilly.utils.helper.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FolderService {

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

    public void addFolder(String name, String path) {
        this.addFolder(this.nodeService.getLocalNode().getName(), name, path);
    }

    public VideoFolder addFolder(String nodeName, String name, String path) {
        Long folderId = NumberUtils.stringToLong(name);
        VideoFolder existingFolder = videoFolderRepository.findOne(folderId);
        Node node = this.nodeService.getNodeByName(nodeName);

        if (existingFolder == null) {  // create a new folder
            VideoFolder newFolder = new VideoFolder(name, nodeName, path);
            log.info("Add new folder " + path + " on node " + nodeName);
            return videoFolderRepository.save(newFolder);
        } else {
            // check if there is already a path on this node
            if (existingFolder.getNodePathMap().containsKey(node.getName()) == false) {
                existingFolder.addNode(nodeName, path);
                log.info("Add folder " + path + " on node " + nodeName);
                return videoFolderRepository.save(existingFolder);
            } else {
                log.warn("This folder has already been added on this node.");
            }
        }
        return null;
    }

    public VideoFolder getVideoFolderById(long id) {
        return videoFolderRepository.findOne(id);
    }
}