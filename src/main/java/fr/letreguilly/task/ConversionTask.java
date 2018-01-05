package fr.letreguilly.task;

import fr.letreguilly.business.VideoConversionControler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class ConversionTask {

    @Autowired
    private VideoConversionControler videoConversionControler;

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void convert() throws ExecutionException, InterruptedException {
        videoConversionControler.convertionLoop();
    }
}
