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


/**
 * Created by amitropp on 05/08/2017.
 */


//TODO - fix add button
//TODO - remove firebase?
//TODO - edit onItem click to have edit option and to actually delete

public class SettingFragmentTemplates extends Fragment {


    private int MAX_TEPLATES = 10;
    private static final String TAB_NAME = "templates";
    private ArrayList<String> msgTemplate;
    private ListView listView;
    private  ArrayAdapter<String> adapter;
    private View rootView;


//    public SettingFragmentTemplates(){
//
//        msgTemplate.add("Hey <nickname>, How are yoy?");
//        msgTemplate.add("whats up <nickname>?");
//        msgTemplate.add("<nickname>, I miss you!! \uD83E\uDD17");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fregment_setting_templates, container, false);

        msgTemplate = new ArrayList<String>();
        msgTemplate.add("Hey <nickname>, How are yoy?");
        msgTemplate.add("whats up <nickname>?");
        msgTemplate.add("<nickname>, I miss you!! \uD83E\uDD17");

        String[] mStringArray = new String[msgTemplate.size()];
        mStringArray = msgTemplate.toArray(mStringArray);
        readItems();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.activity_listview, mStringArray);


        listView = (ListView) rootView.findViewById(R.id.listView);
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
                        Log.d("msgTemplate length", String.valueOf(msgTemplate.size()));
                        Log.d("setOnItemClickListener", String.valueOf(pos));
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
                                        removeItemFromScreen(itemToRemoved);
                                        dialog.dismiss();

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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);


        input.setHint("Title");
        layout.addView(input);

        builder.setView(layout);


        builder.setTitle("Add new Template")
                .setMessage(msg)
                .setPositiveButton(add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // add the item to the cloud
                        Log.d("firebaseLog", "~~~addItemToScreen");
                        String itemText = input.getText().toString();// + ",\t" + date.getText().toString();
                        addItemToScreen(itemText);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(can, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Refresh the adapter
                        Log.d("firebaseLog", "~~~removeItemFromScreen");
//                        String itemText = input.getText().toString();// + ",\t" + date.getText().toString();
//                        removeItemFromScreen(itemText);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

        builder.show();


    }

    public void addNewTemplate(View v) {
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
        File dataFile = new File(filesDir, "templates.txt");
        try {
            msgTemplate = new ArrayList<String>(FileUtils.readLines(dataFile));
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

    public void addItemToScreen(String newItem) { //TODO call

        // Refresh the adapter

        adapter.notifyDataSetChanged();
        adapter.add(newItem);
        writeItems();
        //scroll to the bottom of the list
        scrollMyListViewToBottom();

    }

    public void removeItemFromScreen(String item){
        Log.d("removeItemFromScreen", "~~~1");
        msgTemplate.remove(item);
        Log.d("removeItemFromScreen", "~~~2");
        // Refresh the adapter
        adapter.notifyDataSetChanged();
        writeItems();
        Log.d("removeItemFromScreen", "~~~3");

    }

//    private int getIndex(String item) {
//        for (int pos = 0; pos < msgTemplate.size(); pos++) {
//            String it = msgTemplate.get(pos);
//            if (it.equals(item)) {
//                return pos;
//            }
//        }
//        return -1;
//    }

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
