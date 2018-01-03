package fr.letreguilly.task;

import fr.letreguilly.Cluster;
import fr.letreguilly.persistence.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateClusterSizeTask {

    @Autowired
    private NodeService nodeService;

    @Scheduled(fixedRate = 10000)
    public void run(){
        Cluster.clusterSize = nodeService.getAllNodes().size();
    }
}
