package com.example.pol.cardview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pol.ui_custom_elements.IdStringPair;
import com.example.pol.ui_custom_elements.ImageIdAdapter;
import com.example.pol.ui_custom_elements.TodoEditView;
import com.example.pol.utilities.AidStationData;
import com.example.pol.utilities.RaceFileData;
import com.example.pol.utilities.TodoActionsData;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class EditAidStationActivity extends AppCompatActivity {

    private Button saveButton = null;
    private Button addTodoButton = null;
    private LinearLayout mainLayout = null;
    private boolean addEntry = true;
    private int index = -1;
    private TodoEditView todoEditView = null;

    private ArrayList<IdStringPair> idToStringList;
    private final String DEBUG_TAG = "EditAidStationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aid_station);

        todoEditView = new TodoEditView(this);

        // Loading the data if we have received an EXTRA_INDEX
        index = getIntent().getIntExtra(Intent.EXTRA_INDEX, -1);
        if(index >= 0){
            addEntry = false;
            loadStationData();
        }
        // Else we are creating a new aid station
        else{
            addEntry = true;
        }

        idToStringList = new ArrayList<>();
        idToStringList.add(new IdStringPair(R.drawable.action_meditate_mini, getString(R.string.drawable_action_meditate)));
        idToStringList.add(new IdStringPair(R.drawable.action_sleep_baby_mini, getString(R.string.drawable_action_sleep)));

        saveButton = (Button)findViewById(R.id.eas_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
                finish();
            }
        });

        addTodoButton = (Button)findViewById(R.id.eas_add_todo_button);
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpImagePicker(null);
            }
        });

        mainLayout = (LinearLayout)findViewById(R.id.eas_scrollable_layout);

        // Add the final add Todos view to the layout
        mainLayout.addView(todoEditView);

    }

    private void loadStationData(){
        RaceFileData data = RaceFileData.getInstance();
        AidStationData s = data.GetAidStationAtIndex(index);
        setEditTextValue(R.id.eas_name_edit, s.name);
        setEditTextValue(R.id.eas_km_done_edit,""+ s.kmDone);
        setEditTextValue(R.id.eas_dplus_done_edit,""+ s.dPlusDone);
        setEditTextValue(R.id.eas_km_to_next_edit,""+ s.kmToNextStation);
        setEditTextValue(R.id.eas_dplus_to_next_edit,""+ s.dPlusToNextStation);

        // Setting the todos also in the todos layout
        todoEditView.SetAllTheTodos(s.todos);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleSave(){
        // reads all the components of EAS and sets them to a new aidStation data.

        String name = getEditTextValue(R.id.eas_name_edit);
        double kmDone = 0;
        double dpDone = 0;
        double kmNext = 0;
        double dpNext = 0;
        try{
            kmDone = Double.parseDouble(getEditTextValue(R.id.eas_km_done_edit));
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Value for kmDone is Invalid");
        }
        try{
            dpDone = Double.parseDouble(getEditTextValue(R.id.eas_dplus_done_edit));
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Value for dpDone is Invalid");
        }
        try{
            kmNext = Double.parseDouble(getEditTextValue(R.id.eas_km_to_next_edit));
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Value for kmNext is Invalid");
        }
        try{
            dpNext = Double.parseDouble(getEditTextValue(R.id.eas_dplus_to_next_edit));
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Value for dpNext is Invalid");
        }

        RaceFileData data = RaceFileData.getInstance();
        AidStationData s = new AidStationData();
        s.name = name;
        s.kmDone = kmDone;
        s.dPlusDone = dpDone;
        s.dPlusToNextStation = kmNext;
        s.kmToNextStation = dpNext;

        // Saving all the todos
        s.todos = todoEditView.GetAllTheTodos();

        if(addEntry){
            data.AddStation(s);
        }
        else{
            data.ReplaceStation(index, s);
        }

    }


    private String getEditTextValue(int editTextId){
        return ((EditText)findViewById(editTextId)).getText().toString();
    }

    private void setEditTextValue(int editTextId, String value){
        EditText ed = ((EditText)findViewById(editTextId));
        ed.setText(value);
    }



    private Dialog addTodoDialog;
    private void popUpImagePicker(AdapterView.OnItemClickListener onItemClickListener){
        // Create Dialog W/ ListView

        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final ImageIdAdapter adapter = new ImageIdAdapter(this, idToStringList);

        ListView imgView = new ListView(this);
        imgView.setAdapter(adapter);
        builder.setView(imgView);

        if(onItemClickListener == null){
            imgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    IdStringPair pairSelected = adapter.getItem(position);

                    // Launch the next dialog to set the card's trext
                    launchTodoTextDialog(pairSelected, null, null);
                    Log.d(DEBUG_TAG, "Clicked on: " + pairSelected.toString());
                    addTodoDialog.dismiss();
                }
            });
        }
        else{
            imgView.setOnItemClickListener(onItemClickListener);
        }

        builder.setNegativeButton(R.string.eas_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setTitle(R.string.eas_todo_select_image);
        addTodoDialog = builder.create();
        addTodoDialog.show();
    }


    private Dialog textEditDialog;
    private IdStringPair savedPair;
    private void launchTodoTextDialog(IdStringPair pair,String defaultString, DialogInterface.OnClickListener onPositiveClickListener){

        savedPair = pair;
        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate((R.layout.add_todo_text), null));

        if(onPositiveClickListener == null){
            builder.setPositiveButton(R.string.cfd_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // retrieve the value set by the user
                    EditText edit = (EditText)textEditDialog.findViewById(R.id.eas_todo_text_edit);
                    String userText = edit.getText().toString();
                    Log.d(DEBUG_TAG, "Text entered : " + userText);
                    createTodoCard(savedPair.id, userText);
                }
            });
        }
        else{
            builder.setPositiveButton(R.string.cfd_positive, onPositiveClickListener);
        }

        builder.setNegativeButton(R.string.cfd_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle(R.string.eas_todo_enter_text);

        textEditDialog = builder.create();
        textEditDialog.show();
        if (defaultString != null){
            EditText ed = (EditText) textEditDialog.findViewById(R.id.eas_todo_text_edit);
            ed.setText(defaultString);
        }
    }

    private void createTodoCard(int id, String userText){

        ViewGroup todoLayout = (ViewGroup) findViewById(R.id.eas_todo_layout);
        LayoutInflater inflater = getLayoutInflater();
        View cardView = inflater.inflate(R.layout.todo_display_edit, null);

        TextView txt = (TextView) cardView.findViewById(R.id.tde_text_view);
        txt.setText(userText);

        ImageView img = (ImageView) cardView.findViewById(R.id.tde_image);
        img.setImageResource(id);
        // We set a tag here with the id, so as not to create another cusom class
        img.setTag(new Integer(id));

        // Now handle the button actions
        ImageButton editButton = (ImageButton) cardView.findViewById(R.id.tde_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEditTodo(view);
            }
        });

        ImageButton upButton = (ImageButton) cardView.findViewById(R.id.tde_button_up);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup cardFrom = (ViewGroup) view.getParent().getParent();
                ViewGroup todoLayout = (ViewGroup) cardFrom.getParent();
                int indexFrom = todoLayout.indexOfChild(cardFrom);
                int indexTo;
                ViewGroup cardTo;
                // Checking we are not the first
                if(indexFrom >= 1){
                    indexTo = indexFrom - 1;
                    cardTo = (ViewGroup) todoLayout.getChildAt(indexTo);
                    todoLayout.removeView(cardTo);
                    todoLayout.addView(cardTo, indexFrom);
                }
            }
        });

        ImageButton downButton = (ImageButton) cardView.findViewById(R.id.tde_button_down);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup cardFrom = (ViewGroup) view.getParent().getParent();
                ViewGroup todoLayout = (ViewGroup) cardFrom.getParent();
                int indexFrom = todoLayout.indexOfChild(cardFrom);
                int indexTo;
                ViewGroup cardTo;
                // Checking we are not the first
                if(indexFrom < todoLayout.getChildCount() - 1){
                    indexTo = indexFrom + 1;
                    cardTo = (ViewGroup) todoLayout.getChildAt(indexTo);
                    todoLayout.removeView(cardTo);
                    todoLayout.addView(cardTo, indexFrom);
                }

            }
        });

        todoLayout.addView(cardView);
    }


    private ViewGroup itemLayout;
    private String prevStr;
    private void handleEditTodo(View view){
        itemLayout = (ViewGroup)view.getParent();
        TextView tv = (TextView) itemLayout.findViewById(R.id.tde_text_view);
        prevStr = tv.getText().toString();

        popUpImagePicker(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                savedPair = idToStringList.get(i);
                // Launch the next dialog to set the card's trext
                launchTodoTextDialog(savedPair, prevStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // retrieve the value set by the user
                        EditText edit = (EditText)textEditDialog.findViewById(R.id.eas_todo_text_edit);
                        String userText = edit.getText().toString();
                        Log.d(DEBUG_TAG, "Text entered : " + userText);
                        changeTodoCard(itemLayout, savedPair, userText);
                    }
                });
                Log.d(DEBUG_TAG, "Clicked on: " + savedPair.toString());
                addTodoDialog.dismiss();
            }
        });
    }

    private void changeTodoCard(View card, IdStringPair pair, String userText){
        TextView text = (TextView) card.findViewById(R.id.tde_text_view);
        text.setText(userText);
        ImageView img = (ImageView) card.findViewById(R.id.tde_image);
        img.setImageResource(pair.id);
        // We set a tag here with the id, so as not to create another cusom class
        img.setTag(new Integer(pair.id));
    }

}
