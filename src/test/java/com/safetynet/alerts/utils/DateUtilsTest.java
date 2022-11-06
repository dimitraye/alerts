package com.safetynet.alerts.utils;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {



    @Test
    void shouldCalculateAge() throws ParseException {

        //1 - Creation data
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String stringBirthdate = "2007-05-15";
        String stringDate = "2022-05-15";
        Date birthdate =  formatter.parse(stringBirthdate);
        Date endDate =  formatter.parse(stringDate);
        int expectedAge = 15;

        //2 - Data processing
        int actualAge = DateUtils.calculateAge(birthdate, endDate);

        //3 - Test
        assertEquals(expectedAge, actualAge);

    }
}