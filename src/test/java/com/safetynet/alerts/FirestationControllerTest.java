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

/**
 * This class test the methods of the class FirestationController
 */
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

    /**
     * Test that a firestation has been created
     * @throws Exception
     */
    @Test
    void shouldCreateFirestation() throws Exception {
        //1 - Creation data : create a firestation
        Firestation firestationTest = DataTest.getFirestation1();

        //2 - Test : thest that the firestation has been created
        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestationTest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    /**
     * Test that a firestation has been created
     * @throws Exception
     */
    @Test
    void shoulReturn400WhenCreateFirestationIfFirestationExist() throws Exception {
        //1 - Creation data : create a firestation
        Firestation firestationTest = DataTest.getFirestation1();
        when(firestationService.findByAddress(firestationTest.getAddress())).thenReturn(firestationTest);

        //2 - Test : thest that the firestation has been created
        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestationTest)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }



    /**
     * Test that the number of a firestation has been updated
     * @throws Exception
     */
    @Test
    void shouldUpdateFirestation() throws Exception {

        //1 - Creation data :
        Firestation firestation = DataTest.getFirestation1();
        Firestation updatedFirestation = DataTest.getFirestation2();

        String address = "Univers 19";

        //2 - Data processing :
        when(firestationService.findByAddress(address)).thenReturn(firestation);
        when(firestationService.updateNumberFirestation(any(Firestation.class))).thenReturn(updatedFirestation);

        //3 - Test :
        mockMvc.perform(put("/firestation").param("address", address)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFirestation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(updatedFirestation.getAddress()))
                .andExpect(jsonPath("$.station").value(updatedFirestation.getStation()))
                .andDo(print());
    }


    /**
     * Test that the number of a firestation has been updated
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenUpdateIfFirestationDoesNotExist() throws Exception {

        //1 - Creation data :
        Firestation firestation = DataTest.getFirestation1();
        Firestation updatedFirestation = DataTest.getFirestation2();

        String address = "Univers 19";

        //2 - Data processing :
        when(firestationService.findByAddress(address)).thenReturn(null);

        //3 - Test :
        mockMvc.perform(put("/firestation").param("address", address)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFirestation)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }


    /**
     * Test that a firestation has been deleted
     * @throws Exception
     */
    @Test
    void shouldDeleteMappingFirestation() throws Exception {
        //1 - Creation data :
        Firestation firestation = DataTest.getFirestation1();

        String address = firestation.getAddress();

        //2 - Data processing : search a firestation by its first name and last name
        // + delete the firestation
        when(firestationService.findByAddress(address)).thenReturn(firestation);
        doNothing().when(firestationRepository).delete(firestation);

        //3 - Test : test that the firestation has been deleted
        mockMvc.perform(delete("/firestation").param("address", address))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    /**
     * Test that a firestation has been deleted
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenDeleteIfFirestationDoesNotExist() throws Exception {
        //1 - Creation data :
        Firestation firestation = DataTest.getFirestation1();

        String address = firestation.getAddress();

        //2 - Data processing : search a firestation by its first name and last name
        // + delete the firestation
        when(firestationService.findByAddress(address)).thenReturn(null);

        //3 - Test : test that the firestation has been deleted
        mockMvc.perform(delete("/firestation").param("address", address))
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}
