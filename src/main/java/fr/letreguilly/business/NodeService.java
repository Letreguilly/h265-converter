package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.Node;
import fr.letreguilly.persistence.repositories.NodeRepository;
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

@Slf4j
@Service
public class NodeService {

    private Node localNode;

    private List<Node> clusterNodes = new ArrayList();

    @Autowired
    private NodeRepository nodeRepository;

    @PostConstruct
    public void initNode() {
        try {
            localNode = new Node();

            //get mac address && ip
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            //set info
            localNode.setId(this.bytesToLong(mac));
            localNode.setName(ip.getHostName());
            localNode.setIPAddress(ip.getAddress());
            localNode.setMacAddress(mac);
            localNode.setCpuArch(CpuUtils.getCpuArch());
            localNode.setCpuCore(CpuUtils.getNumberOfCPUCores());
            localNode.setOperatingSystem(OsUtils.getOS());

            localNode = nodeRepository.save(localNode);
            clusterNodes.addAll(nodeRepository.findAll());
        } catch (UnknownHostException | SocketException e) {
            log.error("can not find current node mac address, node id is based on the mac address, exiting", e);
            System.exit(-1);
        }
    }

    public  long bytesToLong(byte[] b) {
        long result = 0;

        for (int i = 0; i < b.length; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }

        return result;
    }
}
