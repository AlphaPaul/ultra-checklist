package com.example.pol.ui_custom_elements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pol.cardview.R;

import java.util.ArrayList;

/**
 * Created by pol on 23/12/2016.
 */

public class ImageIdAdapter extends ArrayAdapter<IdStringPair> {

    private ArrayList<IdStringPair> list = null;
    private Context ctxt;

    public ImageIdAdapter(Context context, ArrayList<IdStringPair> pairs){
        super(context, R.layout.swipable_button_layout, pairs);
        list = pairs;
        ctxt = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // We need to always override what the view is trying to do.
        // convertView most likely is just the text view.
        LayoutInflater inflater = LayoutInflater.from(ctxt);
        View rowView = inflater.inflate(R.layout.swipable_button_layout, null);

        TextView txt = (TextView)rowView.findViewById(R.id.sb_text);
        txt.setText(list.get(position).description);

        ImageView img = (ImageView)rowView.findViewById(R.id.sb_image);
        img.setImageResource(list.get(position).id);

        return rowView;
    }
}
