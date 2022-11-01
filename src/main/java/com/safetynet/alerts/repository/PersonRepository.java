package com.safetynet.alerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safetynet.alerts.model.Person;

import java.awt.*;
import java.util.Set;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	public Person findByFirstNameAndLastName(String firstName, String lastName);

	@Query("SELECT email FROM Person WHERE city = ?1")
	Set<String> findEmailsByCity(String city);

	Set<Person> findAllByAddress(String address);

	Set<Phone> findPhonesByFirestationStation(int station);

	public interface Phone {
		String getPhone();
	}




}


