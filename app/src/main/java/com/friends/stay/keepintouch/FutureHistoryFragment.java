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
import android.widget.ImageButton;

import java.util.ArrayList;


/**
 * Future and History display of messages
 */
public class FutureHistoryFragment extends Fragment {
    private View myView = null;
    private boolean isFuture;
    private ImageButton mAddMessageBtn;
    private MsgRecyclerView mMsgRecyclerView;
    public static final String TAG_MESSAGES = "msgFragTag";
    private ArrayList<Msg> mAllMsgs;
    private int mPosOfContact = -1;

    public static FutureHistoryFragment newInstance(boolean isFuture, int posOfContact) {
        FutureHistoryFragment  newFrag = new FutureHistoryFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFuture", isFuture);
        args.putInt("posOfContact", posOfContact);
        newFrag.setArguments(args);
        return newFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            boolean isFutureFrag = args.getBoolean("isFuture", false);
            this.isFuture = isFutureFrag;
            mPosOfContact = args.getInt("posOfContact", -1);
            if (isFutureFrag && mPosOfContact == -1) {
                mAllMsgs = MainActivity.getUser().getAllFutureMessages();
            }
            else if (!isFutureFrag && mPosOfContact == -1) {
                mAllMsgs = MainActivity.getUser().getAllHistoryMessages();
            }
            else if (isFutureFrag && mPosOfContact != -1) {
                mAllMsgs = MainActivity.getUser().getContacts().get(mPosOfContact).getFutureMessages();
            }
            else {
                mAllMsgs = MainActivity.getUser().getContacts().get(mPosOfContact).getHistoryMessages();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //create view only once per instance
        if (myView == null)
        {
            View view;
            // Inflate the layout for this fragment
            if (isFuture) {
                view = inflater.inflate(R.layout.fragment_future, container, false);
                mAddMessageBtn = (ImageButton)view.findViewById(R.id.ib_add_message);
                _setAddListener();
            }
            else {
                view = inflater.inflate(R.layout.fragment_history, container, false);
            }
            myView = view;
            mMsgRecyclerView = new MsgRecyclerView(view, getActivity(), mAllMsgs, isFuture, mPosOfContact);
            return view;
        }
        return myView;

    }

    private void _setAddListener() {
        mAddMessageBtn.bringToFront();
        mAddMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to new fragment - request to add a new contact
                if (mPosOfContact != -1) {
                    // we are in a contact so we know his name and phone
                    Contact c = MainActivity.getInstance().getUser().getContacts().get(mPosOfContact);
                    AddMsgFragment addMsgFragment = AddMsgFragment.newInstance(-1, isFuture, mPosOfContact, c.getName(), c.getNumber());
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.frag_future, addMsgFragment)
                            .addToBackStack(TAG_MESSAGES)
                            .commit();
                }

                else {
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, MainActivity.RESULT_PICK_CONTACT);
                }
            }
        });
    }

    public void updateRecyclerViewOnAdd() {
        int position = mAllMsgs.size() - 1;
        mMsgRecyclerView.mAdapter.notifyItemInserted(position);
        //scroll to the bottom of the list
        mMsgRecyclerView.mRecyclerView.scrollToPosition(position);
    }

    public void updateRVOnUpdate(int pos) {
        if (mMsgRecyclerView != null) {
            mMsgRecyclerView.mAdapter.notifyItemChanged(pos);
        }
    }

    public void updateRVOnUpdate() {
        if (mMsgRecyclerView != null) {
            mMsgRecyclerView.mAdapter.notifyDataSetChanged();
        }
    }

    public void updateRecyclerViewOnRemove(int pos) {
        mMsgRecyclerView.mAdapter.notifyItemRemoved(pos);
        mMsgRecyclerView.mAdapter.notifyItemRangeChanged(pos, mAllMsgs.size());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check for the request code, we might be usign multiple startActivityForReslut
        switch (requestCode) {
            case MainActivity.RESULT_PICK_CONTACT:
                AddMsgFragment addMsgFragment = contactPicked(data);
                if (addMsgFragment != null) {
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.frag_future, addMsgFragment)
                            .addToBackStack(TAG_MESSAGES)
                            .commit();
                }

                break;
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private AddMsgFragment contactPicked(Intent data) {
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
            return AddMsgFragment.newInstance(-1, isFuture, mPosOfContact, name, phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
