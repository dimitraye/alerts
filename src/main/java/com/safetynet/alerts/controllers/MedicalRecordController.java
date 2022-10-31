package com.safetynet.alerts.controllers;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IMedicalRecordService;
import com.safetynet.alerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class MedicalRecordController {
    @Autowired
    IPersonService personService;
    @Autowired
    IMedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords() {return medicalRecordService.getAllMedicalRecords();}

    @Transactional
    @PostMapping("/medicalRecord")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        //2 - If person doesn't exist, throw exception
        if (personFromDB == null){
            throw new IllegalArgumentException("Erreur : La personne n'éxiste pas dans la base.");
        }
        //3 - verify if the medicalRecord of this person exists
        if (personFromDB.getMedicalRecord() != null) {
            throw new IllegalArgumentException("Erreur : Cette personne à déjà un dossier médical.");
        }
        //sinon creer MedicalRecord
        MedicalRecord medicalRecordFromDB = medicalRecordService.addMedicalRecord(medicalRecord);

        //If medicalRecordFromDB == null , throws error : problem while saving medicalRecord
        if (medicalRecordFromDB == null) {
            throw new IllegalArgumentException("Erreur : Le dossier Médical n'a pas pu être sauvegardé.");
        }

        personFromDB.setMedicalRecord(medicalRecordFromDB);
        Person personUpdated = personService.updatePerson(personFromDB);
        // TODO : Si le personUpdated n'a pas un médicalrecord + throw une exception
        if (personUpdated == null || personUpdated.getMedicalRecord() == null) {
            throw new IllegalArgumentException("Erreur : Le dossier Médical n'a pas pu être sauvegardé.");
        }
        return  medicalRecordFromDB;
    }


    @PutMapping("/medicalRecord")
    public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {

        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        //2 - If person doesn't exist, throw exception
        if (personFromDB == null){
            throw new IllegalArgumentException("Erreur : La personne n'éxiste pas dans la base.");
        }
        //3 - verify if the medicalRecord of this person exists
        MedicalRecord medicalRecordFromDB = personFromDB.getMedicalRecord() ;
        if (medicalRecordFromDB == null) {
            throw new IllegalArgumentException("Erreur : Cette personne n'a pas de dossier médical.");
        }

        //sinon mettre à jour MedicalRecord
        medicalRecordFromDB.setBirthdate(medicalRecord.getBirthdate());
        medicalRecordFromDB.setMedications(medicalRecord.getMedications());
        medicalRecordFromDB.setAllergies(medicalRecord.getAllergies());

        return medicalRecordService.updateMedicalRecord(medicalRecordFromDB);
    }


    //Supprimer un Dossier Médical
    @DeleteMapping("/medicalRecord")
    public void deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName){

        //1 - Verify if the person exist
        Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);
        //2 - If person doesn't exist, throw exception
        if (personFromDB == null){
            throw new IllegalArgumentException("Erreur : La personne n'existe pas dans la base.");
        }
        //3 - verify if the medicalRecord of this person exists
        MedicalRecord medicalRecordFromDB = personFromDB.getMedicalRecord() ;
        if (medicalRecordFromDB == null) {
            throw new IllegalArgumentException("Erreur : Cette personne n'a pas de dossier médical.");
        }

        //Sinon,supprimer le dossier médical et met à jour la personne(supprime le dossier de la personne)

        personFromDB.setMedicalRecord(null);
        personFromDB = personService.updatePerson(personFromDB);


    }


}
