package com.safetynet.alerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alerts.model.Firestation;

@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long> {
    public Firestation findFirestationByAddress(String address);
}
