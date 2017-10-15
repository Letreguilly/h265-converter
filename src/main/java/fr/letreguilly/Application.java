package fr.letreguilly;

import fr.letreguilly.business.BinaryService;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.repositories.VideoRepository;
import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Application {

    @Value("${app.name}")
    private String name;


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class);

        try {
            BinaryService.getBinary("ffmpeg");
        } catch (IOException e) {
            log.error("can not find binary ffmpeg", e);
        }

    }


}
