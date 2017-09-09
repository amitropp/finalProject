package com.friends.stay.keepintouch;

import android.content.Context;

import java.util.Date;


public class MsgFactory {
    public static Msg newMsg(String name, String number, Date date, String content, Context context, boolean isCall,
                                         boolean isSms, boolean isWhatsapp, boolean isManual, int id) {
        if (isCall) {
            return new Call(name, number, date, content, context, isManual, id);
        }
        if (isSms) {
            return new SmsMessage(name, number, date, content, context, isManual, id);
        }
        if (isWhatsapp) {
            return new WhatsappMessage(name, number, date, content, context, isManual, id);
        }
        return null;
    }

    public static Msg newMsg(SimpleMsg sm, Context context) {
        return newMsg(sm.name, sm.number, sm.date, sm.content, context, sm.isCall, sm.isMsg, sm.isWa, sm.isManual, sm.id);
    }
}
