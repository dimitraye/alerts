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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * This class test the methods of the class MedicalRecordController
 */
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

    /**
     *Test that a medical record is created
     * @throws Exception
     */
    @Test
    void shouldCreateMedicalRecord() throws Exception {
        //1 - Creation data : a person and a medical record
        // + setting the mediccal record of the person
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());

        //2 - Data processing : search a person by its fist name and last name
        // + create a new medical record
        // + update the person
        when(personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName())).thenReturn(person);
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
        when(personService.updatePerson(any(Person.class))).thenReturn(person);

        //3 - Test : test that the data is created
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    /**
     *Test that a medical record is created
     * @throws Exception
     */
    @Test
    void shouldRetur404WhenCreateIfPersonNotFound() throws Exception {
        //1 - Creation data : a person and a medical record
        // + setting the mediccal record of the person
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());

        //2 - Data processing : search a person by its fist name and last name
        // + create a new medical record
        // + update the person
        when(personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
            .thenReturn(null);

        //3 - Test : test that the data is created
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void shouldReturn400WhenCreateIfMedicalRecordExist() throws Exception {
        //1 - Creation data : a person and a medical record
        // + setting the mediccal record of the person
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());
        person.setMedicalRecord(medicalRecord);

        //2 - Data processing : search a person by its fist name and last name
        // + create a new medical record
        // + update the person
        when(personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName())).thenReturn(person);

        //3 - Test : test that the data is created
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    /**
     *Test that a medical record is created
     * @throws Exception
     */
    @Test
    void shouldReturn500WhenCreateIfMedicalRecordExist() throws Exception {
        //1 - Creation data : a person and a medical record
        // + setting the mediccal record of the person
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());

        //2 - Data processing : search a person by its fist name and last name
        // + create a new medical record
        // + update the person
        when(personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
            .thenReturn(person);
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(null);

        //3 - Test : test that the data is created
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().is5xxServerError())
            .andDo(print());
    }


    /**
     * Test that the medical record has been updated
     * @throws Exception
     */
    @Test
    void shouldUpdateMedicalRecord() throws Exception {
        //1 - Creation data :
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

        //2 - Data processing : search a person by its fist name and last name
        // + update the medical record of the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(updatedMedicalRecord);

        //3 - Test : test that the properties MedicalRecord AKA birthdate, medications and allergies has been updated
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


    /**
     * Test that the medical record has been updated
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenUpdateIfPersonNotFound() throws Exception {
        //1 - Creation data :


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

        //2 - Data processing : search a person by its fist name and last name
        // + update the medical record of the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(null);

        //3 - Test : test that the properties MedicalRecord AKA birthdate, medications and allergies has been updated
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }


    /**
     * Test that the medical record has been updated
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenUpdateIfMedicalRecordNotFound() throws Exception {
        //1 - Creation data :
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());

        MedicalRecord updatedMedicalRecord = DataTest.getMedicalRecord2();
        updatedMedicalRecord.setFirstName(person.getFirstName());
        updatedMedicalRecord.setLastName(person.getLastName());

        String firstName = medicalRecord.getFirstName();
        String lastName = medicalRecord.getLastName();

        //2 - Data processing : search a person by its fist name and last name
        // + update the medical record of the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);

        //3 - Test : test that the properties MedicalRecord AKA birthdate, medications and allergies has been updated
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    /**
     * Test that the medical record of a person has been deleted
     * @throws Exception
     */
    @Test
    void shouldDeleteMedicalRecord() throws Exception {
        //1 - Creation data : create a person and a medical record
        // + set the medical record of the person
        // + create the parameters of the request AKA firstName and lastName
        // + create a map that contains the 2 parameters
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        person.setMedicalRecord(medicalRecord);

        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        //2 - Data processing : return the person that have the same first name as firstName and last name as lastName
        // + update and return the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        when(personService.updatePerson(person)).thenReturn(person);

        //3 - Test : test that the medical record of the person has been deleted
        mockMvc.perform(delete("/medicalRecord").params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    /**
     * Test that the medical record of a person has been deleted
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenDeleteIfPersonNotFound() throws Exception {
        //1 - Creation data : create a person and a medical record
        // + set the medical record of the person
        // + create the parameters of the request AKA firstName and lastName
        // + create a map that contains the 2 parameters
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();
        person.setMedicalRecord(medicalRecord);

        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        //2 - Data processing : return the person that have the same first name as firstName and last name as lastName
        // + update and return the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(null);

        //3 - Test : test that the medical record of the person has been deleted
        mockMvc.perform(delete("/medicalRecord").params(paramsMap))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    /**
     * Test that the medical record of a person has been deleted
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenDeleteIfMedicalRecordNotFound() throws Exception {
        //1 - Creation data : create a person and a medical record
        // + set the medical record of the person
        // + create the parameters of the request AKA firstName and lastName
        // + create a map that contains the 2 parameters
        Person person = DataTest.getPerson1();
        MedicalRecord medicalRecord = DataTest.getMedicalRecord1();

        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        //2 - Data processing : return the person that have the same first name as firstName and last name as lastName
        // + update and return the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);

        //3 - Test : test that the medical record of the person has been deleted
        mockMvc.perform(delete("/medicalRecord").params(paramsMap))
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}
