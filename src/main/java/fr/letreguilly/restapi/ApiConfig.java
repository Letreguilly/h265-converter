package fr.letreguilly.restapi;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class ApiConfig extends ResourceConfig {

    public ApiConfig() {
        this.register(HealthCheckEntryPoint.class);
        this.register(NodeEntryPoint.class);
    }
}
