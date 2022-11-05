package com.safetynet.alerts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controllers.FirestationController;
import com.safetynet.alerts.controllers.PersonController;
import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.IFirestationService;
import com.safetynet.alerts.service.IPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @MockBean
    private FirestationRepository firestationRepository;

    @MockBean
    private IPersonService personService;

    @MockBean
    private IFirestationService firestationService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateFirestation() throws Exception {
        Firestation firestationTest = DataTest.getFirestation1();

        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestationTest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    void shouldUpdateFirestation() throws Exception {

        Firestation firestation = DataTest.getFirestation1();
        Firestation updatedFirestation = DataTest.getFirestation2();

        String address = "Univers 19";

        when(firestationService.findByAddress(address)).thenReturn(firestation);
        when(firestationService.updateNumberFirestation(any(Firestation.class))).thenReturn(updatedFirestation);

        mockMvc.perform(put("/firestation").param("address", address)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFirestation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(updatedFirestation.getAddress()))
                .andExpect(jsonPath("$.station").value(updatedFirestation.getStation()))
                .andDo(print());
    }


    @Test
    void shouldDeleteMappingFirestation() throws Exception {
        Firestation firestation = DataTest.getFirestation1();

        String address = firestation.getAddress();

        when(firestationService.findByAddress(address)).thenReturn(firestation);
        doNothing().when(firestationRepository).delete(firestation);

        mockMvc.perform(delete("/firestation").param("address", address))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
