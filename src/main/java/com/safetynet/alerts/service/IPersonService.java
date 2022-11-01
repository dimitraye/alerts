package com.safetynet.alerts.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	 * @return
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
	Set<PersonRepository.Phone> findPhoneByFirestationStation(int station);

	/**
	 * get a list of persons by Firestation number
	 * @param stations
	 * @return List of persons
	 */
	Set<Person> findByFirestationStationIn(List<Integer> stations);


}
