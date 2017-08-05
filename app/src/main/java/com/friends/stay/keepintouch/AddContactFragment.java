package com.friends.stay.keepintouch;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * This fragment appears when user clicks on the add button - adding a new contact to his list
 * of contacts
 */
public class AddContactFragment extends Fragment {

    public static final int RESULT_PICK_CONTACT = 2;
    private Button mDoneBtn;
    private Button mChooseContactBtn;
    private String mChosenPhoneNumber = null;
    private String mChosenName = null;
    private View thisView = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        thisView = view;
        mChooseContactBtn = (Button)view.findViewById(R.id.btn_pick_contact);
        mDoneBtn = (Button)view.findViewById(R.id.btn_done);
        _setDoneListener();
        _setPickConatctListener();

        return view;

    }

    private void _setPickConatctListener() {
        mChooseContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check for the request code, we might be usign multiple startActivityForReslut
        switch (requestCode) {
            case RESULT_PICK_CONTACT:
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
            cursor = getContext().getContentResolver().query(uri, null, null, null, null);
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


    private void _setDoneListener() {
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                boolean isFacebook = false; //todo

                //get rate
                EditText etRate = (EditText)thisView.findViewById(R.id.et_days);
                int rateEveryXDays = Integer.valueOf(etRate.getText().toString());

                Contact newContact = new Contact(mChosenName, mChosenPhoneNumber, nickname,
                        isWhatsapp, isFacebook, isMsg, isCall, rateEveryXDays);
                //add new contact to list

                //return to last fragment
                getFragmentManager().popBackStack(ContactsListFragment.TAG_CONATCT, 1);
            }
        });
    }


}
