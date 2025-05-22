package com.example.demo.conf;

import com.example.demo.resource.model.PersonBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaTypeExt.APPLICATION_X_PROTOBUF)
public class ProtoMessageReader implements MessageBodyReader<PersonBinding.Person> {

    private final static Logger logger = LoggerFactory.getLogger(ProtoMessageWriter.class);

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass == PersonBinding.Person.class;
    }

    @Override
    public PersonBinding.Person readFrom(Class<PersonBinding.Person> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        logger.info("Proto Request Header: {}",multivaluedMap.get(HttpHeaders.CONTENT_TYPE));
        return PersonBinding.Person.parseFrom(inputStream);
    }
}
