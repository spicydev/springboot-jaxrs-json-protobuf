package com.example.demo.conf;

import com.example.demo.resource.model.PersonBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaTypeExt.APPLICATION_X_PROTOBUF)
public class ProtoMessageWriter implements MessageBodyWriter<PersonBinding.Person> {

    private final static Logger logger = LoggerFactory.getLogger(ProtoMessageReader.class);

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass == PersonBinding.Person.class;
    }

    @Override
    public void writeTo(PersonBinding.Person person, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        multivaluedMap.add(HttpHeaders.CONTENT_LENGTH,person.getSerializedSize());
        logger.info("Proto Response Size: {}",person.getSerializedSize());
        person.writeTo(outputStream);
    }
}
