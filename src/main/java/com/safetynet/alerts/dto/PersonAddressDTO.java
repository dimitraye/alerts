package com.safetynet.alerts.dto;


import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class PersonAddressDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private MedicalRecordInfoDTO medicalRecord;
    private int station;


    public PersonAddressDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
        medicalRecord = new MedicalRecordInfoDTO();
        if (person.getMedicalRecord() != null){
            medicalRecord.setMedications(person.getMedicalRecord().getMedications());
            medicalRecord.setAllergies(person.getMedicalRecord().getAllergies());
            int age = DateUtils.calculateAge(person.getMedicalRecord().getBirthdate(), new Date());
            medicalRecord.setAge(age);
        }
        if (person.getFirestation() != null){
            station = person.getFirestation().getStation();
        }
    }


}
