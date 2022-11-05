package com.safetynet.alerts;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

public class DataTest {


    public static Person getPerson1() {
        return Person.builder().firstName("Dayle")
                .lastName("Xeyb")
                .address("Univers 159789753123")
                .city("BladeWorkisya")
                .zip("999999")
                .phone("07-67-61-03-49")
                .email("W.M.X@gmail.com").build();
    }

    public static Person getPerson2() {
        return Person.builder().firstName("Dyron")
                .lastName("Davis")
                .address("Univers 153")
                .city("Neo Paris")
                .zip("12311")
                .phone("06-07-61-03-99")
                .email("wise.researcher@gmail.com").build();
    }

    public static Person getPerson3() {
        return Person.builder().firstName("Lyuke")
                .lastName("Lucius")
                .address("Univers 15")
                .city("Univerity Galaxy Wise")
                .zip("7777")
                .phone("07-08-12-03-05")
                .email("professor@gmail.com").build();
    }

    public static Person getPerson4() {
        return Person.builder().firstName("Shiro")
                .lastName("Swordiva")
                .address("Univers 159789753123")
                .city("BladeWorkisya")
                .zip("123456")
                .phone("07-67-61-03-49")
                .email("sword.queen@gmail.com").build();
    }

    public static MedicalRecord getMedicalRecord1() throws ParseException {
        return MedicalRecord.builder()
                .birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/1999"))
                .medications(Set.of("Paracetamol", "Aspirine"))
                .allergies(Set.of("pollen", "nuts")).build();
    }

    public static MedicalRecord getMedicalRecord2() throws ParseException {
        return MedicalRecord.builder()
                .birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("15/11/1959"))
                .medications(Set.of("Prednisolone"))
                .allergies(Set.of("cats","dogs" )).build();
    }

    public static MedicalRecord getChildMedicalRecord() throws ParseException {
        return MedicalRecord.builder()
                .birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("13/05/2015"))
                .medications(Set.of("Ventoline", "Symbicort"))
                .allergies(Set.of("pollen", "bees")).build();
    }

    public static Firestation getFirestation1() {
        return new Firestation("15 rue des champs", 5);
    }

    public static Firestation getFirestation2() {
        return new Firestation("17 rue de la mairie", 5);
    }

    public static Firestation getFirestation3() {
        return new Firestation("1 place des cristaux", 7);
    }
}
