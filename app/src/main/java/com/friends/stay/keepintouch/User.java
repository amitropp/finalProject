package com.friends.stay.keepintouch;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
/**
 * Created by amitropp on 13/05/2017.
 */

public class User {
    private ArrayList<Contact> contactsList;
    private HashMap<Integer, ArrayList<String>> availableTimes;
    private ArrayList<String> msgTemplate;
    private ArrayList<Msg> allFutureMessages;
    private ArrayList<Msg> allHistoryMessages;


    public User()  {
        contactsList = new ArrayList<Contact>();
        availableTimes = new HashMap<Integer, ArrayList<String>>(); //day os week and times from 0 to 23
        msgTemplate = new ArrayList<String>();
        allFutureMessages = new ArrayList<>();
        allHistoryMessages = new ArrayList<>();

        //initialize all days
        clearAvailableTimes();
    }

    public User(ArrayList<SimpleContact> simpleContactArrayList, ArrayList<SimpleMsg> simpleFutureMsgs,
                ArrayList<SimpleMsg> simpleHistoryMsgs, Context context) {
        contactsList = new ArrayList<Contact>();
        availableTimes = new HashMap<Integer, ArrayList<String>>(); //day os week and times from 0 to 23
        msgTemplate = new ArrayList<String>();
        allFutureMessages = new ArrayList<>();
        allHistoryMessages = new ArrayList<>();
        //initialize all days
        clearAvailableTimes();
        makeContactlist(simpleContactArrayList, context);
        makeFutureHistoryList(simpleFutureMsgs, context, true);
        makeFutureHistoryList(simpleHistoryMsgs, context, false);
    }

    private void makeFutureHistoryList(ArrayList<SimpleMsg> simpleFutureMsgs, Context context, boolean isFuture) {
        ArrayList<Msg> allMsgs = isFuture ? allFutureMessages : allHistoryMessages;
        for (SimpleMsg sm : simpleFutureMsgs) {
            Msg newmsg = MsgFactory.newMsg(sm, context);
            allMsgs.add(newmsg);
            Contact c = findContactByMsg(newmsg);
            if (c != null) {
                if (isFuture) {
                    c.getFutureMessages().add(newmsg);
                }
                else {
                    c.getHistoryMessages().add(newmsg);
                }
            }


        }
    }

    private void makeContactlist(ArrayList<SimpleContact> simpleContactArrayList, Context context) {
        for (SimpleContact sc : simpleContactArrayList) {
            contactsList.add(new Contact(sc, context));
        }
    }

    public HashMap<Integer, ArrayList<String>> getAvailableTimes() {
        return availableTimes;
    }

    public void addContact(Contact newContact){
        //add first future msgs to this contact
        //newContact.

        //add it to contactsList
        contactsList.add(newContact);
    }

    public ArrayList<Contact> getContacts() {return  contactsList;}

    public void deleteContact(Contact contactToDelete){
        for (Msg msg : contactToDelete.getFutureMessages()){
            allFutureMessages.remove(msg);
        }
        contactsList.remove(contactToDelete);
    }

    public void deleteContact(int pos){
        //delete contact future messages
        Contact c = contactsList.get(pos);
        deleteContact(c);
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
    public void setAvailableTimes(int dayName, String range){
        ArrayList<String> current = availableTimes.get(dayName);
        //update the current
        current.add(range);
        availableTimes.put(dayName, current);
    }

    public void clearAvailableTimes(){
        availableTimes.put(Calendar.SUNDAY, new ArrayList<String>());
        availableTimes.put(Calendar.MONDAY, new ArrayList<String>());
        availableTimes.put(Calendar.TUESDAY, new ArrayList<String>());
        availableTimes.put(Calendar.WEDNESDAY, new ArrayList<String>());
        availableTimes.put(Calendar.THURSDAY, new ArrayList<String>());
        availableTimes.put(Calendar.FRIDAY, new ArrayList<String>());
        availableTimes.put(Calendar.SATURDAY, new ArrayList<String>());
    }

    public ArrayList<String> getAvailableTimes(int dayName) {
        return availableTimes.get(dayName);
    }

    public void addToAllFutureMsg(Msg m) {
        Contact contact = findContactByMsg(m);
        allFutureMessages.add(m);
        if (contact != null) {
            // the message is to an existing conatct
            contact.getFutureMessages().add(m);
        }
    }

    public void addToAllHistoryMsg(Msg m) {
        allHistoryMessages.add(m);
        Contact contact = findContactByMsg(m);
        if (contact != null) {
            contact.getHistoryMessages().add(m);
        }
    }

    public Contact delFromFutureMsg(int pos) {
        Contact contact = findContactByMsg(allFutureMessages.get(pos));
        if (contact != null) {
            contact.delFromFutureMessages(allFutureMessages.get(pos));
        }
        allFutureMessages.remove(pos);
        return contact;
    }

    public Contact delFromFutureMsgByMsg(Msg msg) {
        Contact contact = findContactByMsg(msg);
        if (contact != null) {
            contact.delFromFutureMessages(msg);
        }
        allFutureMessages.remove(msg);
        return contact;
    }

    public Contact delFromHistoryMsg(int pos) {
        Contact contact = findContactByMsg(allHistoryMessages.get(pos));
        if (contact != null) {
            contact.delFromHistoryMessages(allHistoryMessages.get(pos));
        }
        allHistoryMessages.remove(pos);
        return contact;
    }

    public Contact findContactByMsg(Msg m) {
        for (Contact curContact : contactsList) {
            if (curContact.getName().equals(m.getName()) && curContact.getNumber().equals(m.getNumber())) {
                return curContact;
            }
        }
        return null;
    }

    public Msg getFuturMsgByDate(long milliSeconds){
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(milliSeconds);
//        Date date = c.getTime();
        for (Msg msg : allFutureMessages) {
            if (msg.getDateInMillis() ==milliSeconds){
                return msg;
            }
        }
        return null;

    }

    public ArrayList<Msg> getAllFutureMessages() { return allFutureMessages; }

    public ArrayList<Msg> getAllHistoryMessages() { return allHistoryMessages; }

    public String getRandomMsgTemplate() {
        Random random = new Random();
        Log.d("msgTemplate", String.valueOf(msgTemplate));
        if (msgTemplate.size() != 0){
            int index = random.nextInt(msgTemplate.size());
            return msgTemplate.get(index);
        } else {
            return "Hi :)";
        }
    }


    public ArrayList<SimpleContact> getSimpleContacts() {
        ArrayList<SimpleContact> res = new ArrayList<>();
        for (Contact c : contactsList) {
            res.add(c.makeSimpleContact());
        }
    return res;
    }

    public ArrayList<SimpleMsg> getSimpleMsgs(boolean isFuture) {
        ArrayList<SimpleMsg> res = new ArrayList<>();
        ArrayList<Msg> myMsgs;
        myMsgs = isFuture ?  allFutureMessages : allHistoryMessages;
        for (Msg m : myMsgs) {
            res.add(m.makeSimpleMsg());
        }
        return res;
    }


    public boolean isExistContact(String name, String phoneNo) {
        for (Contact c : contactsList) {
            if (c.getName().equals(name) && c.getNumber().equals(phoneNo)) {
                return true;
            }
        }
        return false;
    }
}
