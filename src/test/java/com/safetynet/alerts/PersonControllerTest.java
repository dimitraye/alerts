package com.safetynet.alerts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controllers.PersonController;
import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.IPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * This class test the methods of the class PersonController
 */
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

    /**
     * Test that person has been created
     * @throws Exception
     */
    @Test
    void shouldCreatePerson() throws Exception {
        //1 - Creation data : create a person
        Person personTest = DataTest.getPerson1();

        //2 - Test : test that the person has been created
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personTest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    /**
     * Test that person has been created
     * @throws Exception
     */
    @Test
    void shouldReturn400WhenCreateIfërsonExist() throws Exception {
        //1 - Creation data : create a person
        Person personTest = DataTest.getPerson1();

        //2 - Test : test that the person has been created
        when(personService.findByFirstNameAndLastName(personTest.getFirstName(),
            personTest.getLastName())).thenReturn(personTest);
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personTest)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    /**
     * Test that a person has been updated
     * @throws Exception
     */
    @Test
    void shouldUpdatePerson() throws Exception {

        //1 - Creation data :
        Person person = DataTest.getPerson1();
        Person updatedPerson = Person.builder()
                .firstName("Dayle")
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

        //2 - Data processing : Search a person by its first name and last name
        // + update the person and return the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);
        when(personService.updatePerson(any(Person.class))).thenReturn(updatedPerson);

        //3 - Test : test that the person has been updated
        mockMvc.perform(put("/person").params(paramsMap)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(updatedPerson.getAddress()))
                .andExpect(jsonPath("$.city").value(updatedPerson.getCity()))
                .andExpect(jsonPath("$.zip").value(updatedPerson.getZip()))
                .andDo(print());
    }


    /**
     * Test that a person has been updated
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenUpdateIfPersonNotFound() throws Exception {

        //1 - Creation data :
        Person person = DataTest.getPerson1();
        Person updatedPerson = Person.builder()
            .firstName("Dayle")
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

        //2 - Data processing : Search a person by its first name and last name
        // + update the person and return the person
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(null);

        //3 - Test : test that the person has been updated
        mockMvc.perform(put("/person").params(paramsMap)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }


    /**
     * Test that a person has been deleted
     * @throws Exception
     */
    @Test
    void shouldDeletePerson() throws Exception {
        Person person = DataTest.getPerson1();

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


    /**
     * Test that a person has been deleted
     * @throws Exception
     */
    @Test
    void shouldReturn404WhenDeleteIfPersonDoesNotExist() throws Exception {
        //1 - Creation data :
        Person person = DataTest.getPerson1();

        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        //2 - Data processing : search a person by its first and last name then return nothing
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(null);

        //3 - Test : test that an exception is thrown when the person does not exist.
        mockMvc.perform(delete("/person").params(paramsMap))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    /**
     * Test that the method return a list of emails ordered by city
     * @throws Exception
     */
    @Test
    void findEmailsByCityTest() throws Exception {
        //1 - Creation data :
        Person person1 = DataTest.getPerson1();
        Person person2 = DataTest.getPerson2();
        person2.setCity(person1.getCity());
        Person person3 = DataTest.getPerson3();
        person3.setCity(person1.getCity());

        Set<String> emails = Set.of(person1.getEmail(), person2.getEmail(), person3.getEmail());
        String city = person1.getCity();

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("city", city);

        //2 - Test : test that the method return a list of emails ordered by city
        when(personService.findEmailsByCity(city)).thenReturn(emails);
        mockMvc.perform(get("/communityEmail").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(emails.size()))
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(emails.toArray())))
                .andDo(print());
    }


    /**
     * Test that the informations of a person has been returned
     * @throws Exception
     */
    @Test
    void getPersonInfoTest () throws Exception {
        //1 - Creation data :
        Person person = DataTest.getPerson1();
        person.setMedicalRecord(DataTest.getMedicalRecord1());
        PersonInfoDTO personInfo = new PersonInfoDTO(person);
        Object [] medications = person.getMedicalRecord().getMedications().toArray();
        Object [] allergies = person.getMedicalRecord().getAllergies().toArray();

        String firstName = person.getFirstName();
        String lastName = person.getLastName();


        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firstName", firstName);
        paramsMap.add("lastName", lastName);

        //2 - Data processing : search a person by its first and last name
        when(personService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(person);

        //3 - Test : test that a the informations of a person has been returned
        mockMvc.perform(get("/personInfo").params(paramsMap)).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(personInfo.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(personInfo.getLastName()))
                .andExpect(jsonPath("$.address").value(personInfo.getAddress()))
                .andExpect(jsonPath("$.email").value(personInfo.getEmail()))
                .andExpect(jsonPath("$.medicalRecord.age").value(personInfo.getMedicalRecord().getAge()))
                .andExpect(jsonPath("$.medicalRecord.medications[*]").value(containsInAnyOrder(medications)))
                .andExpect(jsonPath("$.medicalRecord.allergies[*]").value(containsInAnyOrder(allergies)))
                .andDo(print());
    }


    /**
     * Test that the method return a list of all the persons that have the same address
     * This list have two lists, a list of children (<= 18 years old), and a list of adults
     * @throws Exception
     */
    @Test
    void shouldReturngetFamilyFromAddress() throws Exception {

        //1 - Creation data : create 3 persons
        // + Create a list a set of children and a set of adults
        Person person1 = DataTest.getPerson1();
        person1.setMedicalRecord(DataTest.getMedicalRecord1());
        FamilyMemberDTO person1DTO = new FamilyMemberDTO(person1);
        Person person2 = DataTest.getPerson2();
        FamilyMemberDTO person2DTO = new FamilyMemberDTO(person2);
        person2.setAddress(person1.getAddress());
        person2.setMedicalRecord(DataTest.getMedicalRecord2());
        Person child = DataTest.getPerson3();
        child.setAddress(person1.getAddress());
        child.setMedicalRecord(DataTest.getChildMedicalRecord());
        FamilyMemberDTO childDTO = new FamilyMemberDTO(child);

        FamilyDTO family = new FamilyDTO();
        family.setChildren(Set.of(childDTO));
        family.setAdults(Set.of(person1DTO, person2DTO));

        //2 - Test : test that a list of persons that have the same address is returned
        // + return the number of children and the number of adults
        when(personService.getFamilyFromAddress(person1.getAddress())).thenReturn(family);
        mockMvc.perform(get("/childAlert").param("address", person1.getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children.size()").value(family.getChildren().size()))
                .andExpect(jsonPath("$.adults.size()").value(family.getAdults().size()))
                .andDo(print());
    }

    /**
     * Test that return the phone of the persons that have the same station Number
     * @throws Exception
     */
    @Test
    void shouldFindPhonesByFirestationStation() throws Exception {
        //1 - Creation data : create 2 persons
        // + create a set that contains the phone number of these people
        // + create a station number
        Person person1 = DataTest.getPerson1();
        person1.setFirestation(DataTest.getFirestation1());
        Person person2 = DataTest.getPerson2();
        person2.setFirestation(DataTest.getFirestation1());
        person2.setCity(person1.getCity());


        Set<String> phones = Set.of(person1.getPhone(), person2.getPhone());

        int stationNumber = person1.getFirestation().getStation();
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("firestation", String.valueOf(stationNumber));

        //2 - Test : test that the phone number of the persons that belongs to the same firestation
        when(personService.findPhoneByFirestationStation(stationNumber)).thenReturn(phones);
        mockMvc.perform(get("/phoneAlert").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(phones.size()))
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(phones.toArray())))
                .andDo(print());
    }


    /**
     * Test that he mehod return all the persons that have the same address
     * @throws Exception
     */
    @Test
    void shouldReturnPersonByAddress() throws Exception {
        //1 - Creation data : create 3 persons
        Person person1 = DataTest.getPerson1();
        person1.setMedicalRecord(DataTest.getMedicalRecord1());
        person1.setFirestation(DataTest.getFirestation1());

        Person person2 = DataTest.getPerson2();
        person2.setFirestation(DataTest.getFirestation1());
        person2.setAddress(person1.getAddress());
        person2.setMedicalRecord(DataTest.getMedicalRecord2());

        Person person3 = DataTest.getPerson3();
        person3.setFirestation(DataTest.getFirestation1());
        person3.setAddress(person1.getAddress());
        person3.setMedicalRecord(DataTest.getChildMedicalRecord());

        Set<Person> personsSet = Set.of(person1, person2, person3);

        //2 - Test: test that a list of person is that have the same address is returned
        when(personService.findAllByAddress(person1.getAddress())).thenReturn(personsSet);
        mockMvc.perform(get("/fire").param("address", person1.getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(personsSet.size()))
                .andDo(print());
    }

    /**
     * Test that the method return all the persons that have the same station number
     * @throws Exception
     */
    @Test
    void shouldFindByFirestationStationIn() throws Exception {
        //1 - Creation data : create 3 persons
        Person person1 = DataTest.getPerson1();
        person1.setMedicalRecord(DataTest.getMedicalRecord1());
        person1.setFirestation(new Firestation(person1.getAddress(), 1));

        Person person2 = DataTest.getPerson2();
        person2.setMedicalRecord(DataTest.getMedicalRecord2());
        person2.setAddress("rue de paris");
        person2.setFirestation(new Firestation(person2.getAddress(), 2));

        Person person3 = DataTest.getPerson3();
        person3.setAddress(person2.getAddress());
        person3.setMedicalRecord(DataTest.getChildMedicalRecord());
        person3.setFirestation(new Firestation(person2.getAddress(), 2));

        Set<Person> personsSet = Set.of(person1, person2, person3);

        Map<String, List<Person>> personsByAddress = personsSet.stream().collect(Collectors
                .groupingBy(person -> person.getAddress()));

        Map<String, Set<PersonFirestationDTO>> personsByaddressDTO = new HashMap<>();
        personsByAddress.forEach((address, persons) -> {
            Set<PersonFirestationDTO> personFirestationDTOS = persons.stream()
                    .map(person -> new PersonFirestationDTO(person)).collect(Collectors.toSet());
            personsByaddressDTO.put(address, personFirestationDTOS);
        });

        List<Integer> stations = List.of(person1.getFirestation().getStation(),
                person3.getFirestation().getStation());

        String stationsStr = stations.stream()
                .map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        //2 - Test : test that a list of person is that belongs to the same station is returned for
        // every stations put in parameter
        when(personService.findPersonsByFirestationStationIn(stations)).thenReturn(personsByaddressDTO);
        mockMvc.perform(get("/flood/stations").param("stations", stationsStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(personsByaddressDTO.size()))
                .andDo(print());
    }


    /**
     * Test that the method return all the persons that have the same firestation
     * @throws Exception
     */
    @Test
    void shouldFindByFirestationStation() throws Exception {
        //1 - Create data : create a container that will contain all thes persons separated in 2 sets, one for the adults, and the other for the children
        // + create 3 persons, 2 adults and one child
        // + create a set
        ContainerPersonDTO containerPersonDTO = new ContainerPersonDTO();

        Person person1 = DataTest.getPerson1();
        person1.setMedicalRecord(DataTest.getMedicalRecord1());
        person1.setFirestation(DataTest.getFirestation1());
        Person person2 = DataTest.getPerson2();
        person2.setMedicalRecord(DataTest.getMedicalRecord2());
        person2.setFirestation(DataTest.getFirestation1());
        Person person3 = DataTest.getPerson3();
        person3.setMedicalRecord(DataTest.getChildMedicalRecord());
        person3.setFirestation(DataTest.getFirestation1());

        Set<Person> persons = Set.of(person1, person2, person3);
        Set<PersonDTO> personDTOS = persons.stream().map(person -> new PersonDTO()).collect(Collectors.toSet());
        containerPersonDTO.setPersons(personDTOS);
        containerPersonDTO.setChildrenNumber(1);
        containerPersonDTO.setAdultNumber(2);

        int stationNumber = person1.getFirestation().getStation();
        //2 - Test : test that a list of person is that belongs to the same station is returned
        when(personService.findByFirestationStation(stationNumber)).thenReturn(containerPersonDTO);
        mockMvc.perform(get("/firestation").param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons.size()").value(personDTOS.size()))
                .andDo(print());
    }


}
