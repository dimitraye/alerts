package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controllers.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.IMedicalRecordService;
import com.safetynet.alerts.service.IPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private IPersonService personService;

    @MockBean
    private FirestationRepository firestationRepository;

    @MockBean
    private IMedicalRecordService medicalRecordService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MedicalRecordController medicalRecordController;

    @Test
    void shouldCreateMedicalRecord() throws Exception {

        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());


        when(personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName())).thenReturn(person);
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
        when(personService.updatePerson(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    void shouldUpdateMedicalRecord() throws Exception {

        DateFormat df  = new SimpleDateFormat("dd/MM/yyyy");
        objectMapper.setDateFormat(df);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders
                .standaloneSetup(medicalRecordController)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();

        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        person.setMedicalRecord(medicalRecord);
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());

        MedicalRecord updatedMedicalRecord = DataTest.getMedicalRecord2();
        updatedMedicalRecord.setFirstName(person.getFirstName());
        updatedMedicalRecord.setLastName(person.getLastName());

        String firstName = medicalRecord.getFirstName();
        String lastName = medicalRecord.getLastName();

        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(updatedMedicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.birthdate").value(df.format(updatedMedicalRecord.getBirthdate())))

                .andExpect(jsonPath("$.medications[*]")
                        .value(containsInAnyOrder(medicalRecord.getMedications().toArray())))
                .andExpect(jsonPath("$.allergies[*]")
                        .value(containsInAnyOrder(updatedMedicalRecord.getAllergies().toArray())))
                .andDo(print());
    }


    @Test
    void shouldDeleteMedicalRecord() throws Exception {
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        person.setMedicalRecord(medicalRecord);

        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        when(personService.updatePerson(person)).thenReturn(person);

        mockMvc.perform(delete("/medicalRecord").params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
