package com.friends.stay.keepintouch;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;

import java.util.Random;
/**
 * Created by amitropp on 13/05/2017.
 */

public class User {
    private ArrayList<Contact> contactsList;
    private HashMap<Day, ArrayList<ArrayList<Integer>>> availableTimes;
    private HashMap<Integer, ArrayList<String>> availableTimes;
    private ArrayList<String> msgTemplate;
    private ArrayList<Msg> allFutureMessages;
    private ArrayList<Msg> allHistoryMessages;


    public User() {
        contactsList = new ArrayList<Contact>();
        availableTimes = new HashMap<Day, ArrayList<ArrayList<Integer>>>(); //day os week and times from 0 to 23
        availableTimes = new HashMap<Integer, ArrayList<String>>(); //day os week and times from 0 to 23
        msgTemplate = new ArrayList<String>();
        allFutureMessages = new ArrayList<>();
        allHistoryMessages = new ArrayList<>();

        //initialize all days
        availableTimes.put(Day.SUNDAY, null);
        availableTimes.put(Day.MONDAY, null);
        availableTimes.put(Day.TUESDAY, null);
        availableTimes.put(Day.WEDNESDAY, null);
        availableTimes.put(Day.THURSDAY, null);
        availableTimes.put(Day.FRIDAY, null);
        availableTimes.put(Day.SATURDAY, null);
        availableTimes.put(Calendar.SUNDAY, new ArrayList<String>());
        availableTimes.put(Calendar.MONDAY, new ArrayList<String>());
        availableTimes.put(Calendar.TUESDAY, new ArrayList<String>());
        availableTimes.put(Calendar.WEDNESDAY, new ArrayList<String>());
        availableTimes.put(Calendar.THURSDAY, new ArrayList<String>());
        availableTimes.put(Calendar.FRIDAY, new ArrayList<String>());
        availableTimes.put(Calendar.SATURDAY, new ArrayList<String>());
    }

    public void addContact(Contact newContact){
        //add first future msgs to this contact
        //newContact.

        //add it to contactsList
        contactsList.add(newContact);
    }

    public ArrayList<Contact> getContacts() {return  contactsList;}

    public void deleteContact(Contact contactToDelete){
        contactsList.remove(contactToDelete);
    }

    public void deleteContact(int pos){
        //delete his future messages //TODO remove from comment after avi update
//        Contact c = contactsList.get(pos);
//        for (Msg msg : c.getFutureMessages()){
//            allFutureMessages.remove(msg);
//        }
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
    public void setAvailableTimes(int dayName, String range) throws IOException {
        ArrayList<String> current = availableTimes.get(dayName);
        //update the current
        current.add(range);
        availableTimes.put(dayName, current);
    }

    public ArrayList<String> getAvailableTimes(int dayName) {
        return availableTimes.get(dayName);
    }

    public void addToAllFutureMsg(Msg m) {
        allFutureMessages.add(m);
        Contact contact = _findContactByMsg(m);
        if (contact != null) {
            // the message is to an existing conatct
            contact.addFutureMessages(m);
        }
    }

    public void addToAllHistoryMsg(Msg m) {
        allHistoryMessages.add(m);
        Contact contact = _findContactByMsg(m);
        if (contact != null) {
            contact.addHistoryMessages(m);
        }
    }

    public void delFromFutureMsg(int pos) {
        Contact contact = _findContactByMsg(allFutureMessages.get(pos));
        if (contact != null) {
            contact.delFromFutureMessages(pos);
        }
        allFutureMessages.remove(pos);

    }

    public void delFromHistoryMsg(int pos) {
        Contact contact = _findContactByMsg(allFutureMessages.get(pos));
        if (contact != null) {
            contact.delFromHistoryMessages(pos);
        }
        allHistoryMessages.remove(pos);
    }

    private Contact _findContactByMsg(Msg m) {
        for (Contact curContact : contactsList) {
            if (curContact.getName().equals(m.getName()) && curContact.getNumber().equals(m.getNumber())) {
                return curContact;
            }
        }
        return null;
    }


    public ArrayList<Msg> getAllFutureMessages() { return allFutureMessages; }

    public ArrayList<Msg> getAllHistoryMessages() { return allHistoryMessages; }

    public String getRandomMsgTemplate() {
        Random random = new Random();
        if (msgTemplate.size() != 0){
            int index = random.nextInt(msgTemplate.size()-1);
            return msgTemplate.get(index);
        } else {
            return "Hi :)";
        }
    }


}
