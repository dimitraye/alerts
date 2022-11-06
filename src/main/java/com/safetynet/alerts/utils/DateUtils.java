package com.safetynet.alerts.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Facilitates operations on Dates
 */
public class DateUtils {
    /**
     * Calculates the current age of a person
     * @param birthdate to use to calculate the current age
     * @param date to use to calculate the current age
     * @return the current age of a person
     */
    public static int calculateAge(Date birthdate, Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int startingDate = Integer.parseInt(formatter.format(birthdate));
        int endDate = Integer.parseInt(formatter.format(date));
        int age = (endDate-startingDate)/10000;
        return age;
    }
}
