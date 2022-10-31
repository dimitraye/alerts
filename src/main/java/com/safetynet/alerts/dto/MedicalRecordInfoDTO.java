package com.safetynet.alerts.dto;



import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class MedicalRecordInfoDTO {
    private int age;
    private Set<String> medications = new HashSet();
    private Set<String> allergies = new HashSet();
}
