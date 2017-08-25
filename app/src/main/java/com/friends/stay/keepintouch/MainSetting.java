package com.friends.stay.keepintouch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by amitropp on 05/08/2017.
 */

public class MainSetting extends AppCompatActivity {
    private static final String[] tabsNames = {"TIMES", "TEMPLATES"};

    private Tabs mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_settings);

        Fragment[] tabFragments = {new SettingFragmentTimes(), new SettingFragmentTemplates()};
// create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}