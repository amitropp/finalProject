package com.friends.stay.keepintouch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Avi on 11/05/2017.
 * Sms message class
 */

public class SmsMessage extends Msg {
    /**
     * Construct an SmsMessage object
     * @param number to which contact will the message be sent to
     * @param date date to send the message
     * @param content content of the message
     * @param icon icon of message
     * @param context context of the calling activity
     */
    public SmsMessage(String number, Date date, String content, Bitmap icon, Context context) {
        super(number, date, content, icon, context);
    }

    @Override
    public void send() {
        String phoneNo = getNumber();
        String content = getContent();
        SmsManager smsManager = SmsManager.getDefault();
        try {
            ActivityCompat.requestPermissions((MainActivity)getContext(),new String[]{Manifest.permission.SEND_SMS},2); //todo
            smsManager.sendTextMessage(phoneNo, null, content, null, null);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext().getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
        }

    }
}
