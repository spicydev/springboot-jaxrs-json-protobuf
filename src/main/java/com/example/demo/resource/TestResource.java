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
            PersonBinding.Person john =
                    PersonBinding.Person.newBuilder()
                            .setName("John Test")
                            .setId(1234478948)
                            .setEmail("john.test@test.com")
                            .addPhones(
                                    PersonBinding.Person.PhoneNumber.newBuilder()
                                            .setNumber("761-672-7821")
                                            .setType(PersonBinding.Person.PhoneType.MOBILE)
                            )
                            .build();
            return Response.ok().entity(john).build();
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
