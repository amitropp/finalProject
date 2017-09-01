package com.friends.stay.keepintouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by amitropp on 26/08/2017.
 */

public class sendMsgsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("sendMsgsReceiver", "1");
        sendCurrentTimeSms(intent);
        Log.d("sendMsgsReceiver", "2");

    }

    private void sendCurrentTimeSms(Intent intent){
        Log.d("sendCurrentTimeSms", "1");
        long msgMilliSeconds = intent.getLongExtra("time", 0);
        Log.d("sendCurrentTimeSms", "2");
        Log.d("msgMilliSeconds", String.valueOf(msgMilliSeconds));
        if (msgMilliSeconds != 0) {
            Log.d("sendCurrentTimeSms", "3");
            User user = MainActivity.getUser();
            Log.d("sendCurrentTimeSms", "4");
            Msg msgToSend = user.getFuturMsgByDate(msgMilliSeconds);
            Log.d("sendCurrentTimeSms", "5");
            Log.d("msgToSend.getName", msgToSend.getName());
            Log.d("msgToSend.getClass", String.valueOf(msgToSend.getClass()));
            //send the msg
            msgToSend.send();
            Log.d("sendCurrentTimeSms", "6");
            //Remove it from future (general and specific)
            Contact contact = MainActivity.getUser().findContactByMsg(msgToSend);
            Log.d("sendCurrentTimeSms", "7");
            contact.delFromFutureMessages(msgToSend);
            Log.d("sendCurrentTimeSms", "8");
            user.delFromFutureMsgByMsg(msgToSend);
            Log.d("sendCurrentTimeSms", "9");
            //Move it to history (general and specific)
            user.addToAllHistoryMsg(msgToSend);
            Log.d("sendCurrentTimeSms", "10");
            //if not manually - Add new Mag to futureMsgs
            if (!msgToSend.isManual()){
                Log.d("sendCurrentTimeSms", "11");
                Msg newGeneratedMsg = contact.createFutureMsg();
                Log.d("sendCurrentTimeSms", "12");
                user.addToAllFutureMsg(newGeneratedMsg);
                Log.d("sendCurrentTimeSms", "13");
            }

        }

    }
}
