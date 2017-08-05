package com.friends.stay.keepintouch;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CALL = 1 ;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2 ;
    private static final String[] tabsNames = {"CONTACTS", "FUTURE", "HISTORY"};

    private Call mNextCall;
//    private SmsMessage mNextSmsMessage;
    private ImageButton mAddBtn;
    private Tabs mTabs;
    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddBtn = (ImageButton)findViewById(R.id.ib_add_contact);
        mUser = new User();

        Fragment[] tabFragments = {new ContactsListFragment(), new testFragment(), new testFragment2()};
        //create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);
//        test();


    }


    //test the program
    private void test()
    {
//        Contact me = new Contact();
        Date date = new Date();
//        Msg waMessage = new WhatsappMessage(me, date, "hello", null, this);
        Msg smsMessage = new SmsMessage("5556", date, "hello", null, this);
        smsMessage.send();
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
//            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(mNextSmsMessage.getNumber(), null, mNextSmsMessage.getContent(), null, null);
//                    Toast.makeText(getApplicationContext(), "SMS sent.",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
//                    return;
//                }
        }
    }

    public User getUser()
    {
        return mUser;
    }



}
