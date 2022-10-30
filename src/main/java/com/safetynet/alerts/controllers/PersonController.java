package com.safetynet.alerts.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IPersonService;

@RestController
public class PersonController {
	@Autowired
	IPersonService personService;
	
	@GetMapping("/persons")
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}
	
	@PostMapping("/person")
	public Person addPerson(@RequestBody Person person) {
		//Cherche person par nom prenom
		Person personFromDB = personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

		//si existe, envoie exception
		if(personFromDB != null) {
			throw new IllegalArgumentException("Erreur : Nom ét pénom déjà éxistants dans la base.");
		}
		//sinon creer person
		return personService.addPerson(person);
	}
	
	@PutMapping("/person")
	public Person updatePerson(@RequestParam String firstName, @RequestParam String lastName, Person person) {

		//Cherche person par nom prenom
		Person personFromDB = personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

		//si n'existe pas, envoie exception
		if(personFromDB == null) {
			throw new IllegalArgumentException("Erreur : Nom ét pénom non éxistants dans la base.");
		}
		//sinon mettre à jour person
		personFromDB.setAddress(person.getAddress());
		personFromDB.setCity(person.getCity());
		personFromDB.setZip(person.getZip());
		personFromDB.setPhone(person.getPhone());
		personFromDB.setEmail(person.getEmail());


		return personService.updatePerson(personFromDB);
	
	}

}
