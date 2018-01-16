package com.apps.hassan.thaifoodidentifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class myAdapter extends ArrayAdapter<Directory> {

    public myAdapter(Context context, ArrayList<Directory> users) {
        super(context, 0, users);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Directory directory = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_class, parent, false);
        }
        // Lookup view for data population
        TextView classTitle = (TextView) convertView.findViewById(R.id.class_title);
        Button addClass = (Button) convertView.findViewById(R.id.class_add);
        // Populate the data into the template view using the data object
        classTitle.setText(directory.getDirectory_name());
        // Return the completed view to render on screen
        return convertView;
    }
}
