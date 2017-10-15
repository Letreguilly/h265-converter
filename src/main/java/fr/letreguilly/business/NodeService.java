package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.repositories.NodeRepository;
import fr.letreguilly.utils.helper.CpuUtils;
import fr.letreguilly.utils.helper.NumberUtils;
import fr.letreguilly.utils.helper.OsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NodeService {

    private Node localNode;

    private List<Node> clusterNodes = new ArrayList();

    @Autowired
    private NodeRepository nodeRepository;

    @PostConstruct
    public void initLocalNode() {
        try {
            localNode = new Node();

            //get mac address && ip
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            //set info
            this.localNode.setId(NumberUtils.bytesToLong(mac));
            this.localNode.setName(ip.getHostName());
            this.localNode.setIPAddress(ip.getAddress());
            this.localNode.setMacAddress(mac);
            this.localNode.setCpuArch(CpuUtils.getCpuArch());
            this.localNode.setCpuCore(CpuUtils.getNumberOfCPUCores());
            this.localNode.setOperatingSystem(OsUtils.getOS());

            //get all node
            nodeRepository.findAll().forEach(node -> clusterNodes.add(node));

            //register node
            if(this.clusterNodes.contains(localNode)){
                log.info("Node "+ this.localNode.getName() + " already registered, updating information");
                this.localNode = nodeRepository.save(localNode);
            }else{
                log.info("Register new node " + this.localNode.getName());
                this.localNode = this.registerNewNode(localNode);
            }
        } catch (UnknownHostException | SocketException e) {
            log.error("can not find current node mac address, node id is based on the mac address, exiting", e);
            System.exit(-1);
        }
    }

    public Node registerNewNode(Node node){
        return nodeRepository.save(localNode);
    }


    public List<Node> getAllNodes(){
        return  clusterNodes;
    }

}
