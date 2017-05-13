package com.friends.stay.keepintouch;

/**
 * Created by amitropp on 13/05/2017.
 */

public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY;


    static public boolean isDay(Day aName) {
        Day[] days = Day.values();
        for (Day dayName : days)
            if (dayName.equals(aName))
                return true;
        return false;
    }
}