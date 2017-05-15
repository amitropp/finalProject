package com.friends.stay.keepintouch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Avi on 13/05/2017.
 * contactList - this is the main screen, showing all contacts the user added to his list in
 * order to stay in contact with
 */

public class ContactsListFragment extends Fragment {

    private ImageButton mAddContactBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mAddContactBtn = (ImageButton)view.findViewById(R.id.ib_add_contact);
        _setAddListener();
        return view;

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
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
