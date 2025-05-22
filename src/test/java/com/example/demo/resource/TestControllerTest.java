package com.example.demo.resource;

import com.example.demo.resource.model.PersonBinding;
import com.example.demo.resource.pojo.Citizen;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import; // Added
import com.example.demo.conf.WebConfig; // Added
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
@Import(WebConfig.class) // Added this line
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization/deserialization

    // Test for the /api/test endpoint (GET)
    @Test
    void testEndpoint_whenWebSocketConnectionFails_shouldReturnInternalServerError() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/test"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("WebSocket test failed");
    }

    // Test for /api/test/proto (GET) with Accept: application/json
    @Test
    void testGetProto_whenAcceptJson_shouldReturnCitizenJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/test/proto")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        Citizen citizen = objectMapper.readValue(contentAsString, Citizen.class);

        assertThat(citizen.getName()).isEqualTo("John Test");
        assertThat(citizen.getId()).isEqualTo(1234567890);
        assertThat(citizen.getEmail()).isEqualTo("john.test@test.com");
        assertThat(citizen.getPhones()).hasSize(1);
        assertThat(citizen.getPhones().get(0).getNumber()).isEqualTo("761-672-7821");
        assertThat(citizen.getPhones().get(0).getType()).isEqualTo(PersonBinding.Person.PhoneType.MOBILE);
    }

    // Test for /api/test/proto (GET) with Accept: application/x-protobuf
    @Test
    void testGetProto_whenAcceptProtobuf_shouldReturnPersonProto() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/test/proto")
                        .header(HttpHeaders.ACCEPT, "application/x-protobuf"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf"))
                .andReturn();

        byte[] responseBody = result.getResponse().getContentAsByteArray();
        PersonBinding.Person person = PersonBinding.Person.parseFrom(responseBody);

        assertThat(person.getName()).isEqualTo("John Test");
        assertThat(person.getId()).isEqualTo(1234567890);
        assertThat(person.getEmail()).isEqualTo("john.test@test.com");
        assertThat(person.getPhonesCount()).isEqualTo(1);
        assertThat(person.getPhones(0).getNumber()).isEqualTo("761-672-7821");
        assertThat(person.getPhones(0).getType()).isEqualTo(PersonBinding.Person.PhoneType.MOBILE);
    }

    // Test for /api/test/proto (POST) with JSON content
    @Test
    void testCreateJson_shouldReturnCreatedCitizen() throws Exception {
        Citizen citizenRequest = new Citizen();
        citizenRequest.setName("Jane Doe");
        citizenRequest.setId(987654321);
        citizenRequest.setEmail("jane.doe@example.com");
        Citizen.Phone phone = new Citizen.Phone();
        phone.setNumber("123-456-7890");
        phone.setType(PersonBinding.Person.PhoneType.HOME);
        citizenRequest.setPhones(Collections.singletonList(phone));

        mockMvc.perform(post("/api/test/proto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizenRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.id").value(987654321));
    }

    // Test for /api/test/proto (POST) with Protobuf content
    @Test
    void testCreateProto_shouldReturnCreatedPerson() throws Exception {
        PersonBinding.Person personRequest = PersonBinding.Person.newBuilder()
                .setName("Proto User")
                .setId(11223344)
                .setEmail("proto.user@example.com")
                .addPhones(PersonBinding.Person.PhoneNumber.newBuilder()
                        .setNumber("555-123-4567")
                        .setType(PersonBinding.Person.PhoneType.WORK)
                        .build())
                .build();

        MvcResult result = mockMvc.perform(post("/api/test/proto")
                        .contentType("application/x-protobuf")
                        .content(personRequest.toByteArray())
                        .accept("application/x-protobuf"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf"))
                .andReturn();
        
        byte[] responseBody = result.getResponse().getContentAsByteArray();
        PersonBinding.Person personResponse = PersonBinding.Person.parseFrom(responseBody);

        assertThat(personResponse.getName()).isEqualTo("Proto User");
        assertThat(personResponse.getId()).isEqualTo(11223344);
        assertThat(personResponse.getPhones(0).getNumber()).isEqualTo("555-123-4567");
    }
}
