package com.friends.stay.keepintouch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Date;


public class Call extends Msg {

    public Call(String name, String number, Date date, String content, Context context, boolean isManual, int id) {
        super(name, number, date, "", context, isManual, id);
    }

    @Override
    public void send() {
        showNotification();
    }

    public void send(Context context){}


    private void showNotification() {
        int result = ContextCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.CALL_PHONE);
        //if there is permission start calling
        if (result != PackageManager.PERMISSION_GRANTED){
            //there is no permission - so request for permission
            ActivityCompat.requestPermissions(MainActivity.getInstance(),new String[]{Manifest.permission.CALL_PHONE},
                    MainActivity.PERMISSION_REQUEST_CALL);
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String number = getNumber();
        callIntent.setData(Uri.parse("tel:"+number));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.getInstance(), 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.getInstance())
                .setSmallIcon(R.drawable.ic_call)
                .setContentTitle(MainActivity.getInstance().getResources().getString(R.string.app_name))
                .setContentText("Call " + getName())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) MainActivity.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        int NOTIFICATION_ID = getId();
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    public int getIconId() {
        return R.mipmap.ic_jog_dial_answer;
    }

}
