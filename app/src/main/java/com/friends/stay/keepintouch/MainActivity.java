package com.friends.stay.keepintouch;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CALL = 1;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    public static final int REQUEST_READ_PHONE_STATE = 3;
    public static final int RESULT_PICK_CONTACT = 2;
    private static final String[] tabsNames = {"CONTACTS", "FUTURE", "HISTORY"};
    private static MainActivity mainActivity = null;

    public static Intent mNextCallIntent;
    public static Intent sendMsgintent;
    public static AlarmManager am;
    public static SmsMessage mNextSmsMessage;
    private ImageButton mAddBtn;
    private Tabs mTabs;
    private ContactsListFragment mContactListFrag;
    private FutureHistoryFragment mFutureFrag;
    private FutureHistoryFragment mHistoryFrag;
    private SharedPreferences mPrefs;
    private static User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mainActivity == null) {
            mainActivity = this;
        }
        super.onCreate(savedInstanceState);
         mPrefs = getPreferences(MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        _readUser();
        _readMsgTemplate();
        _readAvailableTimes();
        am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        mAddBtn = (ImageButton) findViewById(R.id.ib_add_contact);
        mContactListFrag = new ContactsListFragment();
        mFutureFrag = FutureHistoryFragment.newInstance(true, -1);
        mHistoryFrag = FutureHistoryFragment.newInstance(false, -1);
        Fragment[] tabFragments = {mContactListFrag, mFutureFrag, mHistoryFrag};
        //create tabs on screen using tab names array and tab fragments array
        mTabs = new Tabs(this, tabsNames, tabFragments);

        sendMsgintent =  new Intent(this, sendMsgsReceiver.class);
        Log.d("intent 0 - ", String.valueOf(sendMsgintent));
        //start running the manager
//        Intent intent = new Intent(this, ManagerService.class);
//        startService(intent);

        test();
    }

    public static MainActivity getInstance() {
        return mainActivity;
    }

    //test the program
    private void test() {
        Date date = new Date();
        Msg msg = new Call("Amit", "0524448111", date, "", this, true);
//        msg.send();

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

    @Override
    protected void onPause() {
        _saveUser();
        super.onPause();
    }

    @Override
    protected void onStop() {
        _saveUser();
        super.onStop();
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
        mFutureFrag.updateRVOnUpdate();
        mHistoryFrag.updateRVOnUpdate();
    }

    public void addFutureMsgAndUpdeateRecyclerV(Msg newMsg) {
        mUser.addToAllFutureMsg(newMsg);
        addMsgToManager(newMsg);
        mFutureFrag.updateRecyclerViewOnAdd();
        if (EditContactActivity.getFutureFrag() != null) {
            EditContactActivity.getFutureFrag().updateRecyclerViewOnAdd();
        }

    }

    public void addMsgToManager(Msg msg){
        Log.d("addMsgToManager", "1");
        sendMsgintent.putExtra("time", msg.getDateInMillis());
        Log.d("intent 1 - ", String.valueOf(sendMsgintent));
        Log.d("msg.getDate - ", String.valueOf(msg.getDate()));
        Log.d("msg.getDateInMillis - ", String.valueOf(msg.getDateInMillis()));
        Log.d("addMsgToManager", "2");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  sendMsgintent, 0);
        Log.d("addMsgToManager", "3");
        am.set(AlarmManager.RTC_WAKEUP, msg.getDateInMillis() + 1000, pendingIntent);
        Log.d("here", "4");
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

    private void _readMsgTemplate() {
        File filesDir = getFilesDir();
        File dataFile = new File(filesDir, "templates.txt");
        try {
            for (Object line : FileUtils.readLines(dataFile)){
                MainActivity.getUser().addTemplate((String) line);
            }
        } catch (IOException e) {
            //
        }
    }

    private void _readAvailableTimes() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Sunday
        boolean isSundayMorning = preferences.getBoolean("isSundayMorning", false);
        boolean isSundayNoon = preferences.getBoolean("isSundayNoon", false);
        boolean isSundayEvening = preferences.getBoolean("isSundayEvening", false);

        //Monday
        boolean isMondayMorning = preferences.getBoolean("isMondayMorning", false);
        boolean isMondayNoon = preferences.getBoolean("isMondayNoon", false);
        boolean isMondayEvening = preferences.getBoolean("isMondayEvening", false);

        //Tuesday
        boolean isTuesdayMorning = preferences.getBoolean("isTuesdayMorning", false);
        boolean isTuesdayNoon = preferences.getBoolean("isTuesdayNoon", false);
        boolean isTuesdayEvening = preferences.getBoolean("isTuesdayEvening", false);

        //Wednesday
        boolean isWednesdayMorning = preferences.getBoolean("isWednesdayMorning", false);
        boolean isWednesdayNoon = preferences.getBoolean("isWednesdayNoon", false);
        boolean isWednesdayEvening = preferences.getBoolean("isWednesdayEvening", false);

        //Thursday
        boolean isThursdayMorning = preferences.getBoolean("isThursdayMorning", false);
        boolean isThursdayNoon = preferences.getBoolean("isThursdayNoon", false);
        boolean isThursdayEvening = preferences.getBoolean("isThursdayEvening", false);

        //Friday
        boolean isFridayMorning = preferences.getBoolean("isFridayMorning", false);
        boolean isFridayNoon = preferences.getBoolean("isFridayNoon", false);
        boolean isFridayEvening = preferences.getBoolean("isFridayEvening", false);

        //Saturday
        boolean isSaturdayMorning = preferences.getBoolean("isSaturdayMorning", false);
        boolean isSaturdayNoon = preferences.getBoolean("isSaturdayNoon", false);
        boolean isSaturdayEvening = preferences.getBoolean("isSaturdayEvening", false);

        //update existing contact's details - Sunday
        if (isSundayMorning){
            mUser.setAvailableTimes(Calendar.SUNDAY, SettingFragmentTimes.MORNING);
        }
        if (isSundayNoon){
            mUser.setAvailableTimes(Calendar.SUNDAY, SettingFragmentTimes.NOON);
        }
        if (isSundayEvening){
            mUser.setAvailableTimes(Calendar.SUNDAY, SettingFragmentTimes.EVNING);
        }

        //update existing contact's details - Monday
        if (isMondayMorning){
            mUser.setAvailableTimes(Calendar.MONDAY, SettingFragmentTimes.MORNING);
        }
        if (isMondayNoon){
            mUser.setAvailableTimes(Calendar.MONDAY, SettingFragmentTimes.NOON);
        }
        if (isMondayEvening){
            mUser.setAvailableTimes(Calendar.MONDAY, SettingFragmentTimes.EVNING);
        }

        //update existing contact's details - Tuesday
        if (isTuesdayMorning){
            mUser.setAvailableTimes(Calendar.TUESDAY, SettingFragmentTimes.MORNING);
        }
        if (isTuesdayNoon){
            mUser.setAvailableTimes(Calendar.TUESDAY, SettingFragmentTimes.NOON);
        }
        if (isTuesdayEvening){
            mUser.setAvailableTimes(Calendar.TUESDAY, SettingFragmentTimes.EVNING);
        }

        //update existing contact's details - Tuesday
        if (isTuesdayMorning){
            mUser.setAvailableTimes(Calendar.TUESDAY, SettingFragmentTimes.MORNING);
        }
        if (isTuesdayNoon){
            mUser.setAvailableTimes(Calendar.TUESDAY, SettingFragmentTimes.NOON);
        }
        if (isTuesdayEvening){
            mUser.setAvailableTimes(Calendar.TUESDAY, SettingFragmentTimes.EVNING);

        }

        //update existing contact's details - Wednesday
        if (isWednesdayMorning){
            mUser.setAvailableTimes(Calendar.WEDNESDAY, SettingFragmentTimes.MORNING);
        }
        if (isWednesdayNoon){
            mUser.setAvailableTimes(Calendar.WEDNESDAY, SettingFragmentTimes.NOON);
        }
        if (isWednesdayEvening){
            mUser.setAvailableTimes(Calendar.WEDNESDAY, SettingFragmentTimes.EVNING);
        }

        //update existing contact's details - Thursday
        if (isThursdayMorning){
            mUser.setAvailableTimes(Calendar.THURSDAY, SettingFragmentTimes.MORNING);
        }
        if (isThursdayNoon){
            mUser.setAvailableTimes(Calendar.THURSDAY, SettingFragmentTimes.NOON);
        }
        if (isThursdayEvening){
            mUser.setAvailableTimes(Calendar.THURSDAY, SettingFragmentTimes.EVNING);
        }

        //update existing contact's details - Friday
        if (isFridayMorning){
            mUser.setAvailableTimes(Calendar.FRIDAY, SettingFragmentTimes.MORNING);
        }
        if (isFridayNoon){
            mUser.setAvailableTimes(Calendar.FRIDAY, SettingFragmentTimes.NOON);
        }
        if (isFridayEvening){
            mUser.setAvailableTimes(Calendar.FRIDAY, SettingFragmentTimes.EVNING);
        }

        //update existing contact's details - Saturday
        if (isSaturdayMorning){
            mUser.setAvailableTimes(Calendar.SATURDAY, SettingFragmentTimes.MORNING);
        }
        if (isSaturdayNoon){
            mUser.setAvailableTimes(Calendar.SATURDAY, SettingFragmentTimes.NOON);
        }
        if (isSaturdayEvening){
            mUser.setAvailableTimes(Calendar.SATURDAY, SettingFragmentTimes.EVNING);
        }

    }

    private void _readUser() {
        Gson gson = new Gson();
        Type typeSimpleContactArray = new TypeToken<ArrayList<SimpleContact>>(){}.getType();
        Type typeSimpleMsgArray = new TypeToken<ArrayList<SimpleMsg>>(){}.getType();


        String stringSimpleContacts = mPrefs.getString("simpleContacts", "");
        String stringSimpleFutureMsg = mPrefs.getString("simpleFutureMsg", "");
        String stringSimpleHistoryMsg = mPrefs.getString("simpleHistoryMsg", "");

        ArrayList<SimpleContact> simpleContactArrayList = gson.fromJson(stringSimpleContacts, typeSimpleContactArray);
        ArrayList<SimpleMsg> simpleFutureMsgs = gson.fromJson(stringSimpleFutureMsg, typeSimpleMsgArray);
        ArrayList<SimpleMsg> simpleHistoryMsgs = gson.fromJson(stringSimpleHistoryMsg, typeSimpleMsgArray);

        if (simpleContactArrayList == null) {
            //first time user entrances
            mUser = new User();
            Intent intent = new Intent(this, MainSetting.class);
            startActivity(intent);

        }
        else {
            mUser = new User(simpleContactArrayList, simpleFutureMsgs, simpleHistoryMsgs, this);
        }
    }

    private void _saveUser() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        Type typeSimpleContactArray = new TypeToken<ArrayList<SimpleContact>>(){}.getType();
        Type typeSimpleMsgArray = new TypeToken<ArrayList<SimpleMsg>>(){}.getType();

        prefsEditor.putString("simpleContacts", gson.toJson(mUser.getSimpleContacts(), typeSimpleContactArray));
        prefsEditor.putString("simpleFutureMsg", gson.toJson(mUser.getSimpleMsgs(true), typeSimpleMsgArray));
        prefsEditor.putString("simpleHistoryMsg", gson.toJson(mUser.getSimpleMsgs(false), typeSimpleMsgArray));
        prefsEditor.commit();


    }

}