package fr.letreguilly.persistence.service;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.repositories.NodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NodeService {

    @Autowired
    private NodeRepository nodeRepository;

    /**
     * register a new node to the cluster
     *
     * @param node the node to register
     * @return the registered node
     */
    public Node registerNewNode(Node node) {
        log.info("Register new node " + Cluster.localNode.getName());
        return nodeRepository.save(Cluster.localNode);
    }

    /**
     * return all the node of the cluster
     *
     * @return all the node of the cluster
     */
    public List<Node> getAllNodes() {
        List<Node> nodeList = new ArrayList();
        this.nodeRepository.findAll().forEach(node -> nodeList.add(node));
        return nodeList;
    }

    public Node save(Node node){
        return this.nodeRepository.save(node);
    }

    /**
     * return the desired node if exist
     *
     * @param name the id of the desired node
     * @return the node
     */
    public Node getNodeByName(String name) {
        return this.nodeRepository.findByName(name);
    }
}
