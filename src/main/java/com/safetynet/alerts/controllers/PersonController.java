package com.safetynet.alerts.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	public Person updatePerson(@RequestParam String firstName, @RequestParam String lastName, @RequestBody Person person) {

		//Cherche person par nom prenom
		Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);

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

	//Supprimer une persone
	@DeleteMapping("/person")
	public void deletePerson(@RequestParam String firstName, @RequestParam String lastName){
		//voir si la personne existe (nom, prénom)
		Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);

		//S'il n'éxiste pas, envooie exception
		if(personFromDB == null) {
			throw new IllegalArgumentException("Erreur : Nom ét pénom non éxistants dans la base.");
		}
		//Sinon, supprimer la personne
		personService.deletePerson(personFromDB);
	}



	//List of emails
	@GetMapping("/communityEmail")
	public Set<String> findEmailsByCity(@RequestParam String city) {
		return personService.findEmailsByCity(city);
	}

}
