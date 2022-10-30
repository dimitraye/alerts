package com.safetynet.alerts.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

import com.safetynet.alerts.model.MedicalRecord;

import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalRecordForImport {
	private SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
	
	private Long Id;
	private String firstName;
	private String lastName;
	private String birthdate;
	private Set<String> medications = new HashSet();
	private Set<String> allergies = new HashSet();
	
	
	
	public MedicalRecord toMedicalRecord() {
		
		
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName(this.firstName);
		medicalRecord.setLastName(this.lastName);
		medicalRecord.setMedications(this.medications);
		medicalRecord.setAllergies(this.allergies);
		
		Date date = null;
		try {
			date = formatter.parse(birthdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		medicalRecord.setBirthdate(date);

		return medicalRecord;
	}
}
