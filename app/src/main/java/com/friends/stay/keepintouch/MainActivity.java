package com.friends.stay.keepintouch;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CALL = 1 ;
    private static final String[] tabsNames = {"CONTACTS", "FUTURE", "HISTORY"};

    private Call mNextCall;
    private ImageButton mAddBtn;
    private Tabs mTabs;
    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddBtn = (ImageButton)findViewById(R.id.ib_add_contact);
        mUser = new User();

        Fragment[] tabFragments = {new ContactsListFragment(), new ContactsListFragment(), new ContactsListFragment()};
        //create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);


    }


    //test the program
    private void test()
    {
        Contact me = new Contact();
        Date date = new Date();
//        Msg waMessage = new WhatsappMessage(me, date, "hello", null, this);
//        Msg smsMessage = new SmsMessage(me, date, "hello", null, this);
//        smsMessage.send();

//        Call mCall = new Call(me, date, null, this);
//        mCall.callNow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mNextCall.callNow();
                }
                break;
        }
    }

    public User getUser()
    {
        return mUser;
    }



}
