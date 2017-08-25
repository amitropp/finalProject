package com.friends.stay.keepintouch;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.Calendar;
import android.widget.TextView;
import android.widget.GridLayout.LayoutParams;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by amitropp on 05/08/2017.
 */

public class SettingFragmentTimes extends Fragment {

    private String MORNING = "8-12";
    private String NOON = "12-17";
    private String EVNING = "17-22";

    private Button mDoneBtn;
    private View thisView = null;

    SharedPreferences preferences;

    //Sunday
    CheckBox cvSundayMorning;
    boolean isSundayMorning;
    CheckBox cvSundayNoon;
    boolean isSundayNoon;
    CheckBox cvSundayEvening;
    boolean isSundayEvening;

    //Monday
    CheckBox cvMondayMorning;
    boolean isMondayMorning;
    CheckBox cvMondayNoon;
    boolean isMondayNoon;
    CheckBox cvMondayEvening;
    boolean isMondayEvening;

    //Tuesday
    CheckBox cvTuesdayMorning;
    boolean isTuesdayMorning;
    CheckBox cvTuesdayNoon;
    boolean isTuesdayNoon;
    CheckBox cvTuesdayEvening;
    boolean isTuesdayEvening;

    //Wednesday
    CheckBox cvWednesdayMorning;
    boolean isWednesdayMorning;
    CheckBox cvWednesdayNoon;
    boolean isWednesdayNoon;
    CheckBox cvWednesdayEvening;
    boolean isWednesdayEvening;

    //Thursday
    CheckBox cvThursdayMorning;
    boolean isThursdayMorning;
    CheckBox cvThursdayNoon;
    boolean isThursdayNoon;
    CheckBox cvThursdayEvening;
    boolean isThursdayEvening;

    //Friday
    CheckBox cvFridayMorning;
    boolean isFridayMorning;
    CheckBox cvFridayNoon;
    boolean isFridayNoon;
    CheckBox cvFridayEvening;
    boolean isFridayEvening;

    //Saturday
    CheckBox cvSaturdayMorning;
    boolean isSaturdayMorning;
    CheckBox cvSaturdayNoon;
    boolean isSaturdayNoon;
    CheckBox cvSaturdayEvening;
    boolean isSaturdayEvening;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_setting_times, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        thisView = view;

        //create all check boxs
        //Sunday
        cvSundayMorning = (CheckBox) thisView.findViewById(R.id.sunday_morning);
        cvSundayNoon = (CheckBox) thisView.findViewById(R.id.sunday_noon);
        cvSundayEvening = (CheckBox) thisView.findViewById(R.id.sunday_evening);

        //Monday
        cvMondayMorning = (CheckBox) thisView.findViewById(R.id.monday_morning);
        cvMondayNoon = (CheckBox) thisView.findViewById(R.id.monday_noon);
        cvMondayEvening = (CheckBox) thisView.findViewById(R.id.monday_evening);

        //Tuesday
        cvTuesdayMorning = (CheckBox) thisView.findViewById(R.id.tuesday_morning);
        cvTuesdayNoon = (CheckBox) thisView.findViewById(R.id.tuesday_noon);
        cvTuesdayEvening = (CheckBox) thisView.findViewById(R.id.tuesday_evening);

        //Wednesday
        cvWednesdayMorning = (CheckBox) thisView.findViewById(R.id.wednesday_morning);
        cvWednesdayNoon = (CheckBox) thisView.findViewById(R.id.wednesday_noon);
        cvWednesdayEvening = (CheckBox) thisView.findViewById(R.id.wednesday_evening);

        //Thursday
        cvThursdayMorning = (CheckBox) thisView.findViewById(R.id.thursday_morning);
        cvThursdayNoon = (CheckBox) thisView.findViewById(R.id.thursday_noon);
        cvThursdayEvening = (CheckBox) thisView.findViewById(R.id.thursday_evening);

        //Friday
        cvFridayMorning = (CheckBox) thisView.findViewById(R.id.friday_morning);
        cvFridayNoon = (CheckBox) thisView.findViewById(R.id.friday_noon);
        cvFridayEvening = (CheckBox) thisView.findViewById(R.id.friday_evening);

        //Saturday
        cvSaturdayMorning = (CheckBox) thisView.findViewById(R.id.saturday_morning);
        cvSaturdayNoon = (CheckBox) thisView.findViewById(R.id.saturday_noon);
        cvSaturdayEvening = (CheckBox) thisView.findViewById(R.id.saturday_evening);

        setTimes();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Sunday
        isSundayMorning = preferences.getBoolean("isSundayMorning", false);
        Log.d("isSundayMorning = ", String.valueOf(isSundayMorning));
        cvSundayMorning.setChecked(isSundayMorning);
        isSundayNoon = preferences.getBoolean("isSundayNoon", false);
        Log.d("isSundayNoon = ", String.valueOf(isSundayNoon));
        cvSundayNoon.setChecked(isSundayNoon);
        isSundayEvening = preferences.getBoolean("isSundayEvening", false);
        cvSundayEvening.setChecked(isSundayEvening);

        //Monday
        isMondayMorning = preferences.getBoolean("isMondayMorning", false);
        cvMondayMorning.setChecked(isMondayMorning);
        isMondayNoon = preferences.getBoolean("isMondayNoon", false);
        cvMondayNoon.setChecked(isMondayNoon);
        isMondayEvening = preferences.getBoolean("isMondayEvening", false);
        cvMondayEvening.setChecked(isMondayEvening);

        //Tuesday
        isTuesdayMorning = preferences.getBoolean("isTuesdayMorning", false);
        cvTuesdayMorning.setChecked(isTuesdayMorning);
        isTuesdayNoon = preferences.getBoolean("isTuesdayNoon", false);
        cvTuesdayNoon.setChecked(isTuesdayNoon);
        isTuesdayEvening = preferences.getBoolean("isTuesdayEvening", false);
        cvTuesdayEvening.setChecked(isTuesdayEvening);

        //Wednesday
        isWednesdayMorning = preferences.getBoolean("isWednesdayMorning", false);
        cvWednesdayMorning.setChecked(isWednesdayMorning);
        isWednesdayNoon = preferences.getBoolean("isWednesdayNoon", false);
        cvWednesdayNoon.setChecked(isWednesdayNoon);
        isWednesdayEvening = preferences.getBoolean("isWednesdayEvening", false);
        cvWednesdayEvening.setChecked(isWednesdayEvening);

        //Thursday
        isThursdayMorning = preferences.getBoolean("isThursdayMorning", false);
        cvThursdayMorning.setChecked(isThursdayMorning);
        isThursdayNoon = preferences.getBoolean("isThursdayNoon", false);
        cvThursdayNoon.setChecked(isThursdayNoon);
        isThursdayEvening = preferences.getBoolean("isThursdayEvening", false);
        cvThursdayEvening.setChecked(isThursdayEvening);

        //Friday
        isFridayMorning = preferences.getBoolean("isFridayMorning", false);
        cvFridayMorning.setChecked(isFridayMorning);
        isFridayNoon = preferences.getBoolean("isFridayNoon", false);
        cvFridayNoon.setChecked(isFridayNoon);
        isFridayEvening = preferences.getBoolean("isFridayEvening", false);
        cvFridayEvening.setChecked(isFridayEvening);

        //Saturday
        isSaturdayMorning = preferences.getBoolean("isSaturdayMorning", false);
        cvSaturdayMorning.setChecked(isSaturdayMorning);
        isSaturdayNoon = preferences.getBoolean("isSaturdayNoon", false);
        cvSaturdayNoon.setChecked(isSaturdayNoon);
        isSaturdayEvening = preferences.getBoolean("isSaturdayEvening", false);
        cvSaturdayEvening.setChecked(isSaturdayEvening);

    }

    public void setTimes(){
        Log.d("", "setTimes");
        mDoneBtn = (Button) thisView.findViewById(R.id.btn_done);
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _setDoneListener();
            }
        });
    }

    // wait for the user to click the done button
    private void _setDoneListener() {
        Log.d("", "_setDoneListener");
        //get times - Sunday
        isSundayMorning = cvSundayMorning.isChecked();
        isSundayNoon = cvSundayNoon.isChecked();
        isSundayEvening = cvSundayEvening.isChecked();

        //get times - Monday
        isMondayMorning = cvMondayMorning.isChecked();
        isMondayNoon = cvMondayNoon.isChecked();
        isMondayEvening = cvMondayEvening.isChecked();

        //get times - Tuesday
        isTuesdayMorning = cvTuesdayMorning.isChecked();
        isTuesdayNoon = cvTuesdayNoon.isChecked();
        isTuesdayEvening = cvTuesdayEvening.isChecked();

        //get times - Wednesday
        isWednesdayMorning = cvWednesdayMorning.isChecked();
        isWednesdayNoon = cvWednesdayNoon.isChecked();
        isWednesdayEvening = cvWednesdayEvening.isChecked();

        //get times - Thursday
        isThursdayMorning = cvThursdayMorning.isChecked();
        isThursdayNoon = cvThursdayNoon.isChecked();
        isThursdayEvening = cvThursdayEvening.isChecked();

        //get times - Friday
        isFridayMorning = cvFridayMorning.isChecked();
        isFridayNoon = cvFridayNoon.isChecked();
        isFridayEvening = cvFridayEvening.isChecked();

        //get times - Saturday
        isSaturdayMorning = cvSaturdayMorning.isChecked();
        isSaturdayNoon = cvSaturdayNoon.isChecked();
        isSaturdayEvening = cvSaturdayEvening.isChecked();


        //save times
        //Sunday
        preferences.edit().putBoolean("isSundayMorning", isSundayMorning).commit();
        Log.d("save isSundayMorning = ", String.valueOf(isSundayMorning));
        preferences.edit().putBoolean("isSundayNoon", isSundayNoon).commit();
        Log.d("save isSundayNoon = ", String.valueOf(isSundayNoon));
        preferences.edit().putBoolean("isSundayEvening", isSundayEvening).commit();

        //Monday
        preferences.edit().putBoolean("isMondayMorning", isMondayMorning).commit();
        preferences.edit().putBoolean("isMondayNoon", isMondayNoon).commit();
        preferences.edit().putBoolean("isMondayEvening", isMondayEvening).commit();

        //Tuesday
        preferences.edit().putBoolean("isTuesdayMorning", isTuesdayMorning).commit();
        preferences.edit().putBoolean("isTuesdayNoon", isTuesdayNoon).commit();
        preferences.edit().putBoolean("isTuesdayEvening", isTuesdayEvening).commit();

        //Wednesday
        preferences.edit().putBoolean("isWednesdayMorning", isWednesdayMorning).commit();
        preferences.edit().putBoolean("isWednesdayNoon", isWednesdayNoon).commit();
        preferences.edit().putBoolean("isWednesdayEvening", isWednesdayEvening).commit();

        //Thursday
        preferences.edit().putBoolean("isThursdayMorning", isThursdayMorning).commit();
        preferences.edit().putBoolean("isThursdayNoon", isThursdayNoon).commit();
        preferences.edit().putBoolean("isThursdayEvening", isThursdayEvening).commit();

        //Friday
        preferences.edit().putBoolean("isFridayMorning", isFridayMorning).commit();
        preferences.edit().putBoolean("isFridayNoon", isFridayNoon).commit();
        preferences.edit().putBoolean("isFridayEvening", isFridayEvening).commit();

        //Saturday
        preferences.edit().putBoolean("isSaturdayMorning", isSaturdayMorning).commit();
        preferences.edit().putBoolean("isSaturdayNoon", isSaturdayNoon).commit();
        preferences.edit().putBoolean("isSaturdayEvening", isSaturdayEvening).commit();

        //reset to all available times before updating
        MainActivity.getUser().clearAvailableTimes();

        //update existing contact's details - Sunday
        //update existing contact's details - Sunday
        if (isSundayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.SUNDAY, MORNING);
        }
        if (isSundayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.SUNDAY, NOON);
        }
        if (isSundayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.SUNDAY, EVNING);
        }

        //update existing contact's details - Monday
        if (isMondayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.MONDAY, MORNING);
        }
        if (isMondayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.MONDAY, NOON);
        }
        if (isMondayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.MONDAY, EVNING);
        }

        //update existing contact's details - Tuesday
        if (isTuesdayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, MORNING);
        }
        if (isTuesdayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, NOON);
        }
        if (isTuesdayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, EVNING);
        }

        //update existing contact's details - Tuesday
        if (isTuesdayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, MORNING);
        }
        if (isTuesdayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, NOON);
        }
        if (isTuesdayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, EVNING);

        }

        //update existing contact's details - Wednesday
        if (isWednesdayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.WEDNESDAY, MORNING);
        }
        if (isWednesdayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.WEDNESDAY, NOON);
        }
        if (isWednesdayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.WEDNESDAY, EVNING);
        }

        //update existing contact's details - Thursday
        if (isThursdayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.THURSDAY, MORNING);
        }
        if (isThursdayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.THURSDAY, NOON);
        }
        if (isThursdayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.THURSDAY, EVNING);
        }

        //update existing contact's details - Friday
        if (isFridayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.FRIDAY, MORNING);
        }
        if (isFridayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.FRIDAY, NOON);
        }
        if (isFridayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.FRIDAY, EVNING);
        }

        //update existing contact's details - Saturday
        if (isSaturdayMorning){
            MainActivity.getUser().setAvailableTimes(Calendar.SATURDAY, MORNING);
        }
        if (isSaturdayNoon){
            MainActivity.getUser().setAvailableTimes(Calendar.SATURDAY, NOON);
        }
        if (isSaturdayEvening){
            MainActivity.getUser().setAvailableTimes(Calendar.SATURDAY, EVNING);
        }
    }
}