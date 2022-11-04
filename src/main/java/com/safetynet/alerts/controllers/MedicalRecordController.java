package com.safetynet.alerts.controllers;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IMedicalRecordService;
import com.safetynet.alerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Manage the requests linked to a medical record
 */
@Slf4j
@RestController
public class MedicalRecordController {
    @Autowired
    IPersonService personService;
    @Autowired
    IMedicalRecordService medicalRecordService;

    /**
     * Get all the medical Records in the Data Base.
     * @return List of Medical Record.
     */
    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords() {return medicalRecordService.getAllMedicalRecords();}

    /**
     * Save a new medical record
     * @param medicalRecord to save
     * @return saved medical record
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        log.debug("Create a person ");
        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        //2 - If person doesn't exist, throw exception
        if (personFromDB == null){
            log.error("Error : The person doesn't exist in the Data Base.");
            //throw new IllegalArgumentException("Erreur : La personne n'éxiste pas dans la base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //3 - verify if the medicalRecord of this person exists
        if (personFromDB.getMedicalRecord() != null) {
            log.error("Error : This person is already saved in the Data Base.");
            //throw new IllegalArgumentException("Erreur : Cette personne à déjà un dossier médical.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("Create a medical record.");
        //If not create MedicalRecord
        MedicalRecord medicalRecordFromDB = medicalRecordService.addMedicalRecord(medicalRecord);

        //If medicalRecordFromDB == null , throws error : problem while saving medicalRecord
        if (medicalRecordFromDB == null) {
            log.error("Error : The medical record couldn't be saved");
            //throw new IllegalArgumentException("Erreur : Le dossier Médical n'a pas pu être sauvegardé.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.debug("Set the property medical record in the object personFromDB");
        personFromDB.setMedicalRecord(medicalRecordFromDB);
        log.debug("Create a personUpdated that get the updated version of the object personFromDB");
        Person personUpdated = personService.updatePerson(personFromDB);
        // TODO : Si le personUpdated n'a pas un médicalrecord + throw une exception
        if (personUpdated == null || personUpdated.getMedicalRecord() == null) {
            log.error("Error : The medical record couldn't be saved");
            //throw new IllegalArgumentException("Erreur : Le dossier Médical n'a pas pu être sauvegardé.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        log.info("Saving the new medical record");
        return new ResponseEntity<>(medicalRecordFromDB, HttpStatus.OK);
    }


    /**
     * Update a medical record
     * @param medicalRecord to update
     * @return updated medical record
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {

        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        //2 - If person doesn't exist, throw exception
        if (personFromDB == null){
            log.error("Error : This person doesn't exist in the Data Base.");
            //throw new IllegalArgumentException("Erreur : La personne n'éxiste pas dans la base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //3 - verify if the medicalRecord of this person exists
        MedicalRecord medicalRecordFromDB = personFromDB.getMedicalRecord() ;
        if (medicalRecordFromDB == null) {
            log.error("Error : This person doesn't have a medical record");
            //throw new IllegalArgumentException("Erreur : Cette personne n'a pas de dossier médical.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.debug("Update the medicalRcordFromDB by setting the birthdate, medication and allergies from the object medicalRecord. ");
        //sinon mettre à jour MedicalRecord
        medicalRecordFromDB.setBirthdate(medicalRecord.getBirthdate());
        medicalRecordFromDB.setMedications(medicalRecord.getMedications());
        medicalRecordFromDB.setAllergies(medicalRecord.getAllergies());

        log.info("Updating person's medical record");
        return new ResponseEntity<>(medicalRecordService.updateMedicalRecord(medicalRecordFromDB), HttpStatus.OK);
    }


    /**
     * Delete a medical record
     * @param firstName search param
     * @param lastName search param
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<HttpStatus> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName){

        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);
        //2 - If person doesn't exist, throw exception
        if (personFromDB == null){
            log.error("Error : This person doesn't exist in the Data Base");
            //throw new IllegalArgumentException("Erreur : La personne n'existe pas dans la base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //3 - verify if the medicalRecord of this person exists
        MedicalRecord medicalRecordFromDB = personFromDB.getMedicalRecord() ;
        if (medicalRecordFromDB == null) {
            log.error("Error : This person doesn't have a medical record.");
            //throw new IllegalArgumentException("Erreur : Cette personne n'a pas de dossier médical.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Sinon,supprimer le dossier médical et met à jour la personne(supprime le dossier de la personne)

        personFromDB.setMedicalRecord(null);


        log.info("Person's medical record deleted");
        try {
            personService.updatePerson(personFromDB);
            log.info("The firestation has been deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
