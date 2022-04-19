package com.example.meet4sho;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.MyViewHolder> {

    List<Restaurant> restaurants;
    Context context;
    FragmentManager fm;

    public RestaurantRecyclerAdapter(Activity ct, List<Restaurant> r, android.app.FragmentManager f){
        context = ct;
        restaurants = r;
        fm = f;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.tvTitle.setText(restaurant.name);
        holder.tvDescription.setText(restaurant.displayAddress);
        holder.tvIsOpen.setText("Phone: " + restaurant.displayPhone);
        holder.tvRating.setText("Rating: " + restaurant.rating);
        holder.tvDistance.setText("Distance: " + restaurant.distance);
        new TM_EventInfoActivity.DownloadImageTask(holder.ivPreview).execute(restaurant.imgURL);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantInfoFragment resFrag = new RestaurantInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("res", restaurant);

                resFrag.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, resFrag);
                fragmentTransaction.addToBackStack("eiFrag");
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void notifyData(List<Restaurant> res) {
        Log.d("notifyData ", res.size() + "");
        this.restaurants = res;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvIsOpen, tvRating, tvDistance, tvDescription;
        ImageView ivPreview;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIsOpen = itemView.findViewById(R.id.tvIsOpen);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            ivPreview = itemView.findViewById(R.id.ivPreview);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
