package com.friends.stay.keepintouch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by user on 8/11/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Contact> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageButton mCallIcon;
        public ImageButton mSmsIcon;
        public ImageButton mWhatsappIcon;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.tv_contact_name);
            mCallIcon = (ImageButton) v.findViewById(R.id.ib_call_icon);
            mSmsIcon = (ImageButton) v.findViewById(R.id.ib_sms_icon);
            mWhatsappIcon = (ImageButton) v.findViewById(R.id.ib_whatsapp_icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Contact> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_contact_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Contact contact = mDataset.get(position);
        holder.mTextView.setText(contact.getName());
        if (contact.isCall()) {
            holder.mCallIcon.setVisibility(View.VISIBLE);
        }
        if (contact.isSMS()) {
            holder.mSmsIcon.setVisibility(View.VISIBLE);
        }
        if (contact.isWatsApp()) {
            holder.mWhatsappIcon.setVisibility(View.VISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
