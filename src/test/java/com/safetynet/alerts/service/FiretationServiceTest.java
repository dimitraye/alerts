package com.safetynet.alerts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.safetynet.alerts.DataTest;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This class test the methods of the class IFirestationService
 */
public class FiretationServiceTest {

  @InjectMocks
  FirestationServiceImpl firestationService;

  @InjectMocks
  MedicalRecordServiceImpl medicalRecordService;

  @Mock
  FirestationRepository firestationRepository;


  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Test that all the firestations are returned
   */
  @Test
  public void getAllFirestationTest() {
    //1 - Creation Data :
    List<Firestation> list = new ArrayList<>();
    Firestation firestation1 = DataTest.getFirestation1();
    Firestation firestation2 = DataTest.getFirestation2();
    Firestation firestation3 = DataTest.getFirestation3();

    list.add(firestation1);
    list.add(firestation2);
    list.add(firestation3);

    //2 - Data processing : Return a list that contains all the firestations
    when(firestationRepository.findAll()).thenReturn(list);

    //3 - Test : Verify that all the firestations are returned as expected
    List<Firestation> firestations = firestationService.getAllFirestations();

    assertEquals(3, firestations.size());
    verify(firestationRepository, times(1)).findAll();
  }


  /**
   * Verify that the correct firestation is return when we enter its address
   */
  @Test
  public void findByAddressTest()
  {
    //1 - Data Processing : Return the firestation that have the same address as the one in the method
    // findFirestationByAddress.
    when(firestationRepository.findFirestationByAddress("Adresse"))
        .thenReturn(DataTest.getFirestation1());

    Firestation firestation = firestationService.findByAddress("Adresse");

    //2 - Test : Test that the correct firestation is returned
    assertEquals("15 rue des champs", firestation.getAddress());
    assertEquals(5, firestation.getStation());
  }


  /**
   * Verify that the firestation has been created
   * @throws ParseException
   */
  @Test
  public void addMappingFirestationTest() throws ParseException {
    //1 - Data Creation :
    Firestation firestation = DataTest.getFirestation1();

    //2 - Data Processing : Save the firestation
    firestationService.addMappingFirestation(firestation);

    //3 - Test : Verify that the firestation has been created
    verify(firestationRepository, times(1)).save(firestation);
  }


  /**
   * Verify that the firestation has been updated
   */
  @Test
  public void updateFirestationTest() {
    //1 - Data Creation :
    Firestation firestation = DataTest.getFirestation1();

    //2 - Data Processing : Update the firestation
    firestationService.updateNumberFirestation(firestation);

    //3 - Test : Verify that the firestation has been updated
    verify(firestationRepository, times(1)).save(firestation);
  }


  /**
   * Verify that the firestation has been deleted
   */
  @Test
  public void deleteFirestationTest() {
    //1 - Data Creation :
    Firestation firestation = DataTest.getFirestation1();

    //2 - Data Processing : Delete the firestation
    firestationService.deleteMappingFirestation(firestation);

    //3 - Test : Verify that the station has been deleted
    verify(firestationRepository, times(1)).delete(firestation);
  }
}