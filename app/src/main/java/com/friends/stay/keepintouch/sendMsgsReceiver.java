package com.friends.stay.keepintouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class sendMsgsReceiver extends BroadcastReceiver {
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        sendCurrentTimeSms(intent);
        this.context = context;

    }

    private void sendCurrentTimeSms(Intent intent){
        String msgID = intent.getStringExtra("msgID");

        if (MainActivity.getInstance() == null) {
            handleClosedApp(msgID);
        }

        if (msgID != "") {
            User user = MainActivity.getUser();
            Msg msgToSend = user.getFuturMsgByID(msgID);
            //send the msg
            msgToSend.send();
            //Remove it from future (general and specific)
            Contact contact = MainActivity.getUser().findContactByMsg(msgToSend);
            if (contact != null) {
                contact.delFromFutureMessages(msgToSend);
            }
            MainActivity.getInstance().deleteFutureMsgAndUpdeateRecyclerV(msgToSend);
            //Move it to history (general and specific)
            MainActivity.getInstance().addHistoryMsgAndUpdeateRecyclerV(msgToSend);
            //if not manually - Add new Mag to futureMsgs
            if (!msgToSend.isManual()){
                Msg newGeneratedMsg = contact.createFutureMsg();
                user.addToAllFutureMsg(newGeneratedMsg);
            }

        }

    }

    private void handleClosedApp(String msgID) {
//            //this means the app is closed
//            Gson gson = new Gson();
//            Type typeSimpleContactArray = new TypeToken<ArrayList<SimpleContact>>(){}.getType();
//            Type typeSimpleMsgArray = new TypeToken<ArrayList<SimpleMsg>>(){}.getType();
//            SharedPreferences mPrefs = context.getSharedPreferences("keepInTouch", 0);
//            String stringSimpleFutureMsg = mPrefs.getString("simpleFutureMsg", "");
//            ArrayList<SimpleMsg> simpleFutureMsgs = gson.fromJson(stringSimpleFutureMsg, typeSimpleMsgArray);
//            SimpleMsg simplemsgToSend = null;
//            for (SimpleMsg msg : simpleFutureMsgs) {
//                Log.d("getFuturMsgByID", String.valueOf(msg).split("@")[1]);
//                if (String.valueOf(msg).split("@")[1].equals(msgID)){
//                    simplemsgToSend = msg;
//                    break;
//                }
//            }
//            if (simplemsgToSend == null) {
//                Log.d("msgToSendIsNull", "null");
//            }
//            else {
//                Msg msgToSend = MsgFactory.newMsg(simplemsgToSend, context);
//                msgToSend.send(context);
//            }
        }
}
