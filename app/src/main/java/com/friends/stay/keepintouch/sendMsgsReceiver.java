package com.friends.stay.keepintouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by amitropp on 26/08/2017.
 */

public class sendMsgsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        sendCurrentTimeSms(intent);

    }

    private void sendCurrentTimeSms(Intent intent){
        long msgMilliSeconds = intent.getLongExtra("time", 0);
        if (msgMilliSeconds != 0) {
            User user = MainActivity.getUser();
            Msg msgToSend = user.getFuturMsgByDate(msgMilliSeconds);
            //send the msg
            msgToSend.send();
            //Remove it from future (general and specific)
            Contact contact = MainActivity.getUser().findContactByMsg(msgToSend);
            contact.delFromFutureMessages(msgToSend);
            user.delFromFutureMsgByMsg(msgToSend);
            //Move it to history (general and specific)
            user.addToAllHistoryMsg(msgToSend);
            //if not manually - Add new Mag to futureMsgs
            if (!msgToSend.isManual()){
                Msg newGeneratedMsg = contact.createFutureMsg();
                user.addToAllFutureMsg(newGeneratedMsg);
            }

        }

    }
}
