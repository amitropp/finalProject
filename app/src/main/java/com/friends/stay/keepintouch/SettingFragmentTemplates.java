package com.friends.stay.keepintouch;


import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by amitropp on 05/08/2017.
 */

public class SettingFragmentTemplates extends Fragment {

    private int MAX_TEPLATES = 10;
    private ArrayList<String> msgTemplate;


    public SettingFragmentTemplates(){
        msgTemplate = new ArrayList<String>();
        msgTemplate.add("");
        msgTemplate.add("");
        msgTemplate.add("");
        msgTemplate.add("");
    }

    public ArrayList<String> getMsgTemplate() {
        return msgTemplate;
    }

    public void addMsgTemplate(String template) {
        msgTemplate.add(template);
    }
}
