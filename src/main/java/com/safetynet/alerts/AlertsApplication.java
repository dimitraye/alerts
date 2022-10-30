package com.safetynet.alerts;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.MedicalRecordForImport;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;

@SpringBootApplication
public class AlertsApplication {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
	
	@Autowired
	private FirestationRepository firestationRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(AlertsApplication.class, args);
		System.out.println("Hello World");
	}
	
	@Bean
	CommandLineRunner runner() {
		
		return args -> {
			List<Person> persons = null;
			// read json and write to db
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Person>> typeReferencePerson = new TypeReference<List<Person>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/person.json");
			try {
				persons = mapper.readValue(inputStream,typeReferencePerson);
				personRepository.saveAll(persons);
				System.out.println("Persons Saved!");
			} catch (IOException e){
				System.out.println("Unable to save persons: " + e.getMessage());
			}
			
			
			//Save MedicalRecords in DB
			TypeReference<List<MedicalRecordForImport>> typeReferenceMedicalRecord = new TypeReference<List<MedicalRecordForImport>>(){};
			inputStream = TypeReference.class.getResourceAsStream("/json/medicalRecord.json");
			try {
				List<Person> personList = persons;
				List<MedicalRecordForImport> medicalRecordsForImport = mapper.readValue(inputStream,typeReferenceMedicalRecord);
				List<MedicalRecord> medicalRecords = medicalRecordsForImport.stream()
						.map(m -> m.toMedicalRecord()).collect(Collectors.toList());
				medicalRecordRepository.saveAll(medicalRecords);
				System.out.println("MedicalRecords Saved!");
				medicalRecords.forEach(medicalRecord -> {
					personList.forEach(person -> {
						if( (medicalRecord.getFirstName().equals(person.getFirstName())) &&
								(medicalRecord.getLastName().equals(person.getLastName())) ) {
							 person.setMedicalRecord(medicalRecord);
						}
					});
				});
				personRepository.saveAll(personList);
				
				
			} catch (IOException e){
				System.out.println("Unable to save medicalRecords: " + e.getMessage());
			}
			
			
			//Save FireStation in DB
			//1 - Save firestation
			List<Firestation> firestations = null;
			List<Person> personList = persons;
			// read json and write to db
			TypeReference<List<Firestation>> typeReferenceFirestation= new TypeReference<List<Firestation>>(){};
			inputStream = TypeReference.class.getResourceAsStream("/json/firestation.json");
			try {
				firestations = mapper.readValue(inputStream,typeReferenceFirestation);
				firestationRepository.saveAll(firestations);
				System.out.println("Firestations Saved!");
				//2 - Link firestation with person
				firestations.forEach(firestation -> {
					personList.forEach(person -> {
						if(firestation.getAddress().equals(person.getAddress())) {
							 person.setFirestation(firestation); 
						}
					});
				});
				personRepository.saveAll(personList);
				
			} catch (IOException e){
				System.out.println("Unable to save firestations: " + e.getMessage());
			}
			
			

		};
	}

}
