package com.friends.stay.keepintouch;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;


public class MsgRecyclerView {
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Msg> mMessages;

    public MsgRecyclerView(View view, Context context, ArrayList<Msg> messages) {
        mMessages = messages;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.display_messages_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MsgAdapter(mMessages);
        mRecyclerView.setAdapter(mAdapter);
    }

}
