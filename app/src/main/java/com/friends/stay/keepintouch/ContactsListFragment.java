package com.friends.stay.keepintouch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Avi on 13/05/2017.
 * contactList - this is the main screen, showing all contacts the user added to his list in
 * order to stay in contact with
 */

public class ContactsListFragment extends Fragment {
    /**
     * ctor
     */
    public ContactsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

}
