package com.friends.stay.keepintouch;

import android.content.Context;

import java.util.Date;

/**
 * Created by user on 8/19/2017.
 */

public class MsgFactory {
    public static Msg newMsg(String name, String number, Date date, String content, Context context, boolean isCall,
                                         boolean isSms, boolean isWhatsapp, boolean isManual) {
        if (isCall) {
            return new Call(name, number, date, content, context, isManual);
        }
        if (isSms) {
            return new SmsMessage(name, number, date, content, context, isManual);
        }
        if (isWhatsapp) {
            return new WhatsappMessage(name, number, date, content, context, isManual);
        }
        return null;
    }

}
