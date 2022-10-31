package com.safetynet.alerts.service;


import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirestationServiceImpl implements IFirestationService {
    @Autowired
    FirestationRepository firestationRepository;
    @Override
    public List<Firestation> getAllFirestations() {
        return firestationRepository.findAll();
    }

    @Override
    public Firestation findByAddress(String address) {
        return firestationRepository.findFirestationByAddress(address);
    }

    @Override
    public Firestation addMappingFirestation(Firestation firestation) {return firestationRepository.save(firestation);}

    @Override
    public Firestation updateNumberFirestation(Firestation firestation) {
        return addMappingFirestation(firestation);
    }

    @Override
    public void deleteMappingFirestation(Firestation firestation) {
        firestationRepository.delete(firestation);
    }

}
