package com.friends.stay.keepintouch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

/**
 * Created by Avi on 11/05/2017.
 * Sms message class
 */

public class SmsMessage extends Msg {
    /**
     * Construct an SmsMessage object
     * @param toContact to which contact will the message be sent to
     * @param date date to send the message
     * @param content content of the message
     * @param icon icon of message
     * @param context context of the calling activity
     */
    public SmsMessage(Contact toContact, Date date, String content, Bitmap icon, Context context) {
        super(toContact, date, content, icon, context);
    }

    @Override
    public void send() {
        String number = getToContact().getNumber();
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address"  , number);
        sendIntent.putExtra("sms_body", getContent());
        sendIntent.setType("vnd.android-dir/mms-sms");
        getContext().startActivity(sendIntent);


    }
}
