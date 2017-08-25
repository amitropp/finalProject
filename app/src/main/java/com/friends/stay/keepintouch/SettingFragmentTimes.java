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
    boolean isSundayNoon;
    boolean isSundayEvening;

    //Monday
    boolean isMondayMorning;
    boolean isMondayNoon;
    boolean isMondayEvening;

    //Tuesday
    boolean isTuesdayMorning;
    boolean isTuesdayNoon;
    boolean isTuesdayEvening;

    //Wednesday
    boolean isWednesdayMorning;
    boolean isWednesdayNoon;
    boolean isWednesdayEvening;

    //Thursday
    boolean isThursdayMorning;
    boolean isThursdayNoon;
    boolean isThursdayEvening;

    //Friday
    boolean isFridayMorning;
    boolean isFridayNoon;
    boolean isFridayEvening;

    //Saturday
    boolean isSaturdayMorning;
    boolean isSaturdayNoon;
    boolean isSaturdayEvening;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_setting_times, container, false);
//        mDoneBtn = (Button)view.findViewById(R.id.btn_done);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        thisView = view;
        cvSundayMorning = (CheckBox)thisView.findViewById(R.id.sunday_morning);
        setTimes();
        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("isSundayMorning", isSundayMorning);
        Log.d("save as= ", String.valueOf(isSundayMorning));
        preferences.edit().putBoolean("isSundayMorning", isSundayMorning).commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        isSundayMorning = savedInstanceState.getBoolean("isSundayMorning");
        isSundayMorning = preferences.getBoolean("isSundayMorning", false);
        Log.d("isSundayMorning = ", String.valueOf(isSundayMorning));
        cvSundayMorning.setChecked(isSundayMorning);
//        if (isSundayMorning) {
//            cvSundayMorning.setChecked(true);
//        }
    }

    public void setTimes(){
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
        //get times - Sunday
//        CheckBox cvSundayMorning = (CheckBox)thisView.findViewById(R.id.sunday_morning);
        isSundayMorning = cvSundayMorning.isChecked();
        preferences.edit().putBoolean("isSundayMorning", isSundayMorning).commit();
        Log.d("in done= ", String.valueOf(isSundayMorning));
        CheckBox cvSundayNoon = (CheckBox)thisView.findViewById(R.id.sunday_noon);
        isSundayNoon = cvSundayNoon.isChecked();
        CheckBox cvSundayEvening = (CheckBox)thisView.findViewById(R.id.sunday_evening);
        isSundayEvening = cvSundayEvening.isChecked();

        //get times - Monday
        CheckBox cvMondayMorning = (CheckBox)thisView.findViewById(R.id.monday_morning);
        isMondayMorning = cvMondayMorning.isChecked();
        CheckBox cvMondayNoon = (CheckBox)thisView.findViewById(R.id.monday_noon);
        isMondayNoon = cvMondayNoon.isChecked();
        CheckBox cvMondayEvening = (CheckBox)thisView.findViewById(R.id.monday_evening);
        isMondayEvening = cvMondayEvening.isChecked();

        //get times - Tuesday
        CheckBox cvTuesdayMorning = (CheckBox)thisView.findViewById(R.id.tuesday_morning);
        isTuesdayMorning = cvTuesdayMorning.isChecked();
        CheckBox cvTuesdayNoon = (CheckBox)thisView.findViewById(R.id.tuesday_noon);
        isTuesdayNoon = cvTuesdayNoon.isChecked();
        CheckBox cvTuesdayEvening = (CheckBox)thisView.findViewById(R.id.tuesday_evening);
        isTuesdayEvening = cvTuesdayEvening.isChecked();

        //get times - Wednesday
        CheckBox cvWednesdayMorning = (CheckBox)thisView.findViewById(R.id.wednesday_morning);
        isWednesdayMorning = cvWednesdayMorning.isChecked();
        CheckBox cvWednesdayNoon = (CheckBox)thisView.findViewById(R.id.wednesday_noon);
        isWednesdayNoon = cvWednesdayNoon.isChecked();
        CheckBox cvWednesdayEvening = (CheckBox)thisView.findViewById(R.id.wednesday_evening);
        isWednesdayEvening = cvWednesdayEvening.isChecked();

        //get times - Thursday
        CheckBox cvThursdayMorning = (CheckBox)thisView.findViewById(R.id.thursday_morning);
        isThursdayMorning = cvThursdayMorning.isChecked();
        CheckBox cvThursdayNoon = (CheckBox)thisView.findViewById(R.id.thursday_noon);
        isThursdayNoon = cvThursdayNoon.isChecked();
        CheckBox cvThursdayEvening = (CheckBox)thisView.findViewById(R.id.thursday_evening);
        isThursdayEvening = cvThursdayEvening.isChecked();

        //get times - Friday
        CheckBox cvFridayMorning = (CheckBox)thisView.findViewById(R.id.friday_morning);
        isFridayMorning = cvFridayMorning.isChecked();
        CheckBox cvFridayNoon = (CheckBox)thisView.findViewById(R.id.friday_noon);
        isFridayNoon = cvFridayNoon.isChecked();
        CheckBox cvFridayEvening = (CheckBox)thisView.findViewById(R.id.friday_evening);
        isFridayEvening = cvFridayEvening.isChecked();

        //get times - Saturday
        CheckBox cvSaturdayMorning = (CheckBox)thisView.findViewById(R.id.saturday_morning);
        isSaturdayMorning = cvSaturdayMorning.isChecked();
        CheckBox cvSaturdayNoon = (CheckBox)thisView.findViewById(R.id.saturday_noon);
        isSaturdayNoon = cvSaturdayNoon.isChecked();
        CheckBox cvSaturdayEvening = (CheckBox)thisView.findViewById(R.id.saturday_evening);
        isSaturdayEvening = cvSaturdayEvening.isChecked();

        //reset to all available times before updating
        MainActivity.getUser().clearAvailableTimes();

        //update existing contact's details - Sunday
        if (isSundayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.SUNDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isSundayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.SUNDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isSundayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.SUNDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update existing contact's details - Monday
        if (isMondayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.MONDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isMondayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.MONDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isMondayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.MONDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update existing contact's details - Tuesday
        if (isTuesdayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isTuesdayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isTuesdayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update existing contact's details - Tuesday
        if (isTuesdayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isTuesdayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isTuesdayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.TUESDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //update existing contact's details - Wednesday
        if (isWednesdayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.WEDNESDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isWednesdayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.WEDNESDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isWednesdayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.WEDNESDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update existing contact's details - Thursday
        if (isThursdayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.THURSDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isThursdayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.THURSDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isThursdayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.THURSDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update existing contact's details - Friday
        if (isFridayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.FRIDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isFridayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.FRIDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isFridayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.FRIDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update existing contact's details - Saturday
        if (isSaturdayMorning){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.SATURDAY, MORNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isSaturdayNoon){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.SATURDAY, NOON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isSaturdayEvening){
            try {
                MainActivity.getUser().setAvailableTimes(Calendar.SATURDAY, EVNING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}


