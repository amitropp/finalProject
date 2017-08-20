package com.friends.stay.keepintouch;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This fragment appears when user clicks on the add button - adding a new contact to his list
 * of contacts
 */
public class AddContactFragment extends Fragment {
    private Button mDoneBtn;
    private Button mChooseContactBtn;
    private Button mDelBtm;
    private Spinner mRateDropdown;
    private View thisView = null;
    private String mChosenPhoneNumber = null;
    private String mChosenName = null;
    private int mChosenRate = 7;
    private Contact mExistingContact = null;
    private static String [] mNumbers1_21 = null;

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
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        thisView = view;
        MainActivity activity = (MainActivity)getActivity();
        mChooseContactBtn = (Button)view.findViewById(R.id.btn_pick_contact);
        mDoneBtn = (Button)view.findViewById(R.id.btn_done);
        mRateDropdown = (Spinner)thisView.findViewById(R.id.sp_days);
        mDelBtm = (Button)view.findViewById(R.id.btn_del);
        Bundle args = getArguments();
        if (args != null) {
            int indexOfContact = args.getInt("indexOfContact", 0);
            mExistingContact = activity.getUser().getContacts().get(indexOfContact);
            mDelBtm.setVisibility(View.VISIBLE);
            setContact();
            _setDelListener();
        }
        _setRateSpinnerListener();
        _setPickConatctListener();
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
                MainActivity activity = (MainActivity)getActivity();
                activity.deleteCntactAndUpdeateRecyclerV(getArguments().getInt("indexOfContact", 0));
                getFragmentManager().popBackStack(ContactsListFragment.TAG_CONATCT, 1);
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

    private void _setPickConatctListener() {
        if (mExistingContact == null) { //no contact chosen yet
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
                }
                else {
                    thisView.findViewById(R.id.tv_err_no_chosen_contact).setVisibility(View.INVISIBLE);
                }

                MainActivity activity = (MainActivity)getActivity();
                //get nickname
                EditText edNickname = (EditText)thisView.findViewById(R.id.et_nickname);
                String nickname = edNickname.getText().toString();
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
                }
                else {
                    Contact newContact = new Contact(mChosenName, mChosenPhoneNumber, nickname,
                            isCall, isMsg, isWhatsapp, mChosenRate, getActivity());
                    //add new contact to list
                    activity.addContactAndUpdeateRecyclerV(newContact);
                }
                //return to last fragment
                getFragmentManager().popBackStack(ContactsListFragment.TAG_CONATCT, 1);
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


    public void setContact() {
        Contact contact = mExistingContact;
        String name = contact.getName();
        mChooseContactBtn.setText(name);
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

        if (!contact.getNickname().equals("")) {
            EditText et = (EditText)thisView.findViewById(R.id.et_nickname);
            et.setText(contact.getNickname());
        }


    }


}
