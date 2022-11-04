package com.safetynet.alerts.controllers;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.IFirestationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Manage the requests linked to a firestation
 */
@Slf4j
@RestController
public class FirestationController {


    @Autowired
    IFirestationService firestationService;

    /**
     * Get all the freStations
     * @return firestation List
     */
    @GetMapping("/firestations")
    public List<Firestation> getAllFirestations() {
        return firestationService.getAllFirestations();
    }

    /**
     * Save a new firestation
     * @param firestation to save
     * @return saved firestation
     */
    @PostMapping("/firestation")
    public ResponseEntity<Firestation> addMappingFirestation(@RequestBody Firestation firestation) {
        //1 - Verify if the firestation exist
        Firestation firestationFromDB = firestationService.findByAddress(firestation.getAddress());

        //2 - If person exist, throw exception
        if(firestationFromDB != null) {
            log.error("Error : Firestation already exists in the Data Base");
            //throw new IllegalArgumentException("Erreur : Firestation déjà éxistante dans la base.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Saving the new firestation");
        //sinon créer firestation
        //return firestationService.addMappingFirestation(firestation);
        return  new ResponseEntity<>(firestationService.addMappingFirestation(firestation), HttpStatus.CREATED);

    }


    /**
     * Update a firestation number.
     * @param address of the firestation
     * @param firestation to update
     * @return updated firestation
     */
    @PutMapping("/firestation")
    public ResponseEntity<Firestation> updateFirestation(@RequestParam String address, @RequestBody Firestation firestation) {

        //1 - Verify if the firestation exist
        Firestation firestattionFromDB = firestationService.findByAddress(address);

        //2 - If firestation doesn't exist, throw exception
        if(firestattionFromDB == null) {
            log.error("Error : Firestation does not exist");
            //throw new IllegalArgumentException("Erreur : Firestation non éxistante dans la base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //sinon mettre à jour station de Firestation
        firestattionFromDB.setStation(firestation.getStation());

        log.info("Returning updated Firestation");
        //return firestationService.updateNumberFirestation(firestattionFromDB);
        return new ResponseEntity<>(firestationService.updateNumberFirestation(firestattionFromDB), HttpStatus.OK);
    }


    /**
     * Delete a firestation
     * @param address to delete
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<HttpStatus> deleteMappingFirestation(@RequestParam String address){
        //1 - Verify if the firestation exist
        Firestation firestationFromDB = firestationService.findByAddress(address);

        //2 - If firestation doesn't exist, throw exception
        if(firestationFromDB == null) {
            log.error("Error : Firestation doesn't exist in Data Base");
            //throw new IllegalArgumentException("Erreur : Firestation non éxistante dans la base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            firestationService.deleteMappingFirestation(firestationFromDB);
            log.info("The firestation has been deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
