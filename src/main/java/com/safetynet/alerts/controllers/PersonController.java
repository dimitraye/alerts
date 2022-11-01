package com.safetynet.alerts.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.safetynet.alerts.dto.FamilyDTO;
import com.safetynet.alerts.dto.FamilyMemberDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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

	@GetMapping("/personInfo")
	public PersonInfoDTO personInfo(@RequestParam String firstName, @RequestParam String lastName){
		//voir si la personne existe (nom, prénom)
		Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);

		//S'il n'éxiste pas, envooie exception
		if(personFromDB == null) {
			throw new IllegalArgumentException("Erreur : Nom ét pénom non éxistants dans la base.");
		}

		//Sinon, retourner personInfoDTO
		return new PersonInfoDTO(personFromDB);
	}

	@GetMapping("/childAlert")
	public FamilyDTO getFamilyFromAddress(@RequestParam String address){
		//1 - Récupérer la liste des personnes ayant la même adresse
		Set<Person> familyList = personService.findAllByAddress(address);
		//Si personne ne correspond à l'adresse, renvoyer une liste de children et adults vide
		if (CollectionUtils.isEmpty(familyList)){
			return new FamilyDTO();
		}

		//2 - Peupler 2 listes , une liste d'enfants et une d'adultes
		Set<Person> children = new HashSet<>();
		Set<Person> adults = new HashSet<>();
		// Ajouter toutes les personnes de 18 ans ou moins dans la liste children
		children = familyList.stream().filter(person -> {
			int age = 0;
			if (person.getMedicalRecord() == null || person.getMedicalRecord().getBirthdate() == null) {
				return false;
			}
			Date birthDate = person.getMedicalRecord().getBirthdate();
			age = DateUtils.calculateAge(birthDate, new Date());

			return age <= 18;
		}).collect(Collectors.toSet());

		//S'il n'y a pas d'enfants, renvoyer une liste de children et adults vide
		if (CollectionUtils.isEmpty(children)){
			return new FamilyDTO();
		}

		//Ajout de toutes les personnes dans la liste adults
		adults.addAll(familyList);
		//suppression de ous les enfants de la liste adults
		adults.removeAll(children);

		//Ajouts des personnes dans le set correspondant
		Set<FamilyMemberDTO> childrenDTO = children.stream().map(person -> new FamilyMemberDTO(person)).collect(Collectors.toSet());
		Set<FamilyMemberDTO> adultsDTO = adults.stream().map(person -> new FamilyMemberDTO(person)).collect(Collectors.toSet());

		//création de l'objet familyDTO qui contient 2 sets, childrenDTO et adultsDTO
		FamilyDTO familyDTO = new FamilyDTO();

		// Initialisation des listes
		familyDTO.setChildren(childrenDTO);
		familyDTO.setAdults(adultsDTO);

		//4 - retourner familyDTO
		return familyDTO;
	}


	//List of phone numbers
	@GetMapping("/phoneAlert")
	public Set<PersonRepository.Phone> findPhoneByFirestationStation(@RequestParam int station) {
		return personService.findPhoneByFirestationStation(station);
	}


}
