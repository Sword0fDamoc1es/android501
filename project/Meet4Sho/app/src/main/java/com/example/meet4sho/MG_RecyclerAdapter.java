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

import java.util.ArrayList;
import java.util.List;

public class MG_RecyclerAdapter extends RecyclerView.Adapter<MG_RecyclerAdapter.MyViewHolder> {

    // TODO: add id
    List<String> ids;
    // END TODO
    List<String> names;
    List<String> descriptions;
    List<String> imageURLS;
    List<String> longitude;
    List<String> latitude;
    List<String> cinemaNames;
    String username;
    Context context;
    FragmentManager fm;

    // TODO change the constructor, the first parameter will be ids array.

    public MG_RecyclerAdapter(Activity ct, List<String> i, List<String> n, List<String> d, List<String> u, List<String> lt,
            List<String> lg, List<String> cn, android.app.FragmentManager f, String un){
        // TODO: add id
        ids = i;
        // END TODO
        context = ct;
        names = n;
        descriptions = d;
        imageURLS = u;
        longitude = lg;
        latitude = lt;
        cinemaNames = cn;
        username = un;
        fm = f;

    }
    // END TODO

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tm_event_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // TODO get id based on postion:
        String id = ids.get(position);
        // END TODO

        String title = names.get(position);
        String description = descriptions.get(position);
        String url = imageURLS.get(position);
        String lg = longitude.get(position);
        String lt = latitude.get(position);
        String cinemaName = cinemaNames.get(position);
        holder.tvTitle.setText(title);
        holder.tvDescription.setText(description);
        new TM_EventInfoActivity.DownloadImageTask(holder.ivPreview).execute(url);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventInfoFragment eiFrag = new EventInfoFragment();
                Bundle bundle = new Bundle();
//                Intent i = new Intent(context, TM_EventInfoActivity.class);
                // TODO: add id
                bundle.putString("id",id);
                // END TODO.
                bundle.putString("name", title);
                bundle.putString("description", description);
                bundle.putString("url", url);
                bundle.putString("lg", lg);
                bundle.putString("lt", lt);
                bundle.putString("cinema_name", cinemaName);
                bundle.putString("username", username);
                bundle.putStringArrayList("names", (ArrayList<String>) names);
                bundle.putStringArrayList("descriptions", (ArrayList<String>) descriptions);
                bundle.putStringArrayList("urls", (ArrayList<String>) imageURLS);

                eiFrag.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, eiFrag);
                fragmentTransaction.addToBackStack("eiFrag");
                fragmentTransaction.commit();

//                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void notifyData(List<String> names, List<String> descriptions, List<String> imageURLS) {
        Log.d("notifyData ", names.size() + "");
        this.names = names;
        this.descriptions = descriptions;
        this.imageURLS = imageURLS;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDescription;
        ImageView ivPreview;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPreview = itemView.findViewById(R.id.ivPFP);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}