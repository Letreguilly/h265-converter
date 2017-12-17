package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.repositories.NodeRepository;
import fr.letreguilly.persistence.repositories.VideoFolderRepository;
import fr.letreguilly.utils.helper.CpuUtils;
import fr.letreguilly.utils.helper.NumberUtils;
import fr.letreguilly.utils.helper.OsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

@Slf4j
@Service
public class NodeService {

    private Node localNode;

    @Autowired
    private NodeRepository nodeRepository;



    @PostConstruct
    private void initLocalNode() {
        try {
            localNode = new Node();

            //get mac address && ip
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            //set info
            //this.localNode.setId(NumberUtils.bytesToLong(mac));
            this.localNode.setName(ip.getHostName());
            this.localNode.setIPAddress(ip.getHostAddress());
            this.localNode.setMacAddress(Hex.encodeHexString(mac));
            this.localNode.setCpuArch(CpuUtils.getCpuArch());
            this.localNode.setCpuCore(CpuUtils.getNumberOfCPUCores());
            this.localNode.setOperatingSystem(OsUtils.getOS());

            //register node
            if (this.getAllNodes().contains(localNode)) {
                log.info("Node " + this.localNode.getName() + " already registered, updating information");
                this.localNode = nodeRepository.save(localNode);
            } else {
                this.localNode = this.registerNewNode(localNode);
            }
        } catch (UnknownHostException | SocketException e) {
            log.error("can not find current node mac address, node id is based on the mac address, exiting", e);
            System.exit(-1);
        }
    }

    /**
     * register a new node to the cluster
     *
     * @param node the node to register
     * @return the registered node
     */
    public Node registerNewNode(Node node) {
        log.info("Register new node " + this.localNode.getName());
        return nodeRepository.save(localNode);
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

    /**
     * return the desired node if exist
     * @param name the id of the desired node
     * @return the node
     */
    public Node getNodeByName(String name){
        return this.nodeRepository.findByName(name);
    }

    /**
     * return the local node
     * @return the local node
     */
    public Node getLocalNode() {
        return localNode;
    }
}
