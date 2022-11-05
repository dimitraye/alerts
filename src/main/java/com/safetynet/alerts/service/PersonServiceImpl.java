package com.safetynet.alerts.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class PersonServiceImpl implements IPersonService {
	
	@Autowired
	PersonRepository personRepository;
	
	@Override
	public List<Person> getAllPersons() {
		return personRepository.findAll();
	}

	@Override
	public Person addPerson(Person person) {
		return personRepository.save(person);
	}

	@Override
	public Person updatePerson(Person person) {
		return addPerson(person);
	}

	@Override
	public void deletePerson(Person person) {
		personRepository.delete(person);
	}

	@Override
	public Set<String> findEmailsByCity(String city) {
		return personRepository.findEmailsByCity(city);
	}

	@Override
	public Set<Person> findAllByAddress(String address) {
		return personRepository.findAllByAddress(address);
	}

	@Override
	public Set<String> findPhoneByFirestationStation(int station) {
		return personRepository.findPhonesByFirestationStation(station);
	}

	@Override
	public Set<Person> findByFirestationStationIn(List<Integer> stations) {
		return personRepository.findByFirestationStationIn(stations);
	}

	@Override
	public FamilyDTO getFamilyFromAddress(String address) {
		//1 - Récupérer la liste des personnes ayant la même adresse
		Set<Person> familyList = findAllByAddress(address);
		//Si personne ne correspond à l'adresse, renvoyer une liste de children et adults vide
		if (CollectionUtils.isEmpty(familyList)){
 			return null;
		}
		//2 - Peupler 2 listes , une liste d'enfants et une d'adultes
		Set<Person> children;
		Set<Person> adults = new HashSet<>();
		// Ajouter toutes les personnes de 18 ans ou moins dans la liste children
		children = familyList.stream().filter(person -> {
			if (person.getMedicalRecord() == null || person.getMedicalRecord().getBirthdate() == null) {
				return false;
			}
			int age = 0;
			Date birthDate = person.getMedicalRecord().getBirthdate();
			age = DateUtils.calculateAge(birthDate, new Date());

			return age <= 18;
		}).collect(Collectors.toSet());

		//S'il n'y a pas d'enfants, renvoyer une liste de children et adults vide
		if (CollectionUtils.isEmpty(children)){
			return null;
		}

		//Ajout de toutes les personnes dans la liste adults
		adults.addAll(familyList);
		//suppression de ous les enfants de la liste adults
		adults.removeAll(children);

		//Ajouts des personnes dans le set correspondant
		Set<FamilyMemberDTO> childrenDTO = children.stream().map(person -> new FamilyMemberDTO(person)).collect(Collectors.toSet());
		Set<FamilyMemberDTO> adultsDTO = adults.stream().map(person -> new FamilyMemberDTO(person)).collect(Collectors.toSet());

		//création de l'objet familyDTO qui va contenir 2 sets, childrenDTO et adultsDTO
		FamilyDTO familyDTO = new FamilyDTO();

		// Initialisation des listes
		familyDTO.setChildren(childrenDTO);
		familyDTO.setAdults(adultsDTO);

		return familyDTO;
	}




	@Override
	public ContainerPersonDTO findByFirestationStation(int stationNumber) {
		//1 - Fetch persons associated to this station
		Set<Person> personsByStation = findByFirestationStationIn(List.of(stationNumber));

		//2 - If there's no one associated to this station, send an empty map
		if(CollectionUtils.isEmpty(personsByStation)) {
			//return new ContainterPersonDTO();
			return null;
		}

		//Transform the list of personByAddress to list personFireStationDTO
		Set<PersonDTO> personDTOS = personsByStation.stream()
				.map(person -> new PersonDTO(person)).collect(Collectors.toSet());

		ContainerPersonDTO containerPersonDTO = new ContainerPersonDTO();
		containerPersonDTO.setPersons(personDTOS);

		AtomicInteger adultCount = new AtomicInteger();
		AtomicInteger childrenCount = new AtomicInteger();

		personsByStation.forEach(person -> {
			if (person.getMedicalRecord() == null || person.getMedicalRecord().getBirthdate() == null) {
				return;
			}
			int age = DateUtils.calculateAge(person.getMedicalRecord().getBirthdate(), new Date());
			if (age <= 18) {
				childrenCount.getAndIncrement();
			} else {
				adultCount.getAndIncrement();
			}
		});

		containerPersonDTO.setAdultNumber(adultCount.get());
		containerPersonDTO.setChildrenNumber(childrenCount.get());

		log.info("Returning a list of people linked to the firestation");
		return containerPersonDTO;
	}


	@Override
	public Map<String, Set<PersonFirestationDTO>> findPersonsByFirestationStationIn(List<Integer> stations) {
		//1 - Fetch persons associated to those stations
		Set<Person> personsByStations = findByFirestationStationIn(stations);

		//2 - If there's no one associated to those stations, send an empty map
		if(CollectionUtils.isEmpty(personsByStations)) {
			return null;
		}

		//3 - create a map that will group the persons by address
		Map<String, List<Person>> personsByAddress = personsByStations.stream().collect(Collectors
				.groupingBy(person -> person.getAddress()));

		//Transform the list of personByAddress to list personFireStationDTO
		Map<String, Set<PersonFirestationDTO>> personsByaddressDTO = new HashMap<>();
		personsByAddress.forEach((address, persons) -> {
			Set<PersonFirestationDTO> personFirestationDTOS = persons.stream()
					.map(person -> new PersonFirestationDTO(person)).collect(Collectors.toSet());
			personsByaddressDTO.put(address, personFirestationDTOS);
		});

		log.info("Returning a list of people linked to the list of  firestations");
		return personsByaddressDTO;
	}


	@Override
	public Person findByFirstNameAndLastName(String firstName, String lastName) {
		return personRepository.findByFirstNameAndLastName(firstName, lastName);
	}

}
