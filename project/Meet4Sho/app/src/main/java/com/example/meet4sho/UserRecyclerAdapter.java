package com.example.meet4sho;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.MyViewHolder> {

    List<String> usernames;
    List<String> bios;
    Context context;
    FragmentManager fm;

    public UserRecyclerAdapter(Activity ct, List<String> un, List<String> b, android.app.FragmentManager f){
        context = ct;
        usernames = un;
        bios = b;
        fm = f;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_search_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String user = usernames.get(position);
        holder.tvUsername.setText(user);
        holder.tvBio.setText(bios.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileOtherFragment proFrag = new ProfileOtherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", user);

                proFrag.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, proFrag);
                fragmentTransaction.addToBackStack("eiFrag");
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public void notifyData(List<String> un, List<String> b) {
        Log.d("notifyData ", un.size() + "");
        this.usernames = un;
        this.bios = b;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername, tvBio;
        ImageView ivPFP;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvTitle);
            tvBio = itemView.findViewById(R.id.tvDate);
            ivPFP = itemView.findViewById(R.id.ivPFP);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
