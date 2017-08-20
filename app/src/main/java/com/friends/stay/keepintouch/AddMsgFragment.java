package com.friends.stay.keepintouch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Date;


/**
 * This fragment appears when user clicks on the add button - adding a new msg to his list
 * of future messages
 */
public class AddMsgFragment extends Fragment {
    private View thisView;
    private boolean mIsFuture = true;
    private Button mChooseContactBtn;
    private EditText mMessageTextEt;
    private Button mDoneBtn;
    private Button mDelBtm;
    private ImageButton mMsgTypeIcon;
    private Msg mExistingMsg = null;
    private String mChosenName;
    private String mChosenPhoneNumber;
    private Date mChosenDate;
    private int mindexOfMsgToEdit;
    private CheckBox[] mAllCb;
    private final static int CALL = 0;
    private final static int SMS = 1;
    private final static int WA = 2;

    public static AddMsgFragment newInstance(int indexOfMsgToEdit, boolean isFuture) {
        AddMsgFragment newFrag = new AddMsgFragment();
        Bundle args = new Bundle();
        args.putInt("indexOfMsgToEdit", indexOfMsgToEdit);
        args.putBoolean("isFuture", isFuture);
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
        mChooseContactBtn = (Button)view.findViewById(R.id.btn_pick_contact);
        mDoneBtn = (Button)view.findViewById(R.id.btn_done);
        mDelBtm = (Button)view.findViewById(R.id.btn_del);
        mMessageTextEt = (EditText)view.findViewById(R.id.et_message_content);
        mAllCb = new CheckBox[3];
        mAllCb[CALL] = (CheckBox)view.findViewById(R.id.cb_call);
        mAllCb[SMS] = (CheckBox)view.findViewById(R.id.cb_message);
        mAllCb[WA] = (CheckBox)view.findViewById(R.id.cb_whatsapp);
        mMsgTypeIcon = (ImageButton)view.findViewById(R.id.ib_type_icon);

        mChosenDate =  new Date(); //todo!!!!!
        Bundle args = getArguments();
        if (args != null) {
            mindexOfMsgToEdit = args.getInt("indexOfMsgToEdit", 0);
            mIsFuture =  args.getBoolean("isFuture", false);
            if (mIsFuture) {
                mExistingMsg = MainActivity.getUser().getAllFutureMessages().get(mindexOfMsgToEdit);
            }
            else {
                mExistingMsg = MainActivity.getUser().getAllHistoryMessages().get(mindexOfMsgToEdit);
            }
            mDelBtm.setVisibility(View.VISIBLE);
            _setDisplayFromMsg();
            _setDelListener();
        }
        _setPickConatctListener();
        _setDoneListener();

        if (mExistingMsg == null) {
            // force only one checkbox to be checked at every time
            for (CheckBox cb : mAllCb) {
                _setUniqVInCheckBox(cb);
            }
        }

        return view;

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
            MainActivity activity = (MainActivity)getActivity();
            activity.deleteMsgAndUpdeateRecyclerV(getArguments().getInt("indexOfMsgToEdit", 0), mIsFuture);
            getFragmentManager().popBackStack(FutureHistoryFragment.TAG_MESSAGES, 1);
            }
        });
    }


    private void _setPickConatctListener() {
        if (mExistingMsg == null) { // creating new message
            mChooseContactBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, MainActivity.RESULT_PICK_CONTACT);
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check for the request code, we might be usign multiple startActivityForReslut
        switch (requestCode) {
            case MainActivity.RESULT_PICK_CONTACT:
                contactPicked(data);
                break;
        }
    }


    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            mChooseContactBtn.setText(name);
            mChosenName = name;
            mChosenPhoneNumber = phoneNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // wait for the user to click the done button
    private void _setDoneListener() {
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChosenName = mChosenName != null;
                if (!isChosenName) {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.VISIBLE);
                } else {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.INVISIBLE);
                }

                String msgContent = mMessageTextEt.getText().toString();
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
                if (!isChosenBox || !isChosenName) {
                    return;
                }
                MainActivity activity = (MainActivity)getActivity();
                //update existing contact's details
                if (mExistingMsg != null) {
                    mExistingMsg.setContent(msgContent);
                    mExistingMsg.setDate(mChosenDate);
                    activity.updeateFutureRecyclerV(mindexOfMsgToEdit);
                }
                else {
                    Msg newMsg = MsgFactory.newMsg(mChosenName, mChosenPhoneNumber, mChosenDate,
                            msgContent, getActivity().getApplicationContext(), isCall, isMsg, isWhatsapp, true);
                    //add new contact to list
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
        mChosenDate.setTime(mExistingMsg.getDate().getTime());
        mMessageTextEt.setText(mExistingMsg.getContent());
        mChosenName = mExistingMsg.getName();
        mChooseContactBtn.setText(mExistingMsg.getName());
        if (mExistingMsg instanceof Call) {
            (thisView.findViewById(R.id.ll_message_content)).setVisibility(View.INVISIBLE);
        }

    }
}
