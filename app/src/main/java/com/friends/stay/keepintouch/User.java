package com.friends.stay.keepintouch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by amitropp on 13/05/2017.
 */

public class User {
    private ArrayList<Contact> contactsList;
    private HashMap<Day, ArrayList<ArrayList<Integer>>> availableTimes;
    private ArrayList<String> msgTemplate;

    public User() {
        contactsList = new ArrayList<Contact>();
        availableTimes = new HashMap<Day, ArrayList<ArrayList<Integer>>>(); //day os week and times from 0 to 23
        msgTemplate = new ArrayList<String>();

        //initialize all days
        availableTimes.put(Day.SUNDAY, new ArrayList<ArrayList<Integer>>());
        availableTimes.put(Day.MONDAY, new ArrayList<ArrayList<Integer>>());
        availableTimes.put(Day.TUESDAY, new ArrayList<ArrayList<Integer>>());
        availableTimes.put(Day.WEDNESDAY, new ArrayList<ArrayList<Integer>>());
        availableTimes.put(Day.THURSDAY, new ArrayList<ArrayList<Integer>>());
        availableTimes.put(Day.FRIDAY, new ArrayList<ArrayList<Integer>>());
        availableTimes.put(Day.SATURDAY, new ArrayList<ArrayList<Integer>>());
    }

    public void addContact(Contact newContact){
        contactsList.add(newContact);
    }

    public void deleteTemplate(String msgToDelete){
        msgTemplate.remove(msgToDelete);
    }

    public void addTemplate(String msgToAdd){
        msgTemplate.add(msgToAdd);
    }

    //give the user an option to edit an existing msg
    //need to save the original msg (before the change) in order to delete it
    public void editTemplate(String msgToEdit, String newMsg){
        msgTemplate.remove(msgToEdit);
        msgTemplate.add(newMsg);
    }

    /**
     * set a day in the availableTimes hashMap. each setting require to update the range of the
     * specified day
     * @param dayName - the day we want to chang is range
     * @param range - ArrayList<ArrayList<Integer>>, each inner ArrayList contain two numbers
     *              from 00 to 23 which represent a range og hours.
     *              for example, if the user available in Sunday from 12pm to 5pm and from 8pm
     *              to 10pm this param will by <<12,17>,<20,22>>
     * @throws IOException
     */
    public void setAvailableTimes(Day dayName, ArrayList<ArrayList<Integer>> range) throws IOException {
        if(Day.isDay(dayName)){
            availableTimes.put(dayName, range);
        } else {
            throw new IOException("Wrong Day sent");
        }
    }
}
