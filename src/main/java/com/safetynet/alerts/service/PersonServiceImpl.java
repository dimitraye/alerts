package com.safetynet.alerts.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;

@Service
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
	public Set<PersonRepository.Phone> findPhoneByFirestationStation(int station) {
		return personRepository.findPhonesByFirestationStation(station);
	}

	@Override
	public Person findByFirstNameAndLastName(String firstName, String lastName) {
		return personRepository.findByFirstNameAndLastName(firstName, lastName);
	}

}
