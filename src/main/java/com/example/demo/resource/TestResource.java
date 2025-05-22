package com.example.demo.resource;

import com.example.demo.conf.MediaTypeExt;
import com.example.demo.resource.model.PersonBinding;
import com.example.demo.resource.pojo.Citizen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Path("/test")
public class TestResource {

    private final static Logger logger = LoggerFactory.getLogger(TestResource.class);


    @Context
    HttpHeaders httpHeaders;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() throws IOException {
        ClientWebsocketTest clientEndpointTest = new ClientWebsocketTest("ws://localhost:8080/hello");
        clientEndpointTest.sendMessage("Hello, World!");
        return Response.ok().entity("SUCCESS").build();
    }

    @GET
    @Path("/proto")
    @Produces({MediaType.APPLICATION_JSON,
            MediaTypeExt.APPLICATION_X_PROTOBUF})
    public Response testProto() {

        String accept = httpHeaders.getHeaderString(HttpHeaders.ACCEPT);
        if (accept.isEmpty() || MediaType.APPLICATION_JSON.equals(accept)) {

            Citizen john = new Citizen();
            john.setId(1234567890);
            john.setEmail("john.test@test.com");
            john.setName("John Test");
            Citizen.Phone phone = new Citizen.Phone();
            phone.setType(PersonBinding.Person.PhoneType.MOBILE);
            phone.setNumber("761-672-7821");
            john.setPhones(Collections.singletonList(phone));
            return Response.ok().entity(john).build();

        } else {
            var name = "John Test";
            Integer id = 1234567890;
            var email = "john.test@test.com";
            var phone = "761-672-7821";
            var phoneTyp = PersonBinding.Person.PhoneType.MOBILE;
            PersonBinding.Person.Builder personBuilder =
                    PersonBinding.Person.newBuilder();
            personBuilder.setName(name);
            personBuilder.setId(id);
            personBuilder.setEmail(email);
            PersonBinding.Person.PhoneNumber.Builder phoneBuilder
                    = PersonBinding.Person.PhoneNumber.newBuilder();
            phoneBuilder.setNumber(phone);
            phoneBuilder.setType(phoneTyp);
            personBuilder.addPhones(phoneBuilder.build());
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
            logger.info("POST CITIZEN: {}", citizen);
            return Response.status(Response.Status.CREATED).entity(citizen).build();
    }

    @POST
    @Path("/proto")
    @Consumes(MediaTypeExt.APPLICATION_X_PROTOBUF)
    @Produces(MediaTypeExt.APPLICATION_X_PROTOBUF)
    public Response createProto(PersonBinding.Person john) {
        logger.info("POST Person: {}", john);
        return  Response.status(Response.Status.CREATED).entity(john).build();
    }
}
