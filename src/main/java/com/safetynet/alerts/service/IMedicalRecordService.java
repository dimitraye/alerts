package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Interface that manage the interaction with the medical record entity
 */
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

    /**
     * Update a medicalRecord
     * @param medicalRecord
     * @return the medicalRecord that has been modified
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);
}
