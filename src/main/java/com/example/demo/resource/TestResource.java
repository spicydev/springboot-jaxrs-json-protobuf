package com.example.demo.resource;

import com.example.demo.conf.MediaTypeExt;
import com.example.demo.resource.model.PersonBinding;
import com.example.demo.resource.pojo.Citizen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

@Path("/test")
public class TestResource {

    private final static Logger logger = LoggerFactory.getLogger(TestResource.class);


    @Context
    HttpHeaders httpHeaders;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() throws IOException {
        ClientEndpointTest clientEndpointTest = new ClientEndpointTest("ws://localhost:8080/api/hello");
        clientEndpointTest.sendMessage("Hello, World!");
        return Response.ok().entity("SUCCESS").build();
    }

    @GET
    @Path("/proto")
    @Produces({MediaType.APPLICATION_JSON, MediaTypeExt.APPLICATION_X_PROTOBUF})
    public Response testProto() {

        String accept = httpHeaders.getHeaderString(HttpHeaders.ACCEPT);
        if (accept.isEmpty() || MediaType.APPLICATION_JSON.equals(accept)) {

            Citizen john = new Citizen();
            john.setId(1234478948);
            john.setEmail("john.test@test.com");
            john.setName("John Test");

            return Response.ok().entity(john).build();

        } else {
            var name = "John Test";
            var id = 1234567890;
            var email = "test@test.com";
            var phone = "761-672-7821";
            var phoneTyp = PersonBinding.Person.PhoneType.MOBILE;
            PersonBinding.Person.Builder personBuilder =
                    PersonBinding.Person.newBuilder();
            Optional.ofNullable(name).ifPresent(personBuilder::setName);
            Optional.ofNullable(id).ifPresent(personBuilder::setId);
            Optional.ofNullable(email).ifPresent(personBuilder::setEmail);
            Optional.ofNullable(phone).ifPresent(number -> {
                    PersonBinding.Person.PhoneNumber.Builder phoneBuilder
                            = PersonBinding.Person.PhoneNumber.newBuilder();
                    phoneBuilder.setNumber(number);
                    Optional.ofNullable(phoneTyp).ifPresent(phoneBuilder::setType);
            });
            return Response.ok()
                    .entity(personBuilder.build())
                    .build();
        }
    }

    @POST
    @Path("/proto")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createJson(Citizen citizen) {
            logger.info("POST CITIZEN NAME: {}", citizen.getName());
            return Response.accepted().entity(citizen).build();
    }

    @POST
    @Path("/proto")
    @Consumes(MediaTypeExt.APPLICATION_X_PROTOBUF)
    @Produces(MediaTypeExt.APPLICATION_X_PROTOBUF)
    public Response createProto(PersonBinding.Person john) {
        logger.info("POST Person Name: {}", john.getName());
        return  Response.accepted().entity(john).build();
    }
}
