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
import android.util.Log;
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
     * @param context context of the calling activity
     */
    public SmsMessage(String name, String number, Date date, String content, Context context, boolean isManual) {
        super(name, number, date, content, context, isManual);
    }

    @Override
    public void send() {
        Log.d("SmsMessage send", "1");

        int permissionCheckReadState = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
        Log.d("SmsMessage send", "2");
        if (permissionCheckReadState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MainActivity) getContext(), new String[]{Manifest.permission.READ_PHONE_STATE}, MainActivity.REQUEST_READ_PHONE_STATE);
        }
        Log.d("SmsMessage send", "3");

        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);
        Log.d("SmsMessage send", "3.5");
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.d("SmsMessage send", "4");
            MainActivity.mNextSmsMessage = this;
            Log.d("SmsMessage send", "5");
            ActivityCompat.requestPermissions((MainActivity) getContext(), new String[]{Manifest.permission.SEND_SMS}, MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS);
            Log.d("SmsMessage send", "6");
        }
        else {
            Log.d("SmsMessage send", "7");
            SmsManager smsManager = SmsManager.getDefault();
            try {
                Log.d("SmsMessage send", "8");
                smsManager.sendTextMessage(getNumber(), null, getContent(), null, null);
                Log.d("SmsMessage send", "9");
                Toast.makeText(getContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                Log.d("SmsMessage send", "10");
            } catch (Exception e) {
                Toast.makeText(getContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public int getIconId() {
        return R.mipmap.ic_sms_image;
    }
}
