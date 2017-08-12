package com.friends.stay.keepintouch;

import java.util.ArrayList;
import java.util.HashMap;


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

    public Contact(String name,  String number, String nickname, boolean isCall,
                   boolean isSMS, boolean isWatsApp, int communicationRate) {
        this.name = name;
        this.number = number;
        this.nickname = nickname;
        this.isWatsApp = isWatsApp;
        this.isSMS = isSMS;
        this.isCall = isCall;
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
    }

    public ArrayList<Msg> getHistoryMessages() {
        return historyMessages;
    }

    public void addHistoryMessages(Msg newMessages) {
        historyMessages.add(newMessages);
    }


}
