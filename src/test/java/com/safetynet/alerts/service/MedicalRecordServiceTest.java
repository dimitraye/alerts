package com.safetynet.alerts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.safetynet.alerts.DataTest;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This class test the methods of the class IMedicalRecordService
 */
public class MedicalRecordServiceTest {

  @InjectMocks
  MedicalRecordServiceImpl medicalRecordService;

  @Mock
  MedicalRecordRepository medicalRecordRepository;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Test that a list with all the medical records is returned correctly
   * @throws ParseException
   */
  @Test
  public void getAllMedicalRecordTest() throws ParseException {
    //1 - Data Creation :
    List<MedicalRecord> list = new ArrayList<>();
    MedicalRecord medicalRecord1 = DataTest.getMedicalRecord1();
    MedicalRecord medicalRecord2 = DataTest.getMedicalRecord2();
    MedicalRecord medicalRecord3 = DataTest.getChildMedicalRecord();

    list.add(medicalRecord1);
    list.add(medicalRecord2);
    list.add(medicalRecord3);

    //2 - Data Processing : return a list that contains all the medical records
    when(medicalRecordRepository.findAll()).thenReturn(list);
    List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();

    //3 - Test : Verify that a list with all the medical records in it is returned
    assertEquals(3, medicalRecords.size());
    verify(medicalRecordRepository, times(1)).findAll();
  }

  /**
   * Test that a medical record is created correctly
   * @throws ParseException
   */
  @Test
  public void addMedicalRecordTest() throws ParseException {
    //1 - Data Creation :
    MedicalRecord medicalRecord = DataTest.getMedicalRecord1();

    //2 - Data Processing : Save the medical record
    medicalRecordService.addMedicalRecord(medicalRecord);

    //3 - Test : Verify that the medical record has been saved correctly
    verify(medicalRecordRepository, times(1)).save(medicalRecord);
  }


  /**
   * Test that a medical record is updated correctly
   * @throws ParseException
   */
  @Test
  public void updateMedicalRecordTest() throws ParseException {
    //1 - Data Creation :
    MedicalRecord medicalRecord = DataTest.getMedicalRecord1();

    //2 - Data Processing : Update the medical record
    medicalRecordService.updateMedicalRecord(medicalRecord);

    //3 - Test : Verify that the medical record has been updated
    verify(medicalRecordRepository, times(1)).save(medicalRecord);
  }
}