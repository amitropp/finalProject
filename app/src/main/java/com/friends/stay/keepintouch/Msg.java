package com.friends.stay.keepintouch;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.Date;


/**
 * Created by Avi on 11/05/2017.
 * Abstract class of a message (children can be whatsapp message, sms message,
 * facebook message, etc.
 */

public abstract class Msg {
    //the contact the message is for
    private String number;
    //date message is timed to
    private Date date;
    //content of message
    private String content;
    //main activity context
    private Context context;
    //name of contact
    private String name;
    //is manual msg
    private boolean isManual;

    /**
     * abstract function to send the message. The inherit class will implement the
     * send function specific for the media we are using
     */
    public abstract void send();
    public abstract int getIconId();

    /**
     * ctor
     * @param number to whom are we sending the message
     * @param date date of timed message
     * @param content content of message
     * @param context context of the calling activity
     */
    public Msg(String name, String number, Date date, String content, Context context)
    public Msg(String name, String number, Date date, String content, Context context, boolean isManual)
    {
        this.number = number;
        this.date = date;
        this.content = content;
        this.context = context;
        this.name = name;
        this.isManual = isManual;
    }

    public Context getContext() {
        return context;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getName() { return name; }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isManual() { return isManual;  }
}
