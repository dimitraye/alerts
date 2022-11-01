package com.safetynet.alerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alerts.model.Firestation;

/**
 * Manage database operations for a firestation entity
 */
@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long> {
    /**
     * Find a firestation by its address
     * @param address search param
     * @return firestation by its address
     */
    public Firestation findFirestationByAddress(String address);
}
