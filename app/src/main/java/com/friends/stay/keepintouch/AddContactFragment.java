package com.friends.stay.keepintouch;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment appears when user clicks on the add button - adding a new contact to his list
 * of contacts
 */
public class AddContactFragment extends Fragment {
    private ImageButton mDoneBtn;
    private TextView mChooseContactTv;
    private ImageButton mDelBtm;
    private Spinner mRateDropdown;
    private View thisView = null;
    private String mChosenPhoneNumber = null;
    private String mChosenName = null;
    private int mChosenRate = 7;
    private Contact mExistingContact = null;
    private static String [] mNumbers1_21 = null;
    private EditText mNicknameEt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mNumbers1_21 == null)
        {
            mNumbers1_21 = new String[21];
            for (int i = 0; i < 21; i++) {
                mNumbers1_21[i] = Integer.toString(i + 1);
            }
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        thisView = view;
        mChooseContactTv = (TextView) view.findViewById(R.id.btn_pick_contact);
        mDoneBtn = (ImageButton)view.findViewById(R.id.btn_done);
        mRateDropdown = (Spinner)thisView.findViewById(R.id.sp_days);
        mDelBtm = (ImageButton)view.findViewById(R.id.btn_del);
        mDelBtm.setColorFilter(Color.RED);
        mNicknameEt = (EditText)view.findViewById(R.id.et_nickname);
        Bundle args = getArguments();
        if (args != null) {
            mChosenName = args.getString("name", "");
            mChosenPhoneNumber = args.getString("number", "");
            if (mChosenName == "") {
                mChosenName = null;
                mChosenPhoneNumber = null;
                int indexOfContact = args.getInt("indexOfContact", -1);
                mExistingContact = MainActivity.getUser().getContacts().get(indexOfContact);
                mDelBtm.setVisibility(View.VISIBLE);
                setContact();
                _setDelListener();
            }
            mChooseContactTv.setText(mChosenName);
        }
        _setRateSpinnerListener();
        _setDoneListener();

        thisView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return view;

    }

    private void _setDelListener() {
        mDelBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().deleteCntactAndUpdeateRecyclerV(getArguments().getInt("indexOfContact", 0));
//                getFragmentManager().popBackStack(ContactsListFragment.TAG_CONATCT, 1);
                getActivity().finish();
            }
        });
    }

    private void _setRateSpinnerListener() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mNumbers1_21);
        mRateDropdown.setAdapter(adapter);
        mRateDropdown.setSelection(mChosenRate - 1);
        mRateDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mChosenRate = Integer.valueOf((String)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    // wait for the user to click the done button
    private void _setDoneListener() {
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChosenName = mChosenName != null;
                if (!isChosenName) {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.VISIBLE);
                }
                else {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.INVISIBLE);
                }

                //get nickname
                String nickname = mNicknameEt.getText().toString();
                //get which medias are pressed is pressed
                CheckBox cvIsWhatsapp = (CheckBox)thisView.findViewById(R.id.cb_whatsapp);
                boolean isWhatsapp = cvIsWhatsapp.isChecked();
                CheckBox cvIsCall = (CheckBox)thisView.findViewById(R.id.cb_call);
                boolean isCall = cvIsCall.isChecked();
                CheckBox cvIsMsg = (CheckBox)thisView.findViewById(R.id.cb_message);
                boolean isMsg = cvIsMsg.isChecked();

                //if no checkbox is checked - tell user to choose a contact
                boolean isChosenBox = isWhatsapp || isCall || isMsg;
                if (!isChosenBox) {
                    thisView.findViewById(R.id.tv_err_no_chosen_box).setVisibility(View.VISIBLE);
                }
                else {
                    thisView.findViewById(R.id.tv_err_no_chosen_box).setVisibility(View.INVISIBLE);
                }

                if (!isChosenBox || !isChosenName) {
                    return;
                }

                //update existing contact's details
                if (mExistingContact != null) {
                    if (mExistingContact.isCall() != isCall) {
                        mExistingContact.setCall();
                    }
                    if (mExistingContact.isSMS() != isMsg) {
                        mExistingContact.setSMS();
                    }
                    if (mExistingContact.isWatsApp() != isWhatsapp) {
                        mExistingContact.setWatsApp();
                    }
                    mExistingContact.setNickname(nickname);
                    mExistingContact.setCommunicationRate(mChosenRate);
                    ContactAdapter.updateContactIcons();
                    getActivity().finish();
                }
                else {
                    Contact newContact = new Contact(mChosenName, mChosenPhoneNumber, nickname,
                            isCall, isMsg, isWhatsapp, mChosenRate, getActivity());
                    //add new contact to list
//                    MainActivity activity = MainActivity.getInstance();
                    MainActivity.getInstance().addContactAndUpdeateRecyclerV(newContact);

                    //add first 4 future msgs
                    MainActivity.getInstance().addFutureMsgAndUpdeateRecyclerV(newContact.createFutureMsg());
                    MainActivity.getInstance().addFutureMsgAndUpdeateRecyclerV(newContact.createFutureMsg());
                    MainActivity.getInstance().addFutureMsgAndUpdeateRecyclerV(newContact.createFutureMsg());
                    MainActivity.getInstance().addFutureMsgAndUpdeateRecyclerV(newContact.createFutureMsg());

                    getFragmentManager().popBackStack(ContactsListFragment.TAG_CONATCT, 1);
                }
                //return to last fragment
            }
        });
    }


    public static AddContactFragment newInstance(int indexOfContact) {
        AddContactFragment newFrag = new AddContactFragment();
        Bundle args = new Bundle();
        args.putInt("indexOfContact", indexOfContact);
        newFrag.setArguments(args);
        return newFrag;
    }

    public static AddContactFragment newInstance(String name, String number) {
        AddContactFragment newFrag = new AddContactFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("number", number);
        newFrag.setArguments(args);
        return newFrag;
    }


    public void setContact() {
        Contact contact = mExistingContact;
        String name = contact.getName();
        mChooseContactTv.setText(name);
        mChosenName = name;
        mChosenRate = contact.getCommunicationRate();

        if (contact.isCall()) {
            CheckBox ch = (CheckBox)thisView.findViewById(R.id.cb_call);
            ch.setChecked(true);
        }
        if (contact.isSMS()) {
            CheckBox ch = (CheckBox)thisView.findViewById(R.id.cb_message);
            ch.setChecked(true);
        }
        if (contact.isWatsApp()) {
            CheckBox ch = (CheckBox)thisView.findViewById(R.id.cb_whatsapp);
            ch.setChecked(true);
        }

        mNicknameEt.setText(contact.getNickname());

    }


}
