package com.example.demo.resource;

import com.example.demo.resource.model.PersonBinding;
import com.example.demo.resource.pojo.Citizen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> test() throws IOException {
        try (ClientWebsocketTest clientEndpointTest = new ClientWebsocketTest("ws://localhost:8080/hello")) {
            clientEndpointTest.sendMessage("Hello, World!");
        }
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping(path = "/proto", produces = { MediaType.APPLICATION_JSON_VALUE, "application/x-protobuf" })
    public ResponseEntity<?> testProto(@RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {

        if (acceptHeader.isEmpty() || MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader)) {
            Citizen john = new Citizen();
            john.setId(1234567890);
            john.setEmail("john.test@test.com");
            john.setName("John Test");
            Citizen.Phone phone = new Citizen.Phone();
            phone.setType(PersonBinding.Person.PhoneType.MOBILE);
            phone.setNumber("761-672-7821");
            john.setPhones(Collections.singletonList(phone));
            return ResponseEntity.ok(john);
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
            return ResponseEntity.ok(personBuilder.build());
        }
    }

    @PostMapping(path = "/proto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Citizen> createJson(@RequestBody Citizen citizen) {
            logger.info("POST CITIZEN: {}", citizen);
            return new ResponseEntity<>(citizen, HttpStatus.CREATED);
    }

    @PostMapping(path = "/proto", consumes = "application/x-protobuf", produces = "application/x-protobuf")
    public ResponseEntity<PersonBinding.Person> createProto(@RequestBody PersonBinding.Person john) {
        logger.info("POST Person: {}", john);
        return  new ResponseEntity<>(john, HttpStatus.CREATED);
    }
}
