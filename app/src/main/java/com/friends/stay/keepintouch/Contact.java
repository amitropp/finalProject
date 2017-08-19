package com.friends.stay.keepintouch;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
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

        //add first 4 future msgs
        addFutureMessages(createFutureMsg());
        addFutureMessages(createFutureMsg());
        addFutureMessages(createFutureMsg());
        addFutureMessages(createFutureMsg());

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
        MainActivity.getUser().addToAllFutureMsg(newMessages);

    }

    public ArrayList<Msg> getFutureMessages() {
        return futureMessages;
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
            c.add(Calendar.DAY_OF_MONTH, communicationRate);
            newMsgDate = c.getTime();
        } else {
            //there is no futureMessages
            Date currentDate = new Date();
            c.setTime(currentDate);
            c.add(Calendar.DAY_OF_MONTH, communicationRate);
            newMsgDate = c.getTime();
        }

        content = MainActivity.getUser().getRandomMsgTemplate();
        if (content.contains("<nickname>")){
            content = content.replace("<nickname>", nickname);
        }

        //Select the type of message sent

        boolean[] communicationTypeArray = {isWatsApp, isSMS, isCall};

        Random random = new Random();
        int index = random.nextInt(3);
        while (communicationTypeArray[index] == false){
            //continue to the next cell
            index += 1;
            index = index % 3;
        }
        if (index == 0){
            return new WhatsappMessage(name, number, newMsgDate, content, context);
        } else if (index == 1){
            return new SmsMessage(name, number, newMsgDate, content, context);
        } else {
            //index == 2
            return new Call(name, number, newMsgDate, content, context);
        }

    }


    public ArrayList<Msg> getHistoryMessages() {
        return historyMessages;
    }

    public void addHistoryMessages(Msg newMessages) {
        historyMessages.add(newMessages);
        MainActivity.getUser().addToAllHistoryMsg(newMessages);

    }


}

