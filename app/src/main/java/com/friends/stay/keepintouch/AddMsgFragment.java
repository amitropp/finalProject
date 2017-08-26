package com.friends.stay.keepintouch;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * This fragment appears when user clicks on the add button - adding a new msg to his list
 * of future messages
 */
public class AddMsgFragment extends Fragment {
    private View thisView;
    private boolean mIsFuture = true;
    private TextView mChooseContactBtn;
    private EditText mMessageTextEt;
    private ImageButton mDoneBtn;
    private ImageButton mDelBtm;
    private ImageButton mMsgTypeIcon;
    private Msg mExistingMsg = null;
    private String mChosenName;
    private String mChosenPhoneNumber;
    private Date mChosenDate = null;
    private int mindexOfMsgToEdit;
    private CheckBox[] mAllCb;
    private final static int CALL = 0;
    private final static int SMS = 1;
    private final static int WA = 2;
    private int mposOfContact;
    private ImageButton mChooseDateBtn;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    private boolean mIsExistingCall = false;
    private TextView mChoseDateTv;

    public static AddMsgFragment newInstance(int indexOfMsgToEdit, boolean isFuture, int posOfContact) {
        AddMsgFragment newFrag = new AddMsgFragment();
        Bundle args = new Bundle();
        args.putInt("indexOfMsgToEdit", indexOfMsgToEdit);
        args.putBoolean("isFuture", isFuture);
        args.putInt("posOfContact", posOfContact);
        newFrag.setArguments(args);
        return newFrag;
    }

    public static AddMsgFragment newInstance(int indexOfMsgToEdit, boolean isFuture, int posOfContact,
                                             String name, String phoneNo) {
        AddMsgFragment newFrag = new AddMsgFragment();
        Bundle args = new Bundle();
        args.putInt("indexOfMsgToEdit", indexOfMsgToEdit);
        args.putBoolean("isFuture", isFuture);
        args.putInt("posOfContact", posOfContact);
        args.putString("name", name);
        args.putString("number", phoneNo);
        newFrag.setArguments(args);
        return newFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_msg, container, false);
        thisView = view;
        mChooseContactBtn = (TextView)view.findViewById(R.id.btn_pick_contact);
        mDoneBtn = (ImageButton)view.findViewById(R.id.btn_done);
        mDelBtm = (ImageButton)view.findViewById(R.id.btn_del);
        mDelBtm.setColorFilter(Color.RED);
        mChooseDateBtn = (ImageButton)view.findViewById(R.id.ib_date);
        mChooseDateBtn.setColorFilter(Color.BLACK);
        mMessageTextEt = (EditText)view.findViewById(R.id.et_message_content);
        mChoseDateTv = (TextView) view.findViewById(R.id.tv_time_chosen);
        mAllCb = new CheckBox[3];
        mAllCb[CALL] = (CheckBox)view.findViewById(R.id.cb_call);
        mAllCb[SMS] = (CheckBox)view.findViewById(R.id.cb_message);
        mAllCb[WA] = (CheckBox)view.findViewById(R.id.cb_whatsapp);
        mMsgTypeIcon = (ImageButton)view.findViewById(R.id.ib_type_icon);

        Bundle args = getArguments();
        if (args != null) {
            mindexOfMsgToEdit = args.getInt("indexOfMsgToEdit", -1);
            mIsFuture =  args.getBoolean("isFuture", false);
            mposOfContact = args.getInt("posOfContact", -1);
            String number = args.getString("number", "");
            if (!number.equals("")) {
                mChosenPhoneNumber = number;
                mChosenName = args.getString("name", "");
                mChooseContactBtn.setText(mChosenName);
            }

            else {
                if (!mIsFuture) {
                    mMessageTextEt.setKeyListener(null);
                }

                boolean isInEditMsg = mindexOfMsgToEdit != -1;
                boolean isInSpecificContact = mposOfContact != -1;
                if (isInEditMsg) { // in messages of specific contact
                    if (mIsFuture && !isInSpecificContact) {
                        mExistingMsg = MainActivity.getUser().getAllFutureMessages().get(mindexOfMsgToEdit);
                    }
                    else if (!mIsFuture && !isInSpecificContact) {
                        mExistingMsg = MainActivity.getUser().getAllHistoryMessages().get(mindexOfMsgToEdit);
                    }
                    else if (mIsFuture && isInSpecificContact) { // edit future msg of specific contact
                        Contact curContact = MainActivity.getUser().getContacts().get(mposOfContact);
                        mExistingMsg = curContact.getFutureMessages().get(mindexOfMsgToEdit);
                    }
                    else { // edit history msgs of specific contact
                        mExistingMsg = MainActivity.getUser().getContacts().get(mposOfContact).getHistoryMessages().get(mindexOfMsgToEdit);
                    }
                    _setDisplayFromMsg();
                    mDelBtm.setVisibility(View.VISIBLE);
                    _setDelListener();
            }

            else { //add a new message, don't edit an existing one
                if (mposOfContact != -1) { //if we are inside future messages of a specific conact
                    Contact curContact = MainActivity.getUser().getContacts().get(mposOfContact);
                    mChosenName = curContact.getName();
                    mChooseContactBtn.setText(mChosenName);
                    mChosenPhoneNumber = curContact.getNumber();
                }
            }
            }

        }
        _setDoneListener();
        _setChooseDateListener();

        if (mExistingMsg == null) {
            // force only one checkbox to be checked at every time
            for (CheckBox cb : mAllCb) {
                _setUniqVInCheckBox(cb);
            }
        }

        return view;

    }

    private void _setChooseDateListener() {
        mChooseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment date = new DatePickerFragment();
                Bundle args = new Bundle();
                Calendar calender = Calendar.getInstance();
                args.putInt("year", calender.get(Calendar.YEAR));
                args.putInt("month", calender.get(Calendar.MONTH));
                args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
                date.setArguments(args);
                date.setCallBack(ondate);
                date.show(getFragmentManager(), "datePicker");

            }});
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            mYear = year;
            mMonth = month;
            mDay = day;
            timePicker();
        }
    };

    private void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        Date d = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute).getTime();
                        if (d.before(new Date())) {
                            Toast.makeText(getActivity(), "Please choose a future date instead!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            mChosenDate = d;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
                            String formattedDate = sdf.format(mChosenDate);
                            mChoseDateTv.setText(formattedDate);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void _setUniqVInCheckBox(CheckBox cb) {
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               if (isChecked) {
                   if (buttonView == mAllCb[CALL]) {
                       (thisView.findViewById(R.id.ll_message_content)).setVisibility(View.INVISIBLE);
                   }
                   else {
                       (thisView.findViewById(R.id.ll_message_content)).setVisibility(View.VISIBLE);
                   }
                    //make only one checkbox active at any given time
                   for (CheckBox cb : mAllCb) {
                       if (cb != buttonView) {
                           if (cb.isChecked()) {
                               cb.setChecked(false);
                           }
                       }
                   }
               }
           }
       }
        );

    }


    private void _setDelListener() {
        mDelBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MainActivity activity = MainActivity.getInstance();
            activity.deleteMsgAndUpdeateRecyclerV(getArguments().getInt("indexOfMsgToEdit", 0), mIsFuture, getArguments().getInt("posOfContact", -1));
            getFragmentManager().popBackStack(FutureHistoryFragment.TAG_MESSAGES, 1);
            }
        });
    }


//    private void _setPickConatctListener() {
//        if (mExistingMsg == null && mposOfContact == -1) { // creating new message for an unknown contact
//            mChooseContactBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//                    startActivityForResult(contactPickerIntent, MainActivity.RESULT_PICK_CONTACT);
//                }
//            });
//        }
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check for the request code, we might be usign multiple startActivityForReslut
//        switch (requestCode) {
//            case MainActivity.RESULT_PICK_CONTACT:
//                contactPicked(data);
//                break;
//        }
//    }


//    /**
//     * Query the Uri and read contact details. Handle the picked contact data.
//     * @param data
//     */
//    private void contactPicked(Intent data) {
//        Cursor cursor = null;
//        try {
//            String phoneNo = null ;
//            String name = null;
//            // getData() method will have the Content Uri of the selected contact
//            Uri uri = data.getData();
//            //Query the content uri
//            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//            cursor.moveToFirst();
//            // column index of the phone number
//            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            // column index of the contact name
//            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//            phoneNo = cursor.getString(phoneIndex);
//            name = cursor.getString(nameIndex);
//            // Set the value to the textviews
//            mChooseContactBtn.setText(name);
//            mChosenName = name;
//            mChosenPhoneNumber = phoneNo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // wait for the user to click the done button
    private void _setDoneListener() {
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgContent = mMessageTextEt.getText().toString();
                boolean isChosenName = mChosenName != null;
                boolean isDateChosen = mChosenDate != null;
                boolean isMsgContent = msgContent.length() > 0;
                if (!isChosenName) {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.VISIBLE);
                } else {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.INVISIBLE);
                }
                if (!isDateChosen) {
                    thisView.findViewById(R.id.tv_err_no_date).setVisibility(View.VISIBLE);
                } else {
                    thisView.findViewById(R.id.tv_err_no_date).setVisibility(View.INVISIBLE);
                }
                if (!isMsgContent) {
                    thisView.findViewById(R.id.tv_err_no_msg).setVisibility(View.VISIBLE);
                } else {
                    thisView.findViewById(R.id.tv_err_no_msg).setVisibility(View.INVISIBLE);
                }

                //get which medias are pressed is pressed
                boolean isCall = mAllCb[CALL].isChecked();
                boolean isMsg = mAllCb[SMS].isChecked();
                boolean isWhatsapp = mAllCb[WA].isChecked();

                //if no checkbox is checked - tell user to choose a contact
                boolean isChosenBox = isWhatsapp || isCall || isMsg || mExistingMsg != null;
                if (!isChosenBox) {
                    thisView.findViewById(R.id.tv_err_no_chosen_box).setVisibility(View.VISIBLE);
                } else {
                    thisView.findViewById(R.id.tv_err_no_chosen_box).setVisibility(View.INVISIBLE);
                }
                if (!isChosenBox || !isChosenName || (!isMsgContent && !(mIsExistingCall||isCall) ) || !isDateChosen) {
                    return;
                }
                MainActivity activity = MainActivity.getInstance();
                //update existing message
                if (mExistingMsg != null) {
                    if (mIsFuture)
                    {
                        mExistingMsg.setContent(msgContent);
                        mExistingMsg.setDate(mChosenDate);
                        activity.updeateFutureRecyclerV(mindexOfMsgToEdit, mposOfContact);
                    }
                }
                else {
                    Msg newMsg = MsgFactory.newMsg(mChosenName, mChosenPhoneNumber, mChosenDate,
                            msgContent, getActivity().getApplicationContext(), isCall, isMsg, isWhatsapp, true);
                    //add new message to list
                    activity.addFutureMsgAndUpdeateRecyclerV(newMsg);
                }

                //return to last fragment
                getFragmentManager().popBackStack(FutureHistoryFragment.TAG_MESSAGES, 1);
            }
        });}

    private void _setDisplayFromMsg() {
        mMessageTextEt.setText(mExistingMsg.getName());
        mMsgTypeIcon.setImageResource(mExistingMsg.getIconId());
        mMsgTypeIcon.setVisibility(View.VISIBLE);
        thisView.findViewById(R.id.ll_type_checkboxes).setVisibility(View.INVISIBLE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        String formattedDate = sdf.format(mExistingMsg.getDate());
        mChoseDateTv.setText(formattedDate);
        mChosenDate = mExistingMsg.getDate();
        mMessageTextEt.setText(mExistingMsg.getContent());
        mChosenName = mExistingMsg.getName();
        mChooseContactBtn.setText(mExistingMsg.getName());
        if (mExistingMsg instanceof Call) {
            (thisView.findViewById(R.id.ll_message_content)).setVisibility(View.INVISIBLE);
            mIsExistingCall = true;
        }

    }

}
