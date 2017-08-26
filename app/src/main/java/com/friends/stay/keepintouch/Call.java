package com.friends.stay.keepintouch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;

import java.util.Date;


public class Call extends Msg {

    public Call(String name, String number, Date date, String content, Context context, boolean isManual) {
        super(name, number, date, "", context, isManual);
    }

    @Override
    public void send() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
        //if there is permission start calling
        if (result == PackageManager.PERMISSION_GRANTED){
            getContext().startActivity(intent);
        }
        else {
            MainActivity.mNextCallIntent = intent;
            //there is no permission - so request for permission
            ActivityCompat.requestPermissions((MainActivity)getContext(),new String[]{Manifest.permission.CALL_PHONE},
                    MainActivity.PERMISSION_REQUEST_CALL);
        }
    }

    public int getIconId() {
        return R.mipmap.ic_jog_dial_answer;
    }

}
