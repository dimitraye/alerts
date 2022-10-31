package com.safetynet.alerts.dto;


import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class FamilyMemberDTO {

    private String firstName;
    private String lastName;
    private Integer age;

    public FamilyMemberDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        if (person.getMedicalRecord() != null){
            this.age = DateUtils.calculateAge(person.getMedicalRecord().getBirthdate(), new Date());
        }
    }

}
