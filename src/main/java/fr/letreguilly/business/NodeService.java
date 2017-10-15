package fr.letreguilly.business;

import fr.letreguilly.persistence.entities.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
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

    @PostConstruct
    public void initNode() {
        localNode = new Node();


        try {
            localNode.setName(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.warn("can node retrieve localNode hostname", e);
        }


        InetAddress ip;
        try {
            //get mac address
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            //
            localNode.setId(this.bytesToLong(mac));



            System.out.println(this.bytesToLong(mac));

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
