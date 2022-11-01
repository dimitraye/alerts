package com.safetynet.alerts.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ContainterPersonDTO {

    Set<PersonDTO> persons = new HashSet<>();

    int adultNumber;
    int childrenNumber;
}
