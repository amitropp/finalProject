package com.friends.stay.keepintouch;

import android.content.Context;
import android.net.Uri;
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
    private boolean isFuture = false;
    private ImageButton mAddMessageBtn;
    private MsgRecyclerView mMsgRecyclerView;

    public static FutureHistoryFragment newInstance(boolean isFuture) {
        FutureHistoryFragment  newFrag = new FutureHistoryFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFuture", isFuture);
        newFrag.setArguments(args);
        return newFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            boolean isFutureFrag = args.getBoolean("isFuture", false);
            if (isFutureFrag) {
                this.isFuture = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //create view only once per instance
        if (myView == null)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_future_history, container, false);
            myView = view;
            mAddMessageBtn = (ImageButton)view.findViewById(R.id.ib_add_message);
            MainActivity mainActivity = (MainActivity)getActivity();
            ArrayList<Msg> msgs;
            if (isFuture) {
                msgs = MainActivity.getUser().getAllFutureMessages();
            }
            else {
                msgs = MainActivity.getUser().getAllHistoryMessages();
                mAddMessageBtn.setVisibility(View.INVISIBLE);
            }
            mMsgRecyclerView = new MsgRecyclerView(view, mainActivity, msgs);
            return view;
        }
        return myView;

    }

}
