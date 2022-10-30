package com.safetynet.alerts.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
