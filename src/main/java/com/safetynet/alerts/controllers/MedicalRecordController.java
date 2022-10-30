package com.safetynet.alerts.controllers;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IMedicalRecordService;
import com.safetynet.alerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicalRecordController {
    @Autowired
    IPersonService personService;
    @Autowired
    IMedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords() {return medicalRecordService.getAllMedicalRecords();}

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
            throw new IllegalArgumentException("Erreur : Dossier Médical déjà présent dans la base.");
        }
    // TODO : mettre ces deux méthodes de sauvegarde dans une transaction
        //sinon creer MedicalRecord
        MedicalRecord medicalRecordFromDB = medicalRecordService.addMedicalRecord(medicalRecord);
        //If medicalRecord == null , throws error : problem while saving medicalRecord

        personFromDB.setMedicalRecord(medicalRecordFromDB);
        personService.updatePerson(personFromDB);
        //throw xception error :
        return  medicalRecordFromDB;
    }


}
