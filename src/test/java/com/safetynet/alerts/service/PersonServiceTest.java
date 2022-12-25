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

/**
 * This class test the methods of the class IPersonService
 */
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

  /**
   * Test that a list that contains all the persons is returned correctly
   */
  @Test
  public void getAllPersonsTest() {
    //1 - Data Processing :
    List<Person> list = new ArrayList<>();
    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    list.add(person1);
    list.add(person2);
    list.add(person3);

    //2 - Data Processing : Return a list of persons
    when(personRepository.findAll()).thenReturn(list);

    //3 - Test : verify that a list of persons is returned correctly
    List<Person> personList = personService.getAllPersons();

    assertEquals(3, personList.size());
    verify(personRepository, times(1)).findAll();
  }

  /**
   * Test that a person is created correctly
   */
  @Test
  public void createPersonTest() {
    //1 - Data Creation :
    Person person = DataTest.getPerson1();

    //2 - Data Processing : Save the person
    personService.addPerson(person);

    //3 - Test : Verify that the person has been saved correctly
    verify(personRepository, times(1)).save(person);
  }

  /**
   * Test that a person has been updated correctly
   */
  @Test
  void shouldUpdatePerson() {

    //1 - Data Creation :
    Person person = DataTest.getPerson1();

    //2 - Data Processing : Update the person
    personService.updatePerson(person);

    //3 - Test : Verify that the person has been updated correctly
    verify(personRepository, times(1)).save(person);
  }

  /**
   * Test that a person is correctly deleted
   */
  @Test
  public void deletePersonTest() {
    //1 - Data Creation :
    Person person = DataTest.getPerson1();

    //2 - Data Processing : Delete the person
    personService.deletePerson(person);

    //3 - Test : Verify that the person has been deleted correctly
    verify(personRepository, times(1)).delete(person);
  }

  /**
   * test that a set that contains the email address of the persons that lives in the city in
   * parameter
   */
  @Test
  public void findEmailsByCityTest() {

    //1 - Data Creation :
    Set<String> set = Set.of("email1", "email2", "email3");

    //2 - Data Processing : return a set of emails that belongs to the persons that
    // are from the city in parameter
    when(personRepository.findEmailsByCity("paris")).thenReturn(set);

    //3 - Test : verify that a set of emails is returned correctly
    Set<String> emailSet = personService.findEmailsByCity("paris");

    assertEquals(3, emailSet.size());
    verify(personRepository, times(1)).findEmailsByCity("paris");
  }

  /**
   * test that a set that contains the persons that lives in the city in
   * parameter
   */
  @Test
  public void getAllByAddressTest() {
    // 1 - Data Creation :
    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    Set<Person> set = Set.of(person1, person2, person3);

    //2 - Data Processing : Return a set of all the persons
    when(personRepository.findAllByAddress("address test")).thenReturn(set);

    //3 - Test : Verify that all the persons that have their address in the research parameter
    // has been returned
    Set<Person> personSet = personService.findAllByAddress("address test");
    System.out.println(personSet);
    assertEquals(3, personSet.size());
    verify(personRepository, times(1)).findAllByAddress("address test");
  }

  /**
   * Test that the method return a set of phone numbers that belongs to the persons
   * that belongs to the firestation in the parameter
   */
  @Test
  public void findPhoneByFirestationStationTest() {
    //1 - Data Creation :
    Set<String> set = Set.of("0102030405", "0102030406", "0102030407");

    //2 - Data Processing : Return a set of phone numbers that belongs to the persons
    //that belongs to the station in the parameter
    when(personRepository.findPhonesByFirestationStation(1)).thenReturn(set);

    //3 - Test : Verify that all the phones numbers that that belongs to the persons
    //that belongs to the station in the parameter has been returned
    Set<String> phoneSet = personService.findPhoneByFirestationStation(1);

    assertEquals(3, phoneSet.size());
    verify(personRepository, times(1)).findPhonesByFirestationStation(1);
  }

  /**
   * Test that the method return a set of persons linked to
   * that belongs to the stations in the parameter
   */
  @Test
  public void findByFirestationStationInTest() {
    //1 - Data Creation
    Person person1 = DataTest.getPerson1();
    Person person2 = DataTest.getPerson2();
    Person person3 = DataTest.getPerson3();

    Set<Person> set = Set.of(person1, person2, person3);
    List<Integer> stations = List.of(1, 2, 5);

    //2 - Data Processing : Return a set persons ordered by firestations
    // (the firestations have to be in the research parameter)
    when(personRepository.findByFirestationStationIn(stations)).thenReturn(set);

    //3 - Test : Verify that all the phones numbers that that belongs to the persons
    //that belongs to the stations in the parameter has been returned
    Set<Person> personSet = personService.findByFirestationStationIn(stations);

    assertEquals(3, personSet.size());
    verify(personRepository, times(1)).findByFirestationStationIn(stations);
  }

  /**
   * Test that the correct firestation is return when we enter its address
   */
  @Test
  public void findByAddresTest() {
    //1 - Data Processing  : find the firestation by entering its address
    when(firestationRepository.findFirestationByAddress("address test")).thenReturn(
        DataTest.getFirestation1());

    Firestation firestation = firestationService.findByAddress("address test");

    //2 - Test : test that the correct firestation is returned when we enter its address
    assertEquals("15 rue des champs", firestation.getAddress());
    assertEquals(5, firestation.getStation());
  }

  /**
   * Test that the method returns all thepersons that have the same address
   * @throws ParseException
   */
  @Test
  public void getFamilyFromAddressTest() throws ParseException {
    //1 - Data creation :
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

    //2 - Data Processing : find all the persons that have the same address as the one in parameter
    when(personService.findAllByAddress(person1.getAddress())).thenReturn(set);

    FamilyDTO familyDTO = personService.getFamilyFromAddress(person1.getAddress());

    //3 - Test :
    assertEquals(1, familyDTO.getChildren().size());
  }

  /**
   * Test that the method return
   * @throws ParseException
   */
  @Test
  public void findByFirestationStationTest() throws ParseException {
    //1 - Data Creation :
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

    //2 - Data Processing :
    when(personService.findByFirestationStationIn(stations)).thenReturn(set);

    ContainerPersonDTO containerPersonDTO =
        personService.findByFirestationStation(firestation1.getStation());

    //3 - Test :
    assertEquals(1, containerPersonDTO.getChildrenNumber());
  }

  /**
   *
   */
  @Test
  public void findPersonsByFirestationStationInTest() {
    //1 - Data Creation :
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

    //2 - Data Processing : return a set of person founded by their firestation
    when(personService.findByFirestationStationIn(stations)).thenReturn(set);

    Map<String, Set<PersonFirestationDTO>> personsByaddressDTO =
        personService.findPersonsByFirestationStationIn(stations);

    //3 - Test : Test that result is a set of the persons founded by their their firestation
    assertEquals(3, personsByaddressDTO.values().size());
  }

  /**
   * Test that the method return nothing instead of returning the persons of the firestations
   * that are in parameter
   */
  @Test
  public void findPersonsByFirestationStationInShouldReturnNullTest() {
    //1 - Data Creation :
    Set<Person> set = new HashSet<>();
    List<Integer> stations = List.of(1, 2);

    //2 - Data Processing : return a set of the persons fouded by their firestation
    when(personService.findByFirestationStationIn(stations)).thenReturn(set);

    Map<String, Set<PersonFirestationDTO>> personsByaddressDTO = personService
        .findPersonsByFirestationStationIn(stations);

    //3 - Test : Verify that the result is null
    assertEquals(null, personsByaddressDTO);
  }

  /**
   * Test that the the method find a person by its firstname and lastname
   */
  @Test
  public void findByFirstNameAndLastNameTest() {
    //1 - Data Creation :
    Person person = DataTest.getPerson1();

    //2 - Data Processing : Find a person by its firstname and lastname
    when(personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
        .thenReturn(person);

    Person personFromDb =
        personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

    //3 - Test : Test that the infos of the person are returned
    assertEquals(person.getFirstName(), personFromDb.getFirstName());
    assertEquals(person.getLastName(), personFromDb.getLastName());
    assertEquals(person.getAddress(), personFromDb.getAddress());
    assertEquals(person.getCity(), personFromDb.getCity());
    assertEquals(person.getZip(), personFromDb.getZip());
    assertEquals(person.getPhone(), personFromDb.getPhone());
    assertEquals(person.getEmail(), personFromDb.getEmail());
  }
}
