package com.friends.stay.keepintouch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Date;

/**
 * Created by Avi on 11/05/2017.
 */

public class Call {
    //the contact the message is for
    private String number;
    //date message is timed to
    private Date date;
    //main activity context
    private Context context;

    public Call(String number, Date date, Context context) {
        this.number = number;
        this.date = date;
        this.context = context;
    }

    public void callNow() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        //check for permission
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        //if there is permission start calling
        if (result == PackageManager.PERMISSION_GRANTED){
            context.startActivity(intent);
        }
        else {
            MainActivity.mNextCallIntent = intent;
            //there is no permission - so request for permission
            ActivityCompat.requestPermissions((MainActivity)context,new String[]{Manifest.permission.CALL_PHONE},
                    MainActivity.PERMISSION_REQUEST_CALL);
        }

    }

    public static int getIconId() {
        return R.drawable.ic_call;
    }
    
}


