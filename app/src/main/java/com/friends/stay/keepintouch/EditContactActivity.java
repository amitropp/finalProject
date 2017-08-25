package com.friends.stay.keepintouch;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class EditContactActivity extends AppCompatActivity {
    private static final String[] tabsNames = {"SETTINGS", "FUTURE", "HISTORY"};
    Tabs mTabs;
    private AddContactFragment mContactSettingsFrag;
    private static FutureHistoryFragment mFutureFrag = null;
    private static FutureHistoryFragment mHistoryFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Bundle b = getIntent().getExtras();
        int pos = -1; // or other values
        if(b != null)
            pos = b.getInt("pos");
        mContactSettingsFrag = AddContactFragment.newInstance(pos);
        mFutureFrag = FutureHistoryFragment.newInstance(true, pos);
        mHistoryFrag = FutureHistoryFragment.newInstance(false, pos);
        Fragment[] tabFragments = {mContactSettingsFrag, mFutureFrag , mHistoryFrag};
        //create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);
    }

    public static FutureHistoryFragment getFutureFrag() {
         return mFutureFrag;
    }

    public static FutureHistoryFragment getHistoryFrag() {
        return mHistoryFrag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
