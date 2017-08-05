package com.friends.stay.keepintouch;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by amitropp on 05/08/2017.
 */

public class SettingFragmentTemplates extends Activity {

    private int MAX_TEPLATES = 10;
    private ArrayList<String> msgTemplate;


    public SettingFragmentTemplates(){
        msgTemplate = new ArrayList<String>();
        msgTemplate.add("Hey <nickbame>, How are yoy?");
        msgTemplate.add("whats up <nickbame>?");
        msgTemplate.add("<nickbame>, I miss you!!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fregment_setting_templates);

        String[] mStringArray = new String[msgTemplate.size()];
        mStringArray = msgTemplate.toArray(mStringArray);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mStringArray);

        ListView listView = (ListView) findViewById(R.id.templates_list);
        listView.setAdapter(adapter);
    }


    public String getRandomMsgTemplate() {
        Random random = new Random();
        int index = random.nextInt(msgTemplate.size()-1);
        return msgTemplate.get(index);
    }

    public void addMsgTemplate(String template) {
        msgTemplate.add(template);
    }
}
