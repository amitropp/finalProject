package com.friends.stay.keepintouch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by user on 8/11/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public static ArrayList<Contact> mDataset;
    private static Contact mCurEditingContact;
    private static ViewHolder mCurVH;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private ImageButton mCallIcon;
        private ImageButton mSmsIcon;
        private ImageButton mWhatsappIcon;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.tv_contact_name);
            mCallIcon = (ImageButton) v.findViewById(R.id.ib_call_icon);
            mSmsIcon = (ImageButton) v.findViewById(R.id.ib_sms_icon);
            mWhatsappIcon = (ImageButton) v.findViewById(R.id.ib_whatsapp_icon);

            v.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                 // get the relevant contact by it's position
                 int pos = getAdapterPosition();
                 mCurEditingContact = mDataset.get(pos);
                 mCurVH = ContactAdapter.ViewHolder.this;

                 final Activity activity = (Activity) v.getContext();
                 //call a new activity which has 3 tabs: Settings, Future, History
//                 Intent myIntent = new Intent(activity, EditContactActivity.class);
//                 activity.startActivity(myIntent);
                 
                 AddContactFragment addContactFrag = AddContactFragment.newInstance(pos);
                 // raise the addContact for editing the settings of the contact
                 activity.getFragmentManager().beginTransaction()
                         .replace(R.id.frag_contact_list, addContactFrag)
                         .addToBackStack(ContactsListFragment.TAG_CONATCT)
                         .commit();
             }
            });
        }
    }

    public static void updateContactIcons() {
        Contact contact = mCurEditingContact;
        int callVisibility = View.INVISIBLE;
        int smsVisibility = View.INVISIBLE;
        int  whatsappVisibility = View.INVISIBLE;;
        if (contact.isCall()) {
            callVisibility = View.VISIBLE;
        }
        if (contact.isSMS()) {
            smsVisibility = View.VISIBLE;;
        }
        if (contact.isWatsApp()) {
            whatsappVisibility = View.VISIBLE;;
        }
        mCurVH.mCallIcon.setVisibility(callVisibility);
        mCurVH.mSmsIcon.setVisibility(smsVisibility);
        mCurVH.mWhatsappIcon.setVisibility(whatsappVisibility);
    }




    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(ArrayList<Contact> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        int isCallVisibility = View.INVISIBLE;
        int isSmsVisibility = View.INVISIBLE;
        int isWhatsappVisibility = View.INVISIBLE;
        if (contact.isCall()) {
            isCallVisibility = View.VISIBLE;
        }
        if (contact.isSMS()) {
            isSmsVisibility = View.VISIBLE;
        }
        if (contact.isWatsApp()) {
            isWhatsappVisibility = View.VISIBLE;
        }

        holder.mTextView.setText(contact.getName());
        holder.mCallIcon.setVisibility(isCallVisibility);
        holder.mSmsIcon.setVisibility(isSmsVisibility);
        holder.mWhatsappIcon.setVisibility(isWhatsappVisibility);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
