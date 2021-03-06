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
    public SmsMessage(String name, String number, Date date, String content, Context context, boolean isManual, int id) {
        super(name, number, date, content, context, isManual, id);
    }

    @Override
    public void send() {

        int permissionCheckReadState = ContextCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheckReadState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.getInstance(), new String[]{Manifest.permission.READ_PHONE_STATE}, MainActivity.REQUEST_READ_PHONE_STATE);
        }

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            MainActivity.mNextSmsMessage = this;
            ActivityCompat.requestPermissions(MainActivity.getInstance(), new String[]{Manifest.permission.SEND_SMS}, MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(getNumber(), null, getContent(), null, null);
                Toast.makeText(MainActivity.getInstance(), "SMS sent.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.getInstance(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public int getIconId() {
        return R.mipmap.ic_sms_image;
    }
}
