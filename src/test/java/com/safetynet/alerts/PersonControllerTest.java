package com.safetynet.alerts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controllers.PersonController;
import com.safetynet.alerts.dto.FamilyDTO;
import com.safetynet.alerts.dto.FamilyMemberDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.IPersonService;
import com.safetynet.alerts.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @MockBean
    private FirestationRepository firestationRepository;

    @MockBean
    private IPersonService personService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePerson() throws Exception {
        Person personTest = getPerson1();

        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personTest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    void shouldUpdatePerson() throws Exception {

        Person person = getPerson1();
        Person updatedPerson = Person.builder().firstName("Dayle")
                .lastName("Xeyb")
                .address("Univers 10")
                .city("Poitiers")
                .zip("11111111")
                .phone("07-67-61-03-49")
                .email("W.M.X@gmail.com").build();

        String firstName = "Dayle";
        String lastName = "Xeyb";

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        when(personService.updatePerson(any(Person.class))).thenReturn(updatedPerson);



        mockMvc.perform(put("/person").params(paramsMap)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(updatedPerson.getAddress()))
                .andExpect(jsonPath("$.city").value(updatedPerson.getCity()))
                .andExpect(jsonPath("$.zip").value(updatedPerson.getZip()))
                .andDo(print());
    }


    @Test
    void shouldDeletePerson() throws Exception {
        Person person = getPerson1();

        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        doNothing().when(personRepository).delete(person);

        mockMvc.perform(delete("/person").params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void findEmailsByCityTest() throws Exception {

        Person person1 = getPerson1();
        Person person2 = getPerson2();
        person2.setCity(person1.getCity());
        Person person3 = getPerson3();
        person3.setCity(person1.getCity());


        Set<String> emails = Set.of(person1.getEmail(), person2.getEmail(), person3.getEmail());

        String city = person1.getCity();
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("city", city);

        when(personService.findEmailsByCity(city)).thenReturn(emails);
        mockMvc.perform(get("/communityEmail").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(emails.size()))
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(emails.toArray())))
                .andDo(print());
    }


    @Test
    void getPersonInfoTest () throws Exception {
        Person person = getPerson1();
        person.setMedicalRecord(getMedicalRecord1());
        PersonInfoDTO personInfo = new PersonInfoDTO(person);
        Object [] medications = person.getMedicalRecord().getMedications().toArray();
        Object [] allergies = person.getMedicalRecord().getAllergies().toArray();

        String firstName = person.getFirstName();
        String lastName = person.getLastName();


        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        mockMvc.perform(get("/personInfo").params(paramsMap)).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(personInfo.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(personInfo.getLastName()))
                .andExpect(jsonPath("$.address").value(personInfo.getAddress()))
                .andExpect(jsonPath("$.email").value(personInfo.getEmail()))
                .andExpect(jsonPath("$.medicalRecord.age").value(personInfo.getMedicalRecord().getAge()))
                .andExpect(jsonPath("$.medicalRecord.medications[*]").value(containsInAnyOrder(medications)))
                .andExpect(jsonPath("$.medicalRecord.allergies[*]") .value(containsInAnyOrder(allergies)))
                .andDo(print());
    }



    @Test
    void shouldReturngetFamilyFromAddress() throws Exception {

        Person person1 = getPerson1();
        person1.setMedicalRecord(getMedicalRecord1());
        FamilyMemberDTO person1DTO = new FamilyMemberDTO(person1);
        Person person2 = getPerson2();
        FamilyMemberDTO person2DTO = new FamilyMemberDTO(person2);
        person2.setAddress(person1.getAddress());
        person2.setMedicalRecord(getMedicalRecord2());
        Person child = getPerson3();
        child.setAddress(person1.getAddress());
        child.setMedicalRecord(getChildMedicalRecord());
        FamilyMemberDTO childDTO = new FamilyMemberDTO(child);

        FamilyDTO family = new FamilyDTO();
        family.setChildren(Set.of(childDTO));
        family.setAdults(Set.of(person1DTO, person2DTO));

        when(personService.getFamilyFromAddress(person1.getAddress())).thenReturn(family);
        mockMvc.perform(get("/childAlert").param("address", person1.getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children[0].firstName").value(childDTO.getFirstName()))
                .andExpect(jsonPath("$.children[0].lastName").value(childDTO.getLastName()))
                .andExpect(jsonPath("$.children[0].age").value(childDTO.getAge()))
                .andExpect(jsonPath("$.adults[0].firstName").value(person2DTO.getFirstName()))
                .andExpect(jsonPath("$.adults[0].lastName").value(person2DTO.getLastName()))
                .andExpect(jsonPath("$.adults[0].age").value(person2DTO.getAge()))
                .andExpect(jsonPath("$.adults[1].firstName").value(person1DTO.getFirstName()))
                .andExpect(jsonPath("$.adults[1].lastName").value(person1DTO.getLastName()))
                .andExpect(jsonPath("$.adults[1].age").value(person1DTO.getAge()))
                .andDo(print());
    }

    @Test
    void findPhonesByFirestation() throws Exception {

        Person person1 = getPerson1();
        person1.setFirestation(getFirestation1());
        Person person2 = getPerson2();
        person2.setFirestation(getFirestation1());
        person2.setCity(person1.getCity());


        Set<String> phones = Set.of(person1.getPhone(), person2.getPhone());

        int stationNumber = person1.getFirestation().getStation();
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firestation", String.valueOf(stationNumber));

        when(personService.findPhoneByFirestationStation(stationNumber)).thenReturn(phones);
        mockMvc.perform(get("/phoneAlert").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(phones.size()))
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(phones.toArray())))
                .andDo(print());
    }



    public Person getPerson1() {
        return Person.builder().firstName("Dayle")
                .lastName("Xeyb")
                .address("Univers 159789753123")
                .city("BladeWorkisya")
                .zip("999999")
                .phone("07-67-61-03-49")
                .email("W.M.X@gmail.com").build();
    }

    public Person getPerson2() {
        return Person.builder().firstName("Dyron")
                .lastName("Davis")
                .address("Univers 153")
                .city("Neo Paris")
                .zip("12311")
                .phone("06-07-61-03-99")
                .email("wise.researcher@gmail.com").build();
    }

    public Person getPerson3() {
        return Person.builder().firstName("Lyuke")
                .lastName("Lucius")
                .address("Univers 15")
                .city("Univerity Galaxy Wise")
                .zip("7777")
                .phone("07-08-12-03-05")
                .email("professor@gmail.com").build();
    }

    public Person getPerson4() {
        return Person.builder().firstName("Shiro")
                .lastName("Swordiva")
                .address("Univers 159789753123")
                .city("BladeWorkisya")
                .zip("123456")
                .phone("07-67-61-03-49")
                .email("sword.queen@gmail.com").build();
    }

    public MedicalRecord getMedicalRecord1() throws ParseException {
        return MedicalRecord.builder()
                .birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/1999"))
                .medications(Set.of("Paracetamol", "Aspirine"))
                .allergies(Set.of("pollen", "nuts")).build();
    }

    public MedicalRecord getMedicalRecord2() throws ParseException {
        return MedicalRecord.builder()
                .birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("15/11/1959"))
                .medications(Set.of("Prednisolone"))
                .allergies(Set.of("cats","dogs" )).build();
    }

    public MedicalRecord getChildMedicalRecord() throws ParseException {
        return MedicalRecord.builder()
                .birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("13/05/2015"))
                .medications(Set.of("Ventoline", "Symbicort"))
                .allergies(Set.of("pollen", "bees")).build();
    }

    public Firestation getFirestation1() {
        return new Firestation("15 rue des champs", 5);
    }

    public Firestation getFirestation2() {
        return new Firestation("17 rue de la mairie", 5);
    }

    public Firestation getFirestation3() {
        return new Firestation("1 place des cristaux", 7);
    }
}
