package com.friends.stay.keepintouch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by amitropp on 05/08/2017.
 */

public class SettingFragmentTemplates extends Fragment {


    private int MAX_TEPLATES = 10;
    private static final String TAB_NAME = "templates";
    private ArrayList<String> msgTemplate;
    private ListView listView;
    private  ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;
    private View rootView;


    public SettingFragmentTemplates(){
        msgTemplate = new ArrayList<String>();
        msgTemplate.add("Hey <nickname>, How are yoy?");
        msgTemplate.add("whats up <nickname>?");
        msgTemplate.add("<nickname>, I miss you!! \uD83E\uDD17");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fregment_setting_templates, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(TAB_NAME);
        addDBListenter(mDatabase);

        String[] mStringArray = new String[msgTemplate.size()];
        mStringArray = msgTemplate.toArray(mStringArray);

        adapter = new ArrayAdapter<String>(getContext(), R.layout.activity_listview, mStringArray);

        listView = (ListView) rootView.findViewById(R.id.templates_list);
        listView.setAdapter(adapter);
        setupListViewListener();
        return rootView;
    }

    private void setupListViewListener() {

        //on long click - open dialog with delete button

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick (final AdapterView<?> adapter,
                                             View item, int pos, long id) {
                        final int index = pos;
                        CharSequence dl = "Delete";
                        CharSequence ed = "Edit";
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(msgTemplate.get(pos).toString()) //
                                .setNegativeButton(ed, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // TODO
                                        Log.d("firebaseLog", "~~~edit item");
                                    }
                                })
                                .setPositiveButton(dl, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Remove the item within array at position
                                        final String itemToRemoved = msgTemplate.get(index);
                                        Log.d("firebaseLog", "~~~delete item");
                                        removeItemFromFirebase(itemToRemoved);

                                    }
                                });


                        builder.show();
                    }
                });
    }


    public void onButtonClick(Button view) {
        CharSequence can = "Cancel";
        CharSequence add = "Add";
        CharSequence msg = "Add new Template";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);


        final EditText input = new EditText(getActivity());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);
//        final EditText date = new EditText(getActivity());


        input.setHint("Title");
        layout.addView(input);

//        date.setHint("Date");
//        layout.addView(date);

        builder.setView(layout);

//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //showDatePickerDialog((EditText) view);
//                //To show current date in the datepicker
//                Calendar mcurrentDate=Calendar.getInstance();
//                int mYear=mcurrentDate.get(Calendar.YEAR);
//                int mMonth=mcurrentDate.get(Calendar.MONTH);
//                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog mDatePicker =new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
//                        date.setText("" + selectedday + "/" + (selectedmonth+1) + "/" + selectedyear);
//                    }
//                },mYear, mMonth, mDay);
//                mDatePicker.setTitle("Select date");
//                mDatePicker.show();
//            }
//
//        });


        builder.setTitle("Add new Template")
                .setMessage(msg)
                .setPositiveButton(add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // add the item to the cloud
                        Log.d("firebaseLog", "~~~here1");
                        String itemText = input.getText().toString();// + ",\t" + date.getText().toString();
                        mDatabase.push().setValue(itemText);

                    }
                })
                .setNegativeButton(can, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Refresh the adapter
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

        builder.show();


    }

    public void onAddItem(View v) {
        Button addBtn = (Button) rootView.findViewById(R.id.btnAddItem);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick((Button) view);
            }
        });

    }


    private void readItems() {
        File filesDir = getActivity().getFilesDir();
        File todoFile = new File(filesDir, "templates.txt");
        try {
            msgTemplate = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            msgTemplate = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getActivity().getFilesDir();
        File todoFile = new File(filesDir, "templates.txt");
        try {
            FileUtils.writeLines(todoFile, msgTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    private void addDBListenter(DatabaseReference newRef)
    {
        newRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                String newItem = dataSnapshot.getValue(String.class);
                if (newItem != null)
                    Log.d("tagChildAdded", "Value is: " + newItem.split(",")[0]);
                addItemToScreen(newItem);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String item = dataSnapshot.getValue(String.class);
                if (item != null)
                    Log.d("tagChildRemoved", "Value is: " + item.split(",")[0]);
                removeItemFromScreen(item);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tagDataCancelled", "Failed to read value.", error.toException());
            }
        });

    }

    public void addItemToScreen(String newItem) { //TODO call

        // Refresh the adapter

        adapter.notifyDataSetChanged();
        adapter.add(newItem);
        writeItems();
        //scroll to the bottom of the list
        scrollMyListViewToBottom();

    }

    public void removeItemFromScreen(String item){

        msgTemplate.remove(item);
        // Refresh the adapter
        adapter.notifyDataSetChanged();

    }

    private int getIndex(String item){
        for (int pos = 0; pos < msgTemplate.size(); pos++){
            String it = msgTemplate.get(pos);
            if (it.equals(item)){
                return pos;
            }
        }
        return -1;
    }

    //remove the tdlItem given as an argument from the database of firebase
    public void removeItemFromFirebase(final String itemToBeRemoved) {

        final Query query = mDatabase.orderByValue();
        //print item body
        Log.d("logTaskFound", itemToBeRemoved.split(",")[0]);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot task : dataSnapshot.getChildren())
                {
                    String item = task.getValue(String.class);
                    Log.d("logTaskFound", item.split(",")[0]);
                    if (item.equals(itemToBeRemoved))
                    {
                        task.getRef().removeValue();
                        query.removeEventListener(this);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public String getRandomMsgTemplate() {
//        Random random = new Random();
//        int index = random.nextInt(msgTemplate.size()-1);
//        return msgTemplate.get(index);
//    }
//
//    public void addMsgTemplate(String template) {
//        msgTemplate.add(template);
//    }
//
//    public void deleteMsgTemplate(int index) {
//        msgTemplate.remove(index);
//    }
}
