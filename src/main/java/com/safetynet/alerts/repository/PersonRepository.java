package com.safetynet.alerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alerts.model.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	public Person findByFirstNameAndLastName(String firstName, String lastName);
}
