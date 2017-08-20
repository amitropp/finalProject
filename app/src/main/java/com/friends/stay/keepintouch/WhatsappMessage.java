package com.friends.stay.keepintouch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Avi on 11/05/2017.
 */

public class WhatsappMessage extends Msg {

    private final String WA_PACKAGE = "com.whatsapp";
    private final String WA_ERR =  "WhatsApp not Installed";

    /**
     * ctor
     *
     * @param number to whom are we sending the message
     * @param date      date of timed message
     * @param content   content of message
     * @param context   context of the calling activity
     */
    public WhatsappMessage(String name, String number, Date date, String content, Context context, boolean isManual) {
        super(name, number, date, content, context, isManual);
    }

    @Override
    public void send() {
        PackageManager pm = getContext().getPackageManager();
        //catch an exception if there is no
        try {
            //start a new send intent (send via whatsapp)
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            //set variable text to be the content of the message
            String text = getContent();

            PackageInfo info = pm.getPackageInfo(WA_PACKAGE, PackageManager.GET_META_DATA);
            waIntent.setPackage(WA_PACKAGE);

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            //start chooser
            getContext().startActivity(Intent.createChooser(waIntent, ""));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getContext(), WA_ERR, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public int getIconId() {
        return R.mipmap.ic_whatsapp;
    }
}
