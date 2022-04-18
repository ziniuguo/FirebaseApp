package com.example.firebaseapp.match;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.R;
import com.example.firebaseapp.thread.models.User;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<Users> list;


    public MyAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.matches,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Users user = list.get(position);
        holder.studentid.setText(user.getUserID());
        holder.gender.setText(user.getGender());
        holder.eduLevel.setText(user.getEduLevel());
        holder.tg.setText(user.getTg());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView eduLevel, gender, studentid, tg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eduLevel = itemView.findViewById(R.id.eduLevel);
            gender = itemView.findViewById(R.id.Gender);
            studentid = itemView.findViewById(R.id.StudentID);
            tg = itemView.findViewById(R.id.telegram);
        }
    }

}
