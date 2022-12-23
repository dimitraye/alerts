package com.safetynet.alerts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


import com.safetynet.alerts.DataTest;
import com.safetynet.alerts.dto.ContainerPersonDTO;
import com.safetynet.alerts.dto.FamilyDTO;
import com.safetynet.alerts.dto.PersonFirestationDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PersonServiceTest {
  @Mock
  private FirestationRepository firestationRepository;

  @Mock
  private PersonRepository personRepository;

  @Mock
  private MedicalRecordRepository medicalRecordRepository;

  @InjectMocks
  private FirestationServiceImpl firestationService;

  @InjectMocks
  private PersonServiceImpl personService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getAllPersonsTest() {
    List<Person> list = new ArrayList<>();
    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    list.add(person1);
    list.add(person2);
    list.add(person3);

    when(personRepository.findAll()).thenReturn(list);

    //test
    List<Person> personList = personService.getAllPersons();

    assertEquals(3, personList.size());
    verify(personRepository, times(1)).findAll();
  }

  @Test
  public void createPersonTest() {
    Person person = DataTest.getPerson1();

    personService.addPerson(person);

    verify(personRepository, times(1)).save(person);
  }

  @Test
  void shouldUpdatePerson() {

    Person person = DataTest.getPerson1();

    personService.updatePerson(person);

    verify(personRepository, times(1)).save(person);
  }

  @Test
  public void deletePersonTest() {

    Person person = DataTest.getPerson1();

    personService.deletePerson(person);

    verify(personRepository, times(1)).delete(person);
  }

  @Test
  public void findEmailsByCityTest() {

    Set<String> set = Set.of("email1", "email2", "email3");

    when(personRepository.findEmailsByCity("paris")).thenReturn(set);

    //test
    Set<String> emailSet = personService.findEmailsByCity("paris");

    assertEquals(3, emailSet.size());
    verify(personRepository, times(1)).findEmailsByCity("paris");
  }

  @Test
  public void getAllByAddressTest() {
    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    Set<Person> set = Set.of(person1, person2, person3);

    when(personRepository.findAllByAddress("address test")).thenReturn(set);

    //test
    Set<Person> personSet = personService.findAllByAddress("address test");

    assertEquals(3, personSet.size());
    verify(personRepository, times(1)).findAllByAddress("address test");
  }

  @Test
  public void findPhoneByFirestationStationTest() {

    Set<String> set = Set.of("0102030405", "0102030406", "0102030407");

    when(personRepository.findPhonesByFirestationStation(1)).thenReturn(set);

    //test
    Set<String> phoneSet = personService.findPhoneByFirestationStation(1);

    assertEquals(3, phoneSet.size());
    verify(personRepository, times(1)).findPhonesByFirestationStation(1);
  }

  @Test
  public void findByFirestationStationInTest() {
    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    Set<Person> set = Set.of(person1, person2, person3);
    List<Integer> stations = List.of(1, 2, 5);

    when(personRepository.findByFirestationStationIn(stations)).thenReturn(set);

    //test
    Set<Person> personSet = personService.findByFirestationStationIn(stations);

    assertEquals(3, personSet.size());
    verify(personRepository, times(1)).findByFirestationStationIn(stations);
  }

  @Test
  public void findByAddresTest() {

    when(firestationRepository.findFirestationByAddress("address test")).thenReturn(
        DataTest.getFirestation1());

    Firestation firestation = firestationService.findByAddress("address test");

    assertEquals("15 rue des champs", firestation.getAddress());
    assertEquals(5, firestation.getStation());
  }

  @Test
  public void getFamilyFromAddressTest() throws ParseException {
    Firestation firestation1 = DataTest.getFirestation1();

    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person child1 = DataTest.getPerson3();

    person1.setFirestation(firestation1);
    person1.setFirestation(firestation1);
    person1.setFirestation(firestation1);

    person1.setMedicalRecord(DataTest.getMedicalRecord1());
    person2.setMedicalRecord(DataTest.getMedicalRecord2());
    child1.setMedicalRecord(DataTest.getChildMedicalRecord());

    Set<Person> set = Set.of(person1, person2, child1);

    when(personService.findAllByAddress(person1.getAddress())).thenReturn(set);

    FamilyDTO familyDTO = personService.getFamilyFromAddress(person1.getAddress());

    assertEquals(1, familyDTO.getChildren().size());
  }

  @Test
  public void findByFirestationStationTest() throws ParseException {
    Firestation firestation1 = DataTest.getFirestation1();

    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person child1 = DataTest.getPerson3();

    person1.setFirestation(firestation1);
    person1.setFirestation(firestation1);
    person1.setFirestation(firestation1);

    person1.setMedicalRecord(DataTest.getMedicalRecord1());
    person2.setMedicalRecord(DataTest.getMedicalRecord2());
    child1.setMedicalRecord(DataTest.getChildMedicalRecord());

    Set<Person> set = Set.of(person1, person2, child1);
    List<Integer> stations = List.of(firestation1.getStation());

    when(personService.findByFirestationStationIn(stations)).thenReturn(set);

    ContainerPersonDTO containerPersonDTO =
        personService.findByFirestationStation(firestation1.getStation());

    assertEquals(1, containerPersonDTO.getChildrenNumber());
  }

  @Test
  public void findPersonsByFirestationStationInTest() {
    Firestation firestation1 = DataTest.getFirestation1();
    Firestation firestation2 = DataTest.getFirestation2();

    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    person1.setFirestation(firestation1);
    person2.setFirestation(firestation1);
    person3.setFirestation(firestation2);

    Set<Person> set = Set.of(person1, person2, person3);
    List<Integer> stations = List.of(1, 2);

    when(personService.findByFirestationStationIn(stations)).thenReturn(set);

    Map<String, Set<PersonFirestationDTO>> personsByaddressDTO =
        personService.findPersonsByFirestationStationIn(stations);

    assertEquals(3, personsByaddressDTO.values().size());
  }

  @Test
  public void findPersonsByFirestationStationInShouldReturnNullTest() {

    Set<Person> set = new HashSet<>();
    List<Integer> stations = List.of(1, 2);

    when(personService.findByFirestationStationIn(stations)).thenReturn(set);

    Map<String, Set<PersonFirestationDTO>> personsByaddressDTO = personService
        .findPersonsByFirestationStationIn(stations);

    assertEquals(null, personsByaddressDTO);
  }

  @Test
  public void findByFirstNameAndLastNameTest() {
    Person person = DataTest.getPerson1();

    when(personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
        .thenReturn(person);

    Person personFromDb =
        personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

    assertEquals(person.getFirstName(), personFromDb.getFirstName());
    assertEquals(person.getLastName(), personFromDb.getLastName());
    assertEquals(person.getAddress(), personFromDb.getAddress());
    assertEquals(person.getCity(), personFromDb.getCity());
    assertEquals(person.getZip(), personFromDb.getZip());
    assertEquals(person.getPhone(), personFromDb.getPhone());
    assertEquals(person.getEmail(), personFromDb.getEmail());
  }
}
