package fr.letreguilly;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class Application {

    @Value("${app.name}")
    private String name;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class);
    }


}
