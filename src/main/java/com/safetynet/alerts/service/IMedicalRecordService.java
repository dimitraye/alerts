package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IMedicalRecordService {

    /**
     * Get a list of MedicalRecords
     * @return a list of MedicalRecords
     */
    public List<MedicalRecord> getAllMedicalRecords();


    /**
     * Add a new MedicalRecord
     * @param medicalRecord
     * @return MedicalRecord
     */
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);

    public MedicalRecord findByPersonFirstNameAndLastName(String firstname, String lastname);

    /**
     * Update a medicalRecord
     * @param medicalRecord
     * @return the medicalRecord that has been modified
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

    /**
     * Delete a medicalRecord based of its firstname and lastname
     * @param medicalRecord
     */
    public void deleteMedicalRecord(MedicalRecord medicalRecord);

}
