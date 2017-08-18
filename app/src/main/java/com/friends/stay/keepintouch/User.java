package com.friends.stay.keepintouch;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.util.Date;
import java.util.HashMap;

/**
 * Created by amitropp on 13/05/2017.
 */

public class User {
    private ArrayList<Contact> contactsList;
    private HashMap<Day, ArrayList<String>> availableTimes;
    private ArrayList<String> msgTemplate;
    private ArrayList<Msg> allFutureMessages;
    private ArrayList<Msg> allHistoryMessages;


    public User() {
        contactsList = new ArrayList<Contact>();
        availableTimes = new HashMap<Day, ArrayList<String>>(); //day os week and times from 0 to 23
        msgTemplate = new ArrayList<String>();
        allFutureMessages = new ArrayList<>();
        allHistoryMessages = new ArrayList<>();

        //initialize all days
        availableTimes.put(Day.SUNDAY, new ArrayList<String>());
        availableTimes.put(Day.MONDAY, new ArrayList<String>());
        availableTimes.put(Day.TUESDAY, new ArrayList<String>());
        availableTimes.put(Day.WEDNESDAY, new ArrayList<String>());
        availableTimes.put(Day.THURSDAY, new ArrayList<String>());
        availableTimes.put(Day.FRIDAY, new ArrayList<String>());
        availableTimes.put(Day.SATURDAY, new ArrayList<String>());
    }

    public void addContact(Contact newContact){
        contactsList.add(newContact);
    }

    public ArrayList<Contact> getContacts() {return  contactsList;}

    public void deleteContact(Contact contactToDelete){
        contactsList.remove(contactToDelete);
    }

    public void deleteContact(int pos){
        contactsList.remove(pos);
    }

    public void deleteTemplate(String msgToDelete){
        msgTemplate.remove(msgToDelete);
    }

    public void addTemplate(String msgToAdd){
        msgTemplate.add(msgToAdd);
    }

    /**
     * set a day in the availableTimes hashMap. each setting require to update the range of the
     * specified day
     * @param dayName - the day we want to chang is range
     * @param range - String, range of hours : MORNING = "8-12", NOON = "12-17", EVNING = "17-22"
     * @throws IOException
     */
    public void setAvailableTimes(Day dayName, String range) throws IOException {
        if(Day.isDay(dayName)){
            ArrayList<String> current = availableTimes.get(dayName);
            //update the current
            current.add(range);
            availableTimes.put(dayName, current);
        } else {
            throw new IOException("Wrong Day!!!");
        }
    }

    public void addToAllFutureMsg(Msg m) {
        allFutureMessages.add(m);
    }

    public void addToAllHistoryMsg(Msg m) {
        allHistoryMessages.add(m);
    }

    public ArrayList<Msg> getAllFutureMessages() { return allFutureMessages; }
    public ArrayList<Msg> getAllHistoryMessages() { return allHistoryMessages; }

}
