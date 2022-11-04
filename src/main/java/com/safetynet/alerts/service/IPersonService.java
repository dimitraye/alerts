package com.safetynet.alerts.service;

import java.util.List;
import java.util.Set;

import com.safetynet.alerts.dto.ContainerPersonDTO;
import com.safetynet.alerts.dto.FamilyDTO;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;

/**
 * Interface that manage the interaction with the person entity
 */
public interface IPersonService {

	/**
	 * Get the list of persons
	 *
	 * @return List of persons
	 */
	public List<Person> getAllPersons();

	/**
	 * Search for a person by its firstName and lastName
	 *
	 * @param firstName
	 * @param lastName
	 * @return person
	 */
	public Person findByFirstNameAndLastName(String firstName, String lastName);


	/**
	 * Add a new person
	 *
	 * @param person
	 * @return person
	 */
	public Person addPerson(Person person);

	/**
	 * Update a person
	 *
	 * @param person
	 * @return the person that has been modified
	 */
	public Person updatePerson(Person person);

	/**
	 * Delete a person based from its firstname and lastname
	 *
	 * @param person
	 */
	public void deletePerson(Person person);

	/**
	 * Create a set of emails sorted by their city
	 * @param city
	 * @return
	 */
	Set<String> findEmailsByCity(String city);

	/**
	 * Find the persons that have the same address
	 * @param address
	 * @return Set of persons
	 */
	Set<Person> findAllByAddress(String address);

	/**
	 * get a List of phone numbers from persons that have the same Firestation station number
	 * @param station
	 * @return
	 */
	Set<String> findPhoneByFirestationStation(int station);

	/**
	 * get a list of persons by Firestation number
	 * @param stations
	 * @return List of persons
	 */
	Set<Person> findByFirestationStationIn(List<Integer> stations);

	/**
	 * List of children and the other family members linked to an address
	 * Display the list if there's at least a child in this address
	 * @param address search param
	 * @return list of children and family members for this address
	 */
	FamilyDTO getFamilyFromAddress(String address);


	/**
	 * Display the list of phone numbers of the people associated to this address
	 * @param stationNumber search param
	 * @return the list of phone numbers of the people associated to this address
	 */
	public ContainerPersonDTO findByFirestationStation(int stationNumber);
}
