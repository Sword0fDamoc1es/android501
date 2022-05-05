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

import com.example.meet4sho.api.MGTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView that displays all the information for every MGCinema Object that we constructed
 *      from the MGRequest class (MovieGlu api call)
 */
public class MG_RecyclerAdapter extends RecyclerView.Adapter<MG_RecyclerAdapter.MyViewHolder> {
    List<String> ids;
    List<String> names;
    List<String> imageURLS;
    List<String> longitude;
    List<String> latitude;
    List<String> cinemaNames;
    List<List<MGTime>> movieTimes;
    String username;
    Context context;
    FragmentManager fm;

    public MG_RecyclerAdapter(Activity ct, List<String> i, List<String> n, List<String> u, List<String> lt,
                              List<String> lg, List<String> cn, List<List<MGTime>> times, android.app.FragmentManager f, String un){
        ids = i;
        context = ct;
        names = n;
        imageURLS = u;
        longitude = lg;
        latitude = lt;
        cinemaNames = cn;
        movieTimes = times;
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

    /**
     * 1.) Get the information from a MGCinema object pertaining to specific position
     *          so that we may pass them into the EventInfoFragment fragment if the
     *          user ends up selecting that cinema
     * 2.) Set a ViewHolder Object's textview to the name of the cinema that pertains to that MGCinema object
     * 3.) Set an on-click event on each ViewHolder so that when a user clicks
     *      on it, it takes them to the EventInfoFragment fragment
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id = ids.get(position);
        String title = names.get(position);
        String url = imageURLS.get(position);
        String lg = longitude.get(position);
        String lt = latitude.get(position);
        String cinemaName = cinemaNames.get(position);
        List<MGTime> movieTime = movieTimes.get(position);
        holder.tvTitle.setText(title);
        String date = movieTime.get(0).getDate();
        holder.tvDate.setText(date);
        holder.tvCinemaName.setText(cinemaName);
        new TM_EventInfoActivity.DownloadImageTask(holder.ivPreview).execute(url);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventInfoFragment eiFrag = new EventInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                bundle.putString("name", title);
                bundle.putString("date", date);
                bundle.putString("url", url);
                bundle.putString("lg", lg);
                bundle.putString("lt", lt);
                bundle.putString("cinema_name", cinemaName);
                bundle.putString("username", username);
                bundle.putStringArrayList("names", (ArrayList<String>) names);
                bundle.putSerializable("dates", (Serializable) movieTime);
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

    public void notifyData(List<String> names, List<List<MGTime>> movieTimes, List<String> imageURLS) {
        Log.d("notifyData ", names.size() + "");
        this.names = names;
        this.movieTimes = movieTimes;
        this.imageURLS = imageURLS;
        notifyDataSetChanged();
    }

    /**
     * An Object that will be used to display every MGCinema in the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDate, tvCinemaName;
        ImageView ivPreview;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            ivPreview = itemView.findViewById(R.id.ivPFP);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}