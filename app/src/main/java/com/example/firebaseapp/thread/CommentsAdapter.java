package com.example.firebaseapp.thread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.firebaseapp.R;

import java.util.ArrayList;

public class CommentsAdapter extends ArrayAdapter {

    ArrayList<String> list_one;
    ArrayList<String> list_two;

    public CommentsAdapter(Context context, int resource, int textViewResourceId, ArrayList objects1, ArrayList objects2) {
        super(context, resource, textViewResourceId, objects1);
        this.list_one = objects1;
        this.list_two = objects2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listviewcomments, parent, false);
        }


        TextView item1 = convertView.findViewById(R.id.listTextComments);
        TextView item2 = convertView.findViewById(R.id.sublistTextComments);
        // ImageView imageView = (ImageView) v.findViewById(R.id.icon);

        item1.setText(list_one.get(position));
        item2.setText(list_two.get(position));
        // imageView.setImageResource(/*not really sure where you will be getting your image from*/);

        return convertView;
    }
}
