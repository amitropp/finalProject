package com.friends.stay.keepintouch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
            mContacts = new ArrayList<Contact>();
            //test
            mContacts.add(new Contact("Amit", "5", "Amitush", true, true, true, true, 5));
            mContacts.add(new Contact("Avi", "7", "avush", true, true, true, true, 5));

            mContactsRecyclerView = new ContactsRecyclerView(view, getActivity(), mContacts);

            _setAddListener();
            myView = view;
            return view;
        }
        return myView;

    }

    private void _setAddListener()
    {
        mAddContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to new fragment - request to add a new contact
                AddContactFragment addContactFrag = new AddContactFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_contact_list, addContactFrag)
                        .addToBackStack(TAG_CONATCT)
                        .commit();
            }
        });

        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getContext(), MainSetting.class);
            startActivity(intent);
            }
        });

    }

}
