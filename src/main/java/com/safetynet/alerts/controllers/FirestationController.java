package com.safetynet.alerts.controllers;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IFirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirestationController {


    @Autowired
    IFirestationService firestationService;
    @GetMapping("/firestations")
    public List<Firestation> getAllFirestations() {
        return firestationService.getAllFirestations();
    }

    @PostMapping("/firestation")
    public Firestation addMappingFirestation(@RequestBody Firestation firestation) {
        //1 - Verify if the firestation exist
        Firestation firestationFromDB = firestationService.findByAddress(firestation.getAddress());

        //2 - If person exist, throw exception
        if(firestationFromDB != null) {
            throw new IllegalArgumentException("Erreur : Firestation déjà éxistante dans la base.");
        }

        //sinon créer firestation
        return firestationService.addMappingFirestation(firestation);
    }


    @PutMapping("/firestation")
    public Firestation updateFirestation(@RequestParam String address, @RequestBody Firestation firestation) {

        //1 - Verify if the firestation exist
        Firestation firestattionFromDB = firestationService.findByAddress(address);

        //2 - If firestation doesn't exist, throw exception
        if(firestattionFromDB == null) {
            throw new IllegalArgumentException("Erreur : Firestation non éxistante dans la base.");
        }
        //sinon mettre à jour station de Firestation
        firestattionFromDB.setStation(firestation.getStation());

        return firestationService.updateNumberFirestation(firestattionFromDB);
    }


    //Supprimer une firestation
    @DeleteMapping("/firestation")
    public void deleteMappingFirestation(@RequestParam String address){
        //1 - Verify if the firestation exist
        Firestation firestationFromDB = firestationService.findByAddress(address);

        //2 - If firestation doesn't exist, throw exception
        if(firestationFromDB == null) {
            throw new IllegalArgumentException("Erreur : Firestation non éxistante dans la base.");
        }
        //Sinon, supprimer la firestation
        firestationService.deleteMappingFirestation(firestationFromDB);
    }
}
