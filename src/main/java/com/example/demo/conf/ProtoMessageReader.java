package com.example.demo.conf;

import com.example.demo.resource.model.PersonBinding;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaTypeExt.APPLICATION_X_PROTOBUF)
public class ProtoMessageReader implements MessageBodyReader<PersonBinding.Person> {

    private final static Logger logger = LoggerFactory.getLogger(ProtoMessageReader.class);

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass == PersonBinding.Person.class && mediaType.isCompatible(MediaType.valueOf(MediaTypeExt.APPLICATION_X_PROTOBUF));
    }

    @Override
    public PersonBinding.Person readFrom(Class<PersonBinding.Person> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        logger.info("Proto Request Header: {}",multivaluedMap.get(HttpHeaders.CONTENT_TYPE));
        try {
            return PersonBinding.Person.parseFrom(inputStream);
        } catch (InvalidProtocolBufferException e) {
            logger.error("Failed to parse protobuf message", e);
            throw new WebApplicationException("Failed to parse protobuf message", e, Response.Status.BAD_REQUEST);
        }
    }
}
