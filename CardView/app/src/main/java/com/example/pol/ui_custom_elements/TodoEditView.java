package com.example.pol.ui_custom_elements;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

import com.example.pol.cardview.R;
import com.example.pol.utilities.TodoActionsData;

import java.util.ArrayList;

/**
 * Created by pol on 12/01/2017.
 */

public class TodoEditView extends LinearLayout {

    private final String DEBUG_TAG = "Todo Edit View";
    private Context context = null;
    private Button addTodo = null;
    private LinearLayout todoCards = null;


    private ArrayList<IdStringPair> idToStringList;

    public TodoEditView(Context context) {
        super(context);

        Log.d(DEBUG_TAG, "Setting up custom todo add / edit view");

        this.context = context;

        setOrientation(LinearLayout.VERTICAL);

        // Add the Ass aid station button, note that we could do it withe an inflater.
        addTodo = new Button(context);
        addTodo.setText(getResources().getString(R.string.tev_add_todo_button));

        addTodo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpImagePicker(null);
            }
        });

        addView(addTodo);

        todoCards = new LinearLayout(context);
        todoCards.setOrientation(LinearLayout.VERTICAL);
        addView(todoCards);

        idToStringList = new ArrayList<>();
        idToStringList.add(new IdStringPair(R.drawable.action_meditate_mini, getResources().getString(R.string.drawable_action_meditate)));
        idToStringList.add(new IdStringPair(R.drawable.action_sleep_baby_mini, getResources().getString(R.string.drawable_action_sleep)));


    }

    public void SetAllTheTodos(ArrayList<TodoActionsData> array){

        // Getting the layout holding all the todos
        int count = array.size();
        for (int i =0 ; i < count ; i ++){
            createTodoCard(array.get(i).id, array.get(i).text);
        }
    }

    public ArrayList<TodoActionsData> GetAllTheTodos(){
        ArrayList<TodoActionsData> array = new ArrayList<>();
        // Going through the layout holding all the data
        int count = todoCards.getChildCount();
        for (int i =0 ; i < count ; i ++){
            View v = todoCards.getChildAt(i);
            TextView text = (TextView) v.findViewById(R.id.tde_text_view);
            if(text == null){
                continue;
            }
            ImageView img = (ImageView) v.findViewById(R.id.tde_image);
            Integer id = (Integer) img.getTag();
            array.add(new TodoActionsData(id.intValue(), text.getText().toString()));
        }

        return  array;
    }

    private Dialog addTodoDialog;
    private void popUpImagePicker(AdapterView.OnItemClickListener onItemClickListener){

            // Create Dialog W/ ListView

            // Create the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            final ImageIdAdapter adapter = new ImageIdAdapter(context, idToStringList);

            ListView imgView = new ListView(context);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

        todoCards.addView(cardView);
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
