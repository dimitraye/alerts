package com.safetynet.alerts.controllers;

import com.safetynet.alerts.config.ExcludeFromJacocoGeneratedReport;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IPersonService;

/**
 * Manage the requests associated to persons
 */
@Slf4j
@RestController
public class PersonController {
	@Autowired
	IPersonService personService;

	/**
	 * Get all persons
	 * @return a list of persons
	 */
	@GetMapping("/persons")
	@ExcludeFromJacocoGeneratedReport
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}


	/**
	 * Save a person.
	 * @param person to save
	 * @return saved person
	 */
	@PostMapping("/person")
	public ResponseEntity<Person>  addPerson(@RequestBody Person person) {
		Person personFromDB = personService.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

		//si existe, envoie exception
		if(personFromDB != null) {
			log.error("Error : FirstName and LastName already exist in the Data Base.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			//throw new IllegalArgumentException("Erreur : Nom ét pénom déjà éxistants dans la base.");
		}
		//sinon creer person
		log.info("Saving a new person");
		return new ResponseEntity<>(personService.addPerson(person), HttpStatus.CREATED);
	}


	/**
	 * Update the informations of a person
	 * @param firstName search param to find the person to update
	 * @param lastName search param to find the person to update
	 * @param person the person to update
	 * @return updated person
	 */
	@PutMapping("/person")
	public ResponseEntity<Person> updatePerson(@RequestParam String firstName, @RequestParam String lastName, @RequestBody Person person) {

		//Cherche person par nom prenom
		Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);

		//si n'existe pas, envoie exception
		if(personFromDB == null) {
			log.error("Error : FirstName and LastName already doesn't exist in the Data Base.");
			//throw new IllegalArgumentException("Erreur : Nom ét pénom non éxistants dans la base.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		log.debug("Set properties in the object personFromDB.");
		//sinon mettre à jour person
		personFromDB.setAddress(person.getAddress());
		personFromDB.setCity(person.getCity());
		personFromDB.setZip(person.getZip());
		personFromDB.setPhone(person.getPhone());
		personFromDB.setEmail(person.getEmail());

		log.info("Updating a person");
		return new ResponseEntity<>(personService.updatePerson(personFromDB), HttpStatus.OK);
	}

	/**
	 * Delete a person
	 * @param firstName search param to find the person to delete
	 * @param lastName search param to find the person to delete
	 */
	@DeleteMapping("/person")
	public ResponseEntity<HttpStatus> deletePerson(@RequestParam String firstName, @RequestParam String lastName){
		//voir si la personne existe (nom, prénom)
		Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);

		//S'il n'éxiste pas, envooie exception
		if(personFromDB == null) {
			log.error("Error : FirstName and LastName doesn't exist in the Data Base.");
			//throw new IllegalArgumentException("Erreur : Nom ét pénom non éxistants dans la base.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		try {
			personService.deletePerson(personFromDB);
			log.info("Person deleted");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Find all the emeails of the persons linked to a city
	 * @param city search param
	 * @return list of emails
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<Set<String>>  findEmailsByCity(@RequestParam String city) {
		//return personService.findEmailsByCity(city);
		return new ResponseEntity<>(personService.findEmailsByCity(city), HttpStatus.OK);
	}

	/**
	 * Display the informations of a prrson
	 * @param firstName search param
	 * @param lastName search param
	 * @return informations of a prrson
	 */
	@GetMapping("/personInfo")
	public ResponseEntity<PersonInfoDTO> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName){
		//voir si la personne existe (nom, prénom)
		Person personFromDB = personService.findByFirstNameAndLastName(firstName, lastName);

		//S'il n'éxiste pas, envooie exception
		if(personFromDB == null) {
			log.error("Error : FirstName and LastName doesn't exist in the Data Base.");
			//throw new IllegalArgumentException("Erreur : Nom ét pénom non éxistants dans la base.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		log.info("Returning the person's informations");
		//Sinon, retourner personInfoDTO
		//return new PersonInfoDTO(personFromDB);
		return new ResponseEntity<>(new PersonInfoDTO(personFromDB), HttpStatus.OK);
	}


	/**
	 * List of children and the other family members linked to an address
	 * Display the list if there's at least a child in this address
	 * @param address search param
	 * @return list of children and family members for this address
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<FamilyDTO> getFamilyFromAddress(@RequestParam String address){
		//1 - Récupérer la liste des personnes ayant la même adresse
		FamilyDTO familyDTO = personService.getFamilyFromAddress(address);
		//Si personne ne correspond à l'adresse, renvoyer une liste de children et adults vide
		if (familyDTO == null){
			return new ResponseEntity<>(new FamilyDTO(), HttpStatus.OK);
		}

		log.info("Returning the whole family");
		return new ResponseEntity<>(familyDTO, HttpStatus.OK);
	}


	/**
	 * Display the list of phone numbers of the people associated to this address
	 * @param firestation search param
	 * @return the list of phone numbers of the people associated to this address
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<Set<String>> findPhoneByFirestationStation(@RequestParam int firestation) {
		log.info("Returning list of phones associated to the station.");
		return new ResponseEntity<>(personService.findPhoneByFirestationStation(firestation), HttpStatus.OK);
	}


	/**
	 * Display a list of people that have the same address
	 * @param address search param
	 * @return list of people that have the same address
	 */
	@GetMapping("/fire")
	public ResponseEntity<Set<PersonAddressDTO>> getPersonsByAddress(@RequestParam String address){
		//1 - Récupérer la liste des personnes ayant la même adresse
		Set<Person> persons = personService.findAllByAddress(address);

		//Si personne ne correspond à l'adresse, renvoyer une liste vide
		if (CollectionUtils.isEmpty(persons)){
			//return new HashSet<>();
			return new ResponseEntity<>(new HashSet<>(), HttpStatus.OK);
		}

		//2 - Peupler la liste
		Set<PersonAddressDTO> personsListDTO = persons.stream().map(person -> new PersonAddressDTO(person))
				.collect(Collectors.toSet());

		log.info("Returning the list of people that have the same address");
		//4 - retourner personsListDTO
		//return personsListDTO;
		return  new ResponseEntity<>(personsListDTO, HttpStatus.OK);
	}

	/**
	 * Display a list of people associated to a list of firestations
	 * @param stations search param
	 * @return list of people associated to a list of firestations
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<Map<String, Set<PersonFirestationDTO>>> findByFirestationStationIn(@RequestParam List<Integer> stations){
		//1 - Fetch persons associated to those stations
		Map<String, Set<PersonFirestationDTO>> personsByStations = personService.findPersonsByFirestationStationIn(stations);

		//2 - If there's no one associated to those stations, send an empty map
		if(CollectionUtils.isEmpty(personsByStations)) {
			return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
		}

		log.info("Returning a list of people linked to the list of  firestations");
		return new ResponseEntity<>(personsByStations, HttpStatus.OK);
	}


	/**
	 * Display a list of people associated to a firestation
	 * @param stationNumber search param
	 * @return list of people associated to a firestation
	 */
	@GetMapping("/firestation")
	public ResponseEntity<ContainerPersonDTO> findByFirestationStation(@RequestParam int stationNumber){
		//1 - Fetch persons associated to this station
		ContainerPersonDTO personsByStation = personService.findByFirestationStation(stationNumber);

		//2 - If there's no one associated to this station, send an empty map
		if(personsByStation == null) {
			return new ResponseEntity<>(new ContainerPersonDTO(), HttpStatus.OK);
		}

		log.info("Returning a list of people linked to the firestation");
		return new ResponseEntity<>(personsByStation, HttpStatus.OK);
	}
}
