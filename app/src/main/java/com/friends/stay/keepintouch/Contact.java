package com.friends.stay.keepintouch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Avi on 11/05/2017.
 */

class Contact {

    private String name;
    private String number;
    private String nickname;
    private boolean isWatsApp;
    private boolean isSMS;
    private boolean isCall;
    private int communicationRate;
    private ArrayList<Msg> futureMessages;
    private ArrayList<Msg> historyMessages;
    private Context context;
    private Random random = new Random();

    public Contact(SimpleContact sc, Context context) {
        this.name = sc.name;
        this.number = sc.number;
        this.nickname = sc.nickname;
        this.isWatsApp = sc.isWatsApp;
        this.isSMS = sc.isSMS;
        this.isCall = sc.isCall;
        this.context = context;
        this.communicationRate = sc.communicationRate;
        futureMessages = new ArrayList<Msg>();
        historyMessages = new ArrayList<Msg>();
    }


    public ArrayList<Msg> getFutureMessages() {
        return futureMessages;
    }

    public Contact(String name,  String number, String nickname, boolean isCall,
                   boolean isSMS, boolean isWatsApp, int communicationRate, Context context) {
        this.name = name;
        this.number = number;
        this.nickname = nickname;
        this.isWatsApp = isWatsApp;
        this.isSMS = isSMS;
        this.isCall = isCall;
        this.context = context;
        this.communicationRate = communicationRate;
        futureMessages = new ArrayList<Msg>();
        historyMessages = new ArrayList<Msg>();


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isWatsApp() {
        return isWatsApp;
    }

    public void setWatsApp() {
        isWatsApp = !isWatsApp;
    }

    public boolean isSMS() {
        return isSMS;
    }

    public void setSMS() {
        isSMS = !isSMS;
    }

    public boolean isCall() {
        return isCall;
    }

    public void setCall() {
        isCall = !isCall;
    }

    public int getCommunicationRate() {
        return communicationRate;
    }

    public void setCommunicationRate(int communicationRate) {
        this.communicationRate = communicationRate;
    }


    public void addFutureMessages(Msg newMessages) {
        futureMessages.add(newMessages);
        MainActivity.getUser().getAllFutureMessages().add(newMessages);
        addMsgToManager(newMessages);
    }

    public void addMsgToManager(Msg msg){
        Log.d("here", "1");
        Intent intent = MainActivity.sendMsgintent;
        Log.d("here", "2");
        intent.putExtra("time", msg.getDateInMillis());
        Log.d("here", "3");
        PendingIntent pendingIntent = MainActivity.pendingIntent;
        Log.d("here", "4");
        AlarmManager alarmMgr = MainActivity.am;
        Log.d("here", "5");
        PendingIntent.getBroadcast(context, 0,  intent, 0);
        Log.d("here", "6");
        Log.d("msg.getDate - ", String.valueOf(msg.getDate()));
        Log.d("msg.getDateInMillis - ", String.valueOf(msg.getDateInMillis()));
        alarmMgr.set(AlarmManager.RTC_WAKEUP, msg.getDateInMillis(), pendingIntent);
        Log.d("here", "7");
    }

    public Msg createFutureMsg(){
        //check existing msgs
        Calendar c = Calendar.getInstance();
        Date newMsgDate;
        String name = this.name;
        String number = this.number;
        String content;
        Context context = this.context;
        int size = futureMessages.size();
        //calculate date
        if (size != 0){
            //there is futureMessages
            Msg last = futureMessages.get(size -1);
            Date lastMsgDate = last.getDate();
            c.setTime(lastMsgDate);
            Log.d("lastMsgDate", String.valueOf(lastMsgDate));
            Log.d("communicationRate", String.valueOf(communicationRate));
            c.add(Calendar.DAY_OF_MONTH, communicationRate);
        } else {
            //there is no futureMessages
            Date currentDate = new Date();
            c.setTime(currentDate);
            c.add(Calendar.DAY_OF_MONTH, communicationRate);
        }
        content = MainActivity.getUser().getRandomMsgTemplate();
        if (content.contains("<nickname>")){
            content = content.replace("<nickname>", nickname);
        }
        //update hour of day according to availability of user

        //Select the type of message sent
        //get the current day
        int DayOfResult = c.get(Calendar.DAY_OF_WEEK);
        //check availability
        ArrayList<String> currentDayRange = MainActivity.getUser().getAvailableTimes(DayOfResult);
        //change day if needed (no available times that day)
        while (currentDayRange.size() == 0){
            Log.d("DayOfResult", String.valueOf(DayOfResult));
            Log.d("currentDayRange", String.valueOf(currentDayRange));
            Log.d("here", "5.1");
            //move to the next day
            DayOfResult += 1;
            DayOfResult = DayOfResult % 8;
            if(DayOfResult == 0){
                DayOfResult = 1;
            }
            //check availability for the next day
            currentDayRange = MainActivity.getUser().getAvailableTimes(DayOfResult);
        }
        //update to day with availability
        c.add(Calendar.DAY_OF_WEEK, DayOfResult);
        //change time TODO date not right
        Log.d("currentDayRange", String.valueOf(currentDayRange));
        int index = random.nextInt(currentDayRange.size());
        Log.d("index", String.valueOf(index));
        String range = currentDayRange.get(index);
        int start = Integer.valueOf(range.split("-")[0]);
        int end = Integer.valueOf(range.split("-")[1]);
        int hour = random.nextInt((end - start) + 1) + start;
        c.set(Calendar.HOUR_OF_DAY, hour);
        int minute = random.nextInt((59 - 0) + 1) + 0;
        c.set(Calendar.MINUTE, minute);
        newMsgDate = c.getTime();
        Log.d("newMsgDate", String.valueOf(newMsgDate));
        boolean[] communicationTypeArray = {isWatsApp, isSMS, isCall};

        index = random.nextInt(3);
        while (!communicationTypeArray[index]){
            //continue to the next cell
            index += 1;
            index = index % 3;
        }
        Msg msg;
        if (index == 0){
            msg = new WhatsappMessage(name, number, newMsgDate, content, context, false);

        } else if (index == 1){
            msg = new SmsMessage(name, number, newMsgDate, content, context, false);
        } else {
            //index == 2
            msg = new Call(name, number, newMsgDate, content, context, false);
        }
//        addMsgToManager(msg); //TODO
        return msg;

    }


    public ArrayList<Msg> getHistoryMessages() {
        return historyMessages;
    }

    public void addHistoryMessages(Msg newMessages) {
        historyMessages.add(newMessages);
        MainActivity.getUser().getAllHistoryMessages().add(newMessages);
    }

    public void delFromHistoryMessages(int pos) {
        historyMessages.remove(pos);
    }

    public void delFromFutureMessages(int pos) {
        futureMessages.remove(pos);
    }

    public void delFromHistoryMessages(Msg msg) {
        historyMessages.remove(msg);
    }
    public void delFromFutureMessages(Msg msg) {
        futureMessages.remove(msg);
    }


    public SimpleContact makeSimpleContact() {
        return new SimpleContact(name,  number, nickname, isCall, isSMS, isWatsApp, communicationRate);
    }
}

class SimpleContact {
    public String name;
    public String number;
    public String nickname;
    public boolean isWatsApp;
    public boolean isSMS;
    public boolean isCall;
    public int communicationRate;

    //a dummy class for serialization
    public SimpleContact(String name,  String number, String nickname, boolean isCall,
                         boolean isSMS, boolean isWatsApp, int communicationRate) {
        this.name = name;
        this.number = number;
        this.nickname = nickname;
        this.isWatsApp = isWatsApp;
        this.isSMS = isSMS;
        this.isCall = isCall;
        this.communicationRate = communicationRate;
    }

}

