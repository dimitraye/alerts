package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;

import java.util.List;

/**
 * Interface that manage the interaction with the firestation entity
 */
public interface IFirestationService {

    /**
     * Get a list of firestations
     * @return a list of firestation
     */
    public List<Firestation> getAllFirestations();

    /**
     * Find a station based on its address
     * @param address
     * @return
     */
    public Firestation findByAddress(String address);

    /**
     * Add a new firestation
     * @return the firestation that has been created
     */
    public Firestation addMappingFirestation(Firestation firestation);

    /**
     * Update a firestation
     * @return the firestation that has been modified
     */
    public Firestation updateNumberFirestation(Firestation firestation);

    /**
     * Delte a station
     */
    public void deleteMappingFirestation(Firestation firestation);
}
