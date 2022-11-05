package com.safetynet.alerts.dto;


import com.safetynet.alerts.model.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class PersonDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public PersonDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.phone = person.getPhone();
    }
}
