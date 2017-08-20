package com.friends.stay.keepintouch;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    public boolean mIsFuture;
    public ArrayList<Msg> mMessages;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mContactName;
        private ImageButton mMsgIcon;
        private TextView mMsgContent;
        private TextView mDate;

        public ViewHolder(View v) {
            super(v);
            mContactName = (TextView)v.findViewById(R.id.tv_msg_to);
            mDate = (TextView)v.findViewById(R.id.tv_msg_date);
            mMsgContent = (TextView)v.findViewById(R.id.tv_msg_content);
            mMsgIcon = (ImageButton) v.findViewById(R.id.ib_message_icon);

            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get the relevant contact by it's position
                    int pos = getAdapterPosition();
                    AddMsgFragment addMsgFrag = AddMsgFragment.newInstance(pos, mIsFuture);
                    final Activity activity = (Activity) v.getContext();

                    if (mIsFuture) {
                        activity.getFragmentManager().beginTransaction()
                                .replace(R.id.frag_future, addMsgFrag)
                                .addToBackStack(FutureHistoryFragment.TAG_MESSAGES)
                                .commit();
                    }

                    else {
                        activity.getFragmentManager().beginTransaction()
                                .replace(R.id.frag_history, addMsgFrag)
                                .addToBackStack(FutureHistoryFragment.TAG_MESSAGES)
                                .commit();
                    }

                }
            });
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MsgAdapter(ArrayList<Msg> myDataset, boolean isFuture) {
        mMessages = myDataset;
        mIsFuture = isFuture;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MsgAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Msg msg = mMessages.get(position);
        holder.mMsgIcon.setImageResource(msg.getIconId());
        holder.mMsgContent.setText(msg.getContent());
        holder.mContactName.setText(msg.getName());
        holder.mDate.setText(convertStringToDate(msg.getDate()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("E, MMM dd HH:mm");
        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }

}
