package fr.letreguilly.restapi;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class ApiConfig extends ResourceConfig {

    public ApiConfig() {
        this.register(NodeApi.class);
        this.register(FolderApi.class);
        this.register(InfoApi.class);
        this.register(IndexationApi.class);
    }
}
