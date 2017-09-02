package com.friends.stay.keepintouch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());

        builder.setTitle("Call " + getName() + "?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Remove the item within array at position
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callNow();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void callNow() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + getNumber()));
        //check for permission
        int result = ContextCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.CALL_PHONE);
        //if there is permission start calling
        if (result == PackageManager.PERMISSION_GRANTED){
            MainActivity.getInstance().startActivity(intent);
        }
        else {
            MainActivity.mNextCallIntent = intent;
            //there is no permission - so request for permission
            ActivityCompat.requestPermissions(MainActivity.getInstance(),new String[]{Manifest.permission.CALL_PHONE},
                    MainActivity.PERMISSION_REQUEST_CALL);
        }
    }

    public void send(Context context){}


    private void showNotification(String eventtext, Context ctx) {
//        NotificationManager notificationManager = (NotificationManager) MainActivity.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String MyText = "Reminder";
//        Notification mNotification = new Notification(R.mipmap.ic_jog_dial_answer, MyText, System.currentTimeMillis() );
//        //The three parameters are: 1. an icon, 2. a title, 3. time when the notification appears
//
//        String MyNotificationTitle = "Medicine!";
//        String MyNotificationText  = "Don't forget to take your medicine!";
//
//        Intent MyIntent = new Intent(Intent.ACTION_VIEW);
//        PendingIntent StartIntent = PendingIntent.getActivity(MainActivity.getInstance().getApplicationContext(),0,MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        //A PendingIntent will be fired when the notification is clicked. The FLAG_CANCEL_CURRENT flag cancels the pendingintent
//
//        mNotification.setLatestEventInfo(MainActivity.getInstance().getApplicationContext(), MyNotificationTitle, MyNotificationText, StartIntent);
//
//        int NOTIFICATION_ID = 1;
//        notificationManager.notify(NOTIFICATION_ID , mNotification);
    }


    public int getIconId() {
        return R.mipmap.ic_jog_dial_answer;
    }

}
