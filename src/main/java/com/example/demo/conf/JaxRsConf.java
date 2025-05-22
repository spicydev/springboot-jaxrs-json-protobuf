package com.example.demo.conf;

import com.example.demo.resource.TestResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import jakarta.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@Configuration
@ApplicationPath("/api")
public class JaxRsConf extends ResourceConfig {

    public JaxRsConf() {
        register(TestResource.class);
        register(ProtoMessageWriter.class);
        register(ProtoMessageReader.class);
    }
}
