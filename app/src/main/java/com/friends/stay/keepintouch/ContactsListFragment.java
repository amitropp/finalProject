package com.friends.stay.keepintouch;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Avi on 13/05/2017.
 * contactList - this is the main screen, showing all contacts the user added to his list in
 * order to stay in contact with
 */

public class ContactsListFragment extends Fragment {

    private ImageButton mAddContactBtn;
    private ImageButton mSettingsBtn;
    public static final String TAG_CONATCT = "conFragTag";
    private View myView = null;
    private ContactsRecyclerView mContactsRecyclerView;
    private ArrayList<Contact> mContacts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //create view only once per instance
        if (myView == null)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
            mAddContactBtn = (ImageButton)view.findViewById(R.id.ib_add_contact);
            mSettingsBtn = (ImageButton)view.findViewById(R.id.ib_settings);
            mSettingsBtn.setColorFilter(Color.DKGRAY);
            MainActivity mainActivity = (MainActivity)getActivity();
            mContacts = MainActivity.getUser().getContacts();
            mContactsRecyclerView = new ContactsRecyclerView(view, mainActivity, mContacts);
            _setAddListener();
            myView = view;
            return view;
        }
        return myView;

    }

    public void updateRecyclerViewOnAdd() {
        int position =  mContacts.size() - 1;
        mContactsRecyclerView.mAdapter.notifyItemInserted(position);
        //scroll to the bottom of the list
        mContactsRecyclerView.mRecyclerView.scrollToPosition(position);
    }

    public void updateRecyclerViewOnRemove(int pos) {
        mContactsRecyclerView.mAdapter.notifyItemRemoved(pos);
        mContactsRecyclerView.mAdapter.notifyItemRangeChanged(pos, mContacts.size());
    }


    private void _setAddListener()
    {
        mAddContactBtn.bringToFront();
        mAddContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, MainActivity.RESULT_PICK_CONTACT);

            }
        });
        mSettingsBtn.bringToFront();
        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getContext(), MainSetting.class);
            startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check for the request code, we might be usign multiple startActivityForReslut
        switch (requestCode) {
            case MainActivity.RESULT_PICK_CONTACT:
                //switch to new fragment - request to add a new contact
                AddContactFragment addContactFrag = contactPicked(data);
                if (addContactFrag != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frag_contact_list, addContactFrag)
                            .addToBackStack(TAG_CONATCT)
                            .commit();
                }
                break;
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private AddContactFragment contactPicked(Intent data) {
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
            if (MainActivity.getUser().isExistContact(name, phoneNo)) {
                Toast.makeText(getContext(), "This contact exists in your contacts list already",
                        Toast.LENGTH_LONG).show();
                return null;
            }
            return AddContactFragment.newInstance(name, phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
