package com.safetynet.alerts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class FamilyDTO {

    private Set<FamilyMemberDTO> children = new HashSet<>();
    private Set<FamilyMemberDTO> adults =  new HashSet<>();

}
