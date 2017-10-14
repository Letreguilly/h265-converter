package fr.letreguilly.restapi;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

/**
 * Created by letreguilly on 17/07/17.
 */
@Configuration
@ApplicationPath("/api")
public class ApiConfig extends ResourceConfig {

    public ApiConfig() {
        this.register(HelloApi.class);
    }
}
