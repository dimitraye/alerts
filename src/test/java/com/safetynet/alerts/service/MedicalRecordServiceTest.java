package com.safetynet.alerts.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.safetynet.alerts.DataTest;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;

import java.net.DatagramSocket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MedicalRecordServiceTest {

  @InjectMocks
  MedicalRecordServiceImpl medicalRecordService;

  @Mock
  MedicalRecordRepository medicalRecordRepository;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getAllMedicalRecordTest() throws ParseException {
    List<MedicalRecord> list = new ArrayList<>();
    MedicalRecord medicalRecord1 = DataTest.getMedicalRecord1();
    MedicalRecord medicalRecord2 = DataTest.getMedicalRecord2();
    MedicalRecord medicalRecord3 = DataTest.getChildMedicalRecord();

    list.add(medicalRecord1);
    list.add(medicalRecord2);
    list.add(medicalRecord3);

    when(medicalRecordRepository.findAll()).thenReturn(list);

    //test
    List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();

    assertEquals(3, medicalRecords.size());
    verify(medicalRecordRepository, times(1)).findAll();
  }

  @Test
  public void addMedicalRecordTest() throws ParseException {
    MedicalRecord medicalRecord = DataTest.getMedicalRecord1();

    medicalRecordService.addMedicalRecord(medicalRecord);

    verify(medicalRecordRepository, times(1)).save(medicalRecord);
  }


  @Test
  public void updateMedicalRecordTest() throws ParseException {
    MedicalRecord medicalRecord = DataTest.getMedicalRecord1();

    medicalRecordService.updateMedicalRecord(medicalRecord);

    verify(medicalRecordRepository, times(1)).save(medicalRecord);
  }
}