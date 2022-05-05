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

import com.example.meet4sho.api.YelpRestaurant;

import java.util.List;

/**
 * RecyclerView that displays all the information for every Restaurant Object that we constructed
 *      from the YelpRequest class (Yelp api call)
 */
public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.MyViewHolder> {

    List<YelpRestaurant> restaurants;
    Context context;
    FragmentManager fm;

    public RestaurantRecyclerAdapter(Activity ct, List<YelpRestaurant> r, android.app.FragmentManager f){
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

    /**
     * 1.) Get the information from a YelpRestaurant object pertaining to specific position
     *          so that we may pass them into the RestaurantInfoFragment fragment if the
     *          user ends up selecting that restaurant
     * 2.) Set a ViewHolder Object's textview to the name of the restaurant that pertains to that YelpRestaurant object
     * 3.) Set an on-click event on each ViewHolder so that when a user clicks
     *      on it, it takes them to the RestaurantInfoFragment fragment
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        YelpRestaurant restaurant = restaurants.get(position);
        System.out.println(restaurant.getName());
        holder.tvTitle.setText(restaurant.getName());
        holder.tvDescription.setText(restaurant.getDisplay_address());
        holder.tvIsOpen.setText("Phone: " + restaurant.getDisplay_phone());
        holder.tvRating.setText("Rating: " + restaurant.getRating());
        holder.tvDistance.setText("Distance: " + restaurant.getDistance());
        new TM_EventInfoActivity.DownloadImageTask(holder.ivPreview).execute(restaurant.getImage_url());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantInfoFragment resFrag = new RestaurantInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("res", restaurant);

                resFrag.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, resFrag);
                fragmentTransaction.addToBackStack("resFrag");
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void notifyData(List<YelpRestaurant> res) {
        Log.d("notifyData ", res.size() + "");
        this.restaurants = res;
        notifyDataSetChanged();
    }

    /**
     * An Object that will be used to display every YelpRestaurant in the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvIsOpen, tvRating, tvDistance, tvDescription;
        ImageView ivPreview;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIsOpen = itemView.findViewById(R.id.tvDate);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            ivPreview = itemView.findViewById(R.id.ivPFP);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
