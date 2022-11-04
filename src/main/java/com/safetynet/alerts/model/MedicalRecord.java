package com.safetynet.alerts.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model of a medical record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MedicalRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@Transient
	private String firstName;
	@Transient
	private String lastName;
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	@ElementCollection
	private Set<String> medications = new HashSet();
	@ElementCollection
	private Set<String> allergies = new HashSet();

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Person person;
}
