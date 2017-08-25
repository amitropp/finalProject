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
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CALL = 1;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    public static final int REQUEST_READ_PHONE_STATE = 3;
    public static final int RESULT_PICK_CONTACT = 2;
    private static final String[] tabsNames = {"CONTACTS", "FUTURE", "HISTORY"};
    private static boolean firstEntrance = true;
    private static MainActivity mainActivity = null;

    public static Intent mNextCallIntent;
    public static SmsMessage mNextSmsMessage;
    private ImageButton mAddBtn;
    private Tabs mTabs;
    private ContactsListFragment mContactListFrag;
    private FutureHistoryFragment mFutureFrag;
    private FutureHistoryFragment mHistoryFrag;

    private static User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mainActivity == null) {
            mainActivity = this;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddBtn = (ImageButton) findViewById(R.id.ib_add_contact);
        mUser = new User();
        mContactListFrag = new ContactsListFragment();
        mFutureFrag = FutureHistoryFragment.newInstance(true, -1);
        mHistoryFrag = FutureHistoryFragment.newInstance(false, -1);
        Fragment[] tabFragments = {mContactListFrag, mFutureFrag, mHistoryFrag};
        //create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);

        if (firstEntrance) {
            //todo
        }
        //start running the manager
        Intent intent = new Intent(this, ManagerService.class);
//        startService(intent);

        test();
    }

    public static MainActivity getInstance() {
        return mainActivity;
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
        final Msg fsmsMessage = new SmsMessage("Amit Tropp", "1", date, "inFutureAmit", this, false);
        final Msg fsmsMessage2 = new WhatsappMessage("Arella Bloom", "2", date, "inFutureArella", this, false);
        final Msg fsmsMessage3 = new Call("Eyal Cohen", "3", date, "", this, false);

        final Msg hsmsMessage = new SmsMessage("Amit Tropp", "1", date, "inHistoryAmit", this, false);
        final Msg hsmsMessage2 = new WhatsappMessage("Arella Bloom", "2", date, "inHistoryArella", this, false);
        final Msg hsmsMessage3 = new Call("Eyal Cohen", "3", date, "inHistoryEyal", this, false);

//        final Handler handler = new Handler();
//        // Define the code block to be executed
//        final Runnable runnableCode = new Runnable() {
//            @Override
//            public void run() {
//                // Do something here on the main thread
//                smsMessage.send();
//                Toast.makeText(getApplicationContext(), "runnableCode executed.",
//                        Toast.LENGTH_LONG).show();
//            }
//        };
//        // Start the initial runnable task by posting through the handler
////        handler.postDelayed(runnableCode, 2000);
//        smsMessage.send();
//        Call mCall = new Call("5556", date, null, this);
//        mCall.callNow();
        mUser.addContact(new Contact("Amit Tropp", "1", "Amitush", true, true, true, 2, getApplicationContext()));
        mUser.addContact(new Contact("Arella Bloom", "2", "Relz", false, true, false, 5, getApplicationContext()));
        mUser.addContact(new Contact("Eyal Cohen", "3", "", false, false, true, 8, getApplicationContext()));
        mUser.addToAllFutureMsg(fsmsMessage);
        mUser.addToAllFutureMsg(fsmsMessage2);
        mUser.addToAllFutureMsg(fsmsMessage3);

        mUser.addToAllHistoryMsg(hsmsMessage);
        mUser.addToAllHistoryMsg(hsmsMessage2);
        mUser.addToAllHistoryMsg(hsmsMessage3);

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

    public static User getUser() {
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

    public void addFutureMsgAndUpdeateRecyclerV(Msg newMsg) {
        mUser.addToAllFutureMsg(newMsg);
        mFutureFrag.updateRecyclerViewOnAdd();
        if (EditContactActivity.getHistoryFrag() != null) {
            EditContactActivity.getFutureFrag().updateRecyclerViewOnAdd();
        }

    }

    public void updeateFutureRecyclerV(int pos, int contactPos) {
        if (contactPos == -1) {
            mFutureFrag.updateRVOnUpdate(pos);
        }
        else {
            if (EditContactActivity.getFutureFrag() != null) {
                EditContactActivity.getFutureFrag().updateRVOnUpdate(pos);
                Msg m = getUser().getContacts().get(contactPos).getFutureMessages().get(pos);
                int posInAllFutureMsgs = _getPosOfFutureMsg(m);
                if (posInAllFutureMsgs != -1) {
                    mFutureFrag.updateRVOnUpdate(posInAllFutureMsgs);
                }
            }
        }
    }

    private int _getPosOfFutureMsg(Msg m) {
        for (int i = 0; i < getUser().getAllFutureMessages().size(); i++) {
            Msg curMsg = getUser().getAllFutureMessages().get(i);
            if (m == curMsg) {
                return i;
            }
        }
        return -1;
    }

    public void deleteMsgAndUpdeateRecyclerV(int pos, boolean isFuture, int contactPos) {
        if (isFuture && contactPos == -1) {
            Contact c = mUser.delFromFutureMsg(pos);
            mFutureFrag.updateRecyclerViewOnRemove(pos);
            if (c != null && EditContactActivity.getFutureFrag() != null) {
                EditContactActivity.getFutureFrag().updateRecyclerViewOnRemove(c.getFutureMessages().size());
            }
        } else if (!isFuture && contactPos == -1) {
            Contact c = mUser.delFromHistoryMsg(pos);
            mHistoryFrag.updateRecyclerViewOnRemove(pos);
            if (c != null && EditContactActivity.getFutureFrag() != null) {
                EditContactActivity.getHistoryFrag().updateRecyclerViewOnRemove(c.getHistoryMessages().size());
            }
        } else if (isFuture && contactPos != -1) {
            Msg m = mUser.getContacts().get(contactPos).getFutureMessages().get(pos);
            mUser.getAllFutureMessages().remove(m);
            mUser.getContacts().get(contactPos).delFromFutureMessages(pos);
            EditContactActivity.getFutureFrag().updateRecyclerViewOnRemove(pos);
            mFutureFrag.updateRecyclerViewOnRemove(mUser.getAllFutureMessages().size() - 1);

        } else {
            Msg m = mUser.getContacts().get(contactPos).getHistoryMessages().get(pos);
            mUser.getAllHistoryMessages().remove(m);
            mUser.getContacts().get(contactPos).delFromHistoryMessages(pos);
            EditContactActivity.getHistoryFrag().updateRecyclerViewOnRemove(pos);
            mHistoryFrag.updateRecyclerViewOnRemove(mUser.getAllHistoryMessages().size() - 1);
        }
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
