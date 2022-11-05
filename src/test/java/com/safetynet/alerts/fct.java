package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controllers.FirestationController;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
public class fct {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationRepository firestationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddMappingFirestation() throws Exception {
        Firestation firestationTest = DataTest.getFirestation1();

        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestationTest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldCreatePerson() throws Exception {
        Person personTest = DataTest.getPerson1();

        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personTest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

}

