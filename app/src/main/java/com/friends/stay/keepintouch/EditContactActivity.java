package com.friends.stay.keepintouch;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditContactActivity extends AppCompatActivity {
    private static final String[] tabsNames = {"SETTINGS", "FUTURE", "HISTORY"};
    Tabs mTabs;
    private AddContactFragment mContactSettingsFrag;
    private FutureHistoryFragment mFutureFrag;
    private FutureHistoryFragment mHistoryFrag;


    public static EditContactActivity newInstance(int pos) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_contact);
//        mContactSettingsFrag = AddContactFragment.newInstance(pos);
//        //todo make future and history go by contact's messages instead of all messages
//        mFutureFrag = FutureHistoryFragment.newInstance(true);
//        mHistoryFrag = FutureHistoryFragment.newInstance(false);
//
//
//        Fragment[] tabFragments = {(mContactSettingsFrag , mFutureFrag , mHistoryFrag};
//        //create tabs on screen using tab names array and tab fragments array
//        mTabs = new Tabs(this, tabsNames, tabFragments);

    }
}
