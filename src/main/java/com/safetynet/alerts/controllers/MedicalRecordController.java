package com.safetynet.alerts.controllers;

import com.safetynet.alerts.config.ExcludeFromJacocoGeneratedReport;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IMedicalRecordService;
import com.safetynet.alerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    @ExcludeFromJacocoGeneratedReport
    public List<MedicalRecord> getAllMedicalRecords() {return medicalRecordService.getAllMedicalRecords();}

    /**
     * Save a new medical record
     * @param medicalRecord to save
     * @return saved medical record
     */
    @Transactional
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        //2 - If person doesn't exist, RETURN NOT FOUND
        if (personFromDB == null){
            log.error("Error : The person doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //3 - verify if the medicalRecord of this person exists
        if (personFromDB.getMedicalRecord() != null) {
            log.error("Error : This person already have a medical record.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("Create a medical record.");
        MedicalRecord medicalRecordFromDB = medicalRecordService.addMedicalRecord(medicalRecord);

        //If medicalRecordFromDB == null , return internal service error
        if (medicalRecordFromDB == null) {
            log.error("Error : The medical record couldn't be saved");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.debug("Set the property medical record in the object personFromDB");
        personFromDB.setMedicalRecord(medicalRecordFromDB);
        log.debug("Update the person and his medical record");
        Person personUpdated = personService.updatePerson(personFromDB);
        if (personUpdated == null || personUpdated.getMedicalRecord() == null) {
            log.error("Error : The medical record couldn't be saved");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicalRecordFromDB, HttpStatus.CREATED);
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //3 - verify if the medicalRecord of this person exists
        MedicalRecord medicalRecordFromDB = personFromDB.getMedicalRecord() ;
        if (medicalRecordFromDB == null) {
            log.error("Error : This person doesn't have a medical record");
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //3 - verify if the medicalRecord of this person exists
        MedicalRecord medicalRecordFromDB = personFromDB.getMedicalRecord() ;
        if (medicalRecordFromDB == null) {
            log.error("Error : This person doesn't have a medical record.");
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
