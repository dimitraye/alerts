package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;

public interface IPersonService {
	
	/**
	 * Get the list of persons
	 * @return List of persons
	 */
	public List<Person> getAllPersons();
	
	/**
	 * Search for a person by its firstName and lastName
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public Person findByFirstNameAndLastName(String firstName, String lastName);

	
	/**
	 * Add a new person
	 * @param person
	 * @return person
	 */
	public Person addPerson(Person person);
	
	/**
	 * Update a person
	 * @param person
	 * @return the person that has been modified
	 */
	public Person updatePerson(Person person);
	
	/**
	 * Delete a person based from its firstname and lastname
	 * @param person
	 */
	public void deletePerson(Person person);
}
