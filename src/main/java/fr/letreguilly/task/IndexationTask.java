package fr.letreguilly.task;

import fr.letreguilly.Cluster;
import fr.letreguilly.business.VideoIndexationControler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IndexationTask {
    private boolean main;

    @Autowired
    private VideoIndexationControler indexationControler;

    @Scheduled(initialDelay = 600000, fixedDelay = 3600000)
    public void run() {
        indexationControler.indexAllLocalFolder();
    }
}
