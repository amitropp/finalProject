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
        Log.d("intent 2 - ", String.valueOf(intent));
        sendCurrentTimeSms(intent);
        Log.d("sendMsgsReceiver", "2");

    }

    private void sendCurrentTimeSms(Intent intent){
        Log.d("sendCurrentTimeSms", "1");
        Log.d("intent 3 - ", String.valueOf(intent));
        String msgID = intent.getStringExtra("msgID");
        Log.d("#UserFutureMessagesSize", String.valueOf(MainActivity.getUser().getAllFutureMessages().size()));
        Log.d("sendCurrentTimeSms", "2");
        Log.d("msgID", String.valueOf(msgID));
        if (msgID != "") {
            Log.d("sendCurrentTimeSms", "3");
            User user = MainActivity.getUser();
            Log.d("sendCurrentTimeSms", "4");
            Msg msgToSend = user.getFuturMsgByID(msgID);
            Log.d("sendCurrentTimeSms", "5");
            Log.d("msgToSend", String.valueOf(msgToSend));
            Log.d("msgToSend.getContent", msgToSend.getContent());
            Log.d("msgToSend.getDate", String.valueOf(msgToSend.getDate()));
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
