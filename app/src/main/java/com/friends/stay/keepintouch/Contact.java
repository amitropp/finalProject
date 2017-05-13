package com.friends.stay.keepintouch;

import java.util.ArrayList;

/**
 * Created by Amit on 13/05/2017.
 */

class Contact {

    private String name;
    private String number;
    private String nickname;
    private boolean isWatsApp;
    private boolean isFacebook;
    private boolean isSMS;
    private boolean isCall;
    private int communicationRate; //in days
    private ArrayList<Msg> futureMessages;
    private ArrayList<Msg> historyMessages;

    public Contact(String name, String number, String nickname, boolean isWatsApp,
                   boolean isFacebook, boolean isSMS, boolean isCall, int communicationRate) {
        this.name = name;
        this.number = number;
        this.nickname = nickname;
        this.isWatsApp = isWatsApp;
        this.isFacebook = isFacebook;
        this.isSMS = isSMS;
        this.isCall = isCall;
        this.communicationRate = communicationRate;
        futureMessages = new ArrayList<Msg>();
        historyMessages = new ArrayList<Msg>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCommunicationRate(int communicationRate) {
        this.communicationRate = communicationRate;
    }

    public String getNumber()
    {
        //TODO update to real line
        //return number;
        return "0509223270";

    }

    public void setWatsApp(boolean status){
        isWatsApp = status;
    }

    public void setFacebook(boolean status){
        isFacebook = status;
    }

    public void setSMS(boolean status){
        isSMS = status;
    }

    public void setCall(boolean status){
        isCall = status;
    }

    public void addFuture(Msg newMsg){
        futureMessages.add(newMsg);
    }

    public void moveToHistory(Msg msgToMove){
        //TODO  make sure send() dunction od msg call this function
        futureMessages.remove(msgToMove);
        historyMessages.add(msgToMove);
    }
}
