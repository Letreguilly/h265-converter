package fr.letreguilly.business;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.entities.VideoFolder;
import fr.letreguilly.persistence.repositories.NodeRepository;
import fr.letreguilly.persistence.service.NodeService;
import fr.letreguilly.utils.helper.CpuUtils;
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

import org.apache.commons.codec.binary.Hex;

@Slf4j
@Service
public class NodeControler {

    @Autowired
    private NodeService nodeService;

    @PostConstruct
    private void initLocalNode() {
        try {
            Cluster.localNode = new Node();

            //get mac address && ip
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            //set info
            //this.localNode.setId(NumberUtils.bytesToLong(mac));
            Cluster.localNode.setName(ip.getHostName());
            Cluster.localNode.setIPAddress(ip.getHostAddress());
            Cluster.localNode.setMacAddress(Hex.encodeHexString(mac));
            Cluster.localNode.setCpuArch(CpuUtils.getCpuArch());
            Cluster.localNode.setCpuCore(CpuUtils.getNumberOfCPUCores());
            Cluster.localNode.setOperatingSystem(OsUtils.getOS());

            //register node
            if (this.nodeService.getAllNodes().contains(Cluster.localNode)) {
                log.info("Node " + Cluster.localNode.getName() + " already registered, updating information");
                Cluster.localNode = this.nodeService.save(Cluster.localNode);
            } else {
                Cluster.localNode = this.nodeService.registerNewNode(Cluster.localNode);
            }
        } catch (UnknownHostException | SocketException e) {
            log.error("can not find current node mac address, node id is based on the mac address, exiting", e);
            System.exit(-1);
        }
    }


}
