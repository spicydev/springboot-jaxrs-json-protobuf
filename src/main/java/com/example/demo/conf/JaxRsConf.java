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
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestResource.class);
        classes.add(ProtoMessageWriter.class);
        classes.add(ProtoMessageReader.class);
        registerClasses(classes);

    }
}
