package com.friends.stay.keepintouch;

import java.io.IOException;
import java.util.ArrayList;
//import java.util.Date;
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
        availableTimes.put(Day.SUNDAY, null);
        availableTimes.put(Day.MONDAY, null);
        availableTimes.put(Day.TUESDAY, null);
        availableTimes.put(Day.WEDNESDAY, null);
        availableTimes.put(Day.THURSDAY, null);
        availableTimes.put(Day.FRIDAY, null);
        availableTimes.put(Day.SATURDAY, null);
    }

    public void addContact(Contact newContact){
        contactsList.add(newContact);
    }

    public void deleteContact(Contact contactToDelete){
        contactsList.remove(contactToDelete);
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
            throw new IOException("Wrong Day!!!");
        }
    }
}
