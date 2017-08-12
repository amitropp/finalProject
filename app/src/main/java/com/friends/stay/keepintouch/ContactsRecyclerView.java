package com.friends.stay.keepintouch;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by user on 8/11/2017.
 */

class ContactsRecyclerView {
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Contact> mContacts;

    public ContactsRecyclerView(View view, Context context, ArrayList<Contact> contacts) {
        mContacts = contacts;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contacts_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(mContacts);
        mRecyclerView.setAdapter(mAdapter);
    }
}
