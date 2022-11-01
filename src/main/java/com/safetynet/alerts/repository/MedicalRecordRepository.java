package com.safetynet.alerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alerts.model.MedicalRecord;

/**
 * Manage database operations for a medical record entity
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
