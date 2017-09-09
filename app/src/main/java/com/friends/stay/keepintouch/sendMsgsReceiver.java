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
        Log.d("sendMsgsReceiver", "1");
        Log.d("intent 2 - ", String.valueOf(intent));
        sendCurrentTimeSms(intent);
        Log.d("sendMsgsReceiver", "2");
        this.context = context;

    }

    private void sendCurrentTimeSms(Intent intent){
        Log.d("sendCurrentTimeSms", "1");
        Log.d("intent 3 - ", intent.toString());
        String msgID = intent.getStringExtra("msgID");

        if (MainActivity.getInstance() == null) {
            handleClosedApp(msgID);
        }

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
            if (contact != null) {
                contact.delFromFutureMessages(msgToSend);
            }
            Log.d("sendCurrentTimeSms", "8");
            MainActivity.getInstance().deleteFutureMsgAndUpdeateRecyclerV(msgToSend);
//            user.delFromFutureMsgByMsg(msgToSend);
//            MainActivity.getInstance().mFutureFrag.updateRVOnUpdate();
            Log.d("sendCurrentTimeSms", "9");
            //Move it to history (general and specific)
            MainActivity.getInstance().addHistoryMsgAndUpdeateRecyclerV(msgToSend);
//            user.addToAllHistoryMsg(msgToSend);
//            MainActivity.getInstance().mHistoryFrag.updateRVOnUpdate();
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
