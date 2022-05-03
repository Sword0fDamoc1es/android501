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

public class TM_RecyclerAdapter extends RecyclerView.Adapter<TM_RecyclerAdapter.MyViewHolder> {

    List<String> ids;
    List<String> names;
    List<String> descriptions;
    List<String> imageURLS;
    List<String> longitude;
    List<String> latitude;
    String username;
    Context context;
    FragmentManager fm;

    public TM_RecyclerAdapter(Activity ct, List<String> i, List<String> n, List<String> d, List<String> u,
                              List<String> lg, List<String> lt,android.app.FragmentManager f, String un){
        ids = i;
        context = ct;
        names = n;
        descriptions = d;
        imageURLS = u;
        longitude = lg;
        latitude = lt;
        username = un;
        fm = f;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tm_event_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String id = ids.get(position);
        String title = names.get(position);
        String description = descriptions.get(position).split("T")[0];
        String url = imageURLS.get(position);
        String lg = longitude.get(position);
        String lt = latitude.get(position);
        holder.tvTitle.setText(title);
        holder.tvDate.setText(description);
        new TM_EventInfoActivity.DownloadImageTask(holder.ivPreview).execute(url);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventInfoFragment eiFrag = new EventInfoFragment();
                Bundle bundle = new Bundle();

                bundle.putString("id",id);
                bundle.putString("name", title);
                bundle.putString("description", description);
                bundle.putString("url", url);
                bundle.putString("lg", lg);
                bundle.putString("lt", lt);
                bundle.putString("username", username);
                bundle.putStringArrayList("names", (ArrayList<String>) names);
                bundle.putStringArrayList("descriptions", (ArrayList<String>) descriptions);
                bundle.putStringArrayList("urls", (ArrayList<String>) imageURLS);

                eiFrag.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, eiFrag);
                fragmentTransaction.addToBackStack("eiFrag");
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void notifyData(List<String> id, List<String> names, List<String> descriptions,
                           List<String> imageURLS, List<String> lon, List<String> lat) {
        Log.d("notifyData ", names.size() + "");
        this.ids = id;
        this.names = names;
        this.descriptions = descriptions;
        this.imageURLS = imageURLS;
        this.longitude = lon;
        this.latitude = lat;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDate;
        ImageView ivPreview;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivPreview = itemView.findViewById(R.id.ivPFP);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
