package com.friends.stay.keepintouch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This fragment appears when user clicks on the add button - adding a new contact to his list
 * of contacts
 */
public class AddContactFragment extends Fragment {

    private Button mDoneBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        mDoneBtn = (Button)view.findViewById(R.id.btn_done);
        _setDoneListener();

        return view;

    }

    private void _setDoneListener() {
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();
                Contact newContact = new Contact(); //TODO complete
                //add new contact to list
                activity.getUser().addContact(newContact);

                //return to last fragment
                getFragmentManager().popBackStack();
            }
        });
    }


}
