package com.friends.stay.keepintouch;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CALL = 1;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    public static final int REQUEST_READ_PHONE_STATE = 3;
    private static final String[] tabsNames = {"CONTACTS", "FUTURE", "HISTORY"};
    private static boolean firstEntrance = true;

    public static Intent mNextCallIntent;
    public static SmsMessage mNextSmsMessage;
    private ImageButton mAddBtn;
    private Tabs mTabs;
    private ContactsListFragment mContactListFrag;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddBtn = (ImageButton) findViewById(R.id.ib_add_contact);
        mUser = new User();
        mContactListFrag = new ContactsListFragment();

        Fragment[] tabFragments = {mContactListFrag, new MessagesViewFragment(), new testFragment2()};
        //create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);

        if (firstEntrance) {
            //todo
        }

        test();
    }


    //test the program
    private void test() {
//        Contact me = new Contact("avi",  "5556", "", false, false, false, true, 2);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);
        calendar.set(Calendar.DAY_OF_MONTH, 16);
        calendar.set(Calendar.YEAR, 2017);
        final Msg smsMessage = new SmsMessage("5556", date, "hello", this);

        final Handler handler = new Handler();
        // Define the code block to be executed
        final Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                smsMessage.send();
                Toast.makeText(getApplicationContext(), "runnableCode executed.",
                        Toast.LENGTH_LONG).show();
            }
        };
        // Start the initial runnable task by posting through the handler
//        handler.postDelayed(runnableCode, 2000);


//        smsMessage.send();
//        Call mCall = new Call("5556", date, null, this);
//        mCall.callNow();
        mUser.addContact(new Contact("Amit Tropp", "5", "Amitush", true, true, true, 2));
        mUser.addContact(new Contact("Avi Hendler", "7", "avush", false, true, false, 5));
        mUser.addContact(new Contact("Eyal Cohen", "7", "", false, false, true, 8));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(mNextCallIntent);
                }
                break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    try {
                        smsManager.sendTextMessage(mNextSmsMessage.getNumber(), null, mNextSmsMessage.getContent(), null, null);
                        Toast.makeText(getApplicationContext(), "SMS sent.",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, permission denied.", Toast.LENGTH_LONG).show();
                    break;
                }
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    return;
                }
                break;
        }
    }

    public User getUser() {
        return mUser;
    }

    public void addContactAndUpdeateRecyclerV(Contact contact) {
        mUser.addContact(contact);
        mContactListFrag.updateRecyclerViewOnAdd();
    }

    public void deleteCntactAndUpdeateRecyclerV(int pos) {
        mUser.deleteContact(pos);
        mContactListFrag.updateRecyclerViewOnRemove(pos);
    }
}
