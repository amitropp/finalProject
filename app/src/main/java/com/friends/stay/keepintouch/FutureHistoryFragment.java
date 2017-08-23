package com.friends.stay.keepintouch;

import android.os.Bundle;
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
        mAddMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to new fragment - request to add a new contact
                AddMsgFragment addMsgFragment = new AddMsgFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frag_future, addMsgFragment)
                        .addToBackStack(TAG_MESSAGES)
                        .commit();
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
        mMsgRecyclerView.mAdapter.notifyItemChanged(pos);
    }

    public void updateRecyclerViewOnRemove(int pos) {
        mMsgRecyclerView.mAdapter.notifyItemRemoved(pos);
        mMsgRecyclerView.mAdapter.notifyItemRangeChanged(pos, mAllMsgs.size());
    }

}
