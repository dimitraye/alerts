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

  @Test
  public void getAllFirestationTest() {
    List<Firestation> list = new ArrayList<>();
    Firestation firestation1 = DataTest.getFirestation1();
    Firestation firestation2 = DataTest.getFirestation2();
    Firestation firestation3 = DataTest.getFirestation3();

    list.add(firestation1);
    list.add(firestation2);
    list.add(firestation3);

    when(firestationRepository.findAll()).thenReturn(list);

    //test
    List<Firestation> firestations = firestationService.getAllFirestations();

    assertEquals(3, firestations.size());
    verify(firestationRepository, times(1)).findAll();
  }


  @Test
  public void findByAddressTest()
  {
    when(firestationRepository.findFirestationByAddress("Adresse"))
        .thenReturn(DataTest.getFirestation1());

    Firestation firestation = firestationService.findByAddress("Adresse");

    assertEquals("15 rue des champs", firestation.getAddress());
    assertEquals(5, firestation.getStation());
  }


  @Test
  public void addMappingFirestationTest() throws ParseException {
    Firestation firestation = DataTest.getFirestation1();

    firestationService.addMappingFirestation(firestation);

    verify(firestationRepository, times(1)).save(firestation);
  }


  @Test
  public void updateFirestationTest() {
    Firestation firestation = DataTest.getFirestation1();

    firestationService.updateNumberFirestation(firestation);

    verify(firestationRepository, times(1)).save(firestation);
  }


  @Test
  public void deleteFirestationTest() {
    Firestation firestation = DataTest.getFirestation1();

    firestationService.deleteMappingFirestation(firestation);

    verify(firestationRepository, times(1)).delete(firestation);
  }
}