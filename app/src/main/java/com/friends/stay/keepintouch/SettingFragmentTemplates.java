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
import java.util.Objects;
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
    private ArrayAdapter<String> adapter;
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
//        msgTemplate.add("Hey <nickname>, How are yoy?");
//        msgTemplate.add("whats up <nickname>?");
//        msgTemplate.add("<nickname>, I miss you!! \uD83E\uDD17");

        String[] mStringArray = new String[msgTemplate.size()];
        readItems();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.activity_listview, msgTemplate);



        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        setupListViewListener();
        addNewTemplate();
        return rootView;
    }

    private void setupListViewListener() {

        //on click - open dialog with delete button

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick (final AdapterView<?> adapter,
                                             View item, int pos, long id) {
                        final int index = pos;
                        Log.d("msgTemplate length", String.valueOf(msgTemplate.size()));
                        Log.d("setOnItemClickListener", String.valueOf(pos));
                        CharSequence dl = "Delete";
                        CharSequence sv = "Save";
                        final CharSequence nn = "<nickname>";

                        final EditText input = new EditText(getActivity());
                        final String originMsg = msgTemplate.get(index);
                        input.append(msgTemplate.get(index));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(input); // uncomment this line

                        builder.setTitle("Edit Template") //
                                .setNegativeButton(dl, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    // Remove the item within array at position
                                    final String itemToRemoved = msgTemplate.get(index);
                                    Log.d("firebaseLog", "~~~delete item");
                                    removeItemFromScreen(itemToRemoved);
                                    dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(sv, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String itemText = input.getText().toString();
                                        Log.d("itemText = ", itemText);
                                        addItemToScreen(itemText);
                                        removeItemFromScreen(originMsg);
                                        Log.d("firebaseLog", "~~~edit item");
                                    }
                                })
                                .setNeutralButton(nn, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        input.append(" <nickname> ");
                                        Log.d("firebaseLog", "~~~add nick name");
                                    }});

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new CustomListener(dialog, input));




                        //prevent <nickname> btn to close the dialog
//                        final AlertDialog dialog = builder.create();
//                        Button b = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//                        b.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //// TODO: 18/08/2017
//                            }
//                        });

                    }
                });
    }


    public void onButtonClick(Button view) {
        CharSequence can = "Cancel";
        CharSequence add = "Add";
        final CharSequence nn = "<nickname>";
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
                .setPositiveButton(add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // add the item to the cloud
                        String itemText = input.getText().toString();// + ",\t" + date.getText().toString();
                        Log.d("itemText = ", itemText);
                        addItemToScreen(itemText);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(can, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Refresh the adapter
//                        String itemText = input.getText().toString();// + ",\t" + date.getText().toString();
//                        removeItemFromScreen(itemText);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(nn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        input.append(" <nickname> ");
                        Log.d("firebaseLog", "~~~add nick name");
                }});

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new CustomListener(dialog, input));


    }

    public void addNewTemplate() {
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
        File dataFile = new File(filesDir, "templates.txt");
        try {
            FileUtils.writeLines(dataFile, msgTemplate);
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
        //add to user templates
        MainActivity.getUser().addTemplate(newItem);

        writeItems();

        //scroll to the bottom of the list
        scrollMyListViewToBottom();

    }

    public void removeItemFromScreen(String item){
        msgTemplate.remove(item);
        //remove to user templates
        MainActivity.getUser().deleteTemplate(item);
        // Refresh the adapter
        adapter.notifyDataSetChanged();
        writeItems();
    }

}

class CustomListener implements View.OnClickListener {
    private final AlertDialog dialog;
    private final EditText input;

    public CustomListener(AlertDialog dialog, EditText input) {
        this.dialog = dialog;
        this.input = input;
    }

    @Override
    public void onClick(View v) {
        input.append("<nickname>");
    }
}