package com.example.demo.conf;

import com.example.demo.resource.model.PersonBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaTypeExt.APPLICATION_X_PROTOBUF)
public class ProtoMessageWriter implements MessageBodyWriter<PersonBinding.Person> {

    private final static Logger logger = LoggerFactory.getLogger(ProtoMessageWriter.class);

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass == PersonBinding.Person.class && mediaType.isCompatible(MediaType.valueOf(MediaTypeExt.APPLICATION_X_PROTOBUF));
    }

    @Override
    public void writeTo(PersonBinding.Person person, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        multivaluedMap.add(HttpHeaders.CONTENT_LENGTH,person.getSerializedSize());
        logger.info("Proto Response Size: {}",person.getSerializedSize());
        person.writeTo(outputStream);
    }
}
