package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Manage database operations for a person entity
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	/**
	 * Find a person by firstname and lastname
	 * @param firstName search param
	 * @param lastName search param
	 * @return the person searched
	 */
	public Person findByFirstNameAndLastName(String firstName, String lastName);

	/**
	 * Find emails by the city where people lives
	 * @param city search param
	 * @return the list of emails
	 */
	@Query("SELECT email FROM Person WHERE city = ?1")
	Set<String> findEmailsByCity(String city);

	/**
	 * Find the people by their address
	 * @param address search param
	 * @return the list of people
	 */
	Set<Person> findAllByAddress(String address);

	/**
	 * Find the phones of people linked to a firestation
	 * @param station search param
	 * @return list of phones
	 */
	@Query(value = "SELECT DISTINCT phone FROM person p, firestation f WHERE p.firestation_id = f.id AND station = ?", nativeQuery = true)
	Set<String> findPhonesByFirestationStation(int station);

	/**
	 * Find the people linked to a list of firestations
	 * @param stations search param
	 * @return list of people linked to a list of firestations
	 */
	Set<Person> findByFirestationStationIn(List<Integer> stations);

	/**
	 * projection of a person that contains only a phone.
	 */
	public interface Phone {
		String getPhone();
	}
}


