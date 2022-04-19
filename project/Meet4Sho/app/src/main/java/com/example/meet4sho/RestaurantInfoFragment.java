package com.example.meet4sho;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


public class RestaurantInfoFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private OnFragmentInteractionListener mListener;


    public RestaurantInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.yelp_loc_info, container, false);

        ImageView ivRestoImg = (ImageView) v.findViewById(R.id.ivRestoImg);
        TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);

        Bundle bundle = this.getArguments();
        Restaurant restaurant = (Restaurant) bundle.getSerializable("res");
        String description = "Name: " + restaurant.name + "\n";
        description += "Status: " + restaurant.isClosed + "\n";
        description += "Rating: " + restaurant.rating + "\n";
        description += "Review Count: " + restaurant.reviewCount + "\n";
        description += "Price: " + restaurant.price + "\n";
        description += "Distance: " + restaurant.distance + "\n";
        description += "Address: " + restaurant.displayAddress + "\n";
        description += "Phone: " + restaurant.displayPhone + "\n";
        tvDescription.setText(description);

        new TM_EventInfoActivity.DownloadImageTask(ivRestoImg).execute(restaurant.imgURL);


        return v;
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void messageFromChildFragment(Map.Entry<String, ArrayList<String>> temp);
//    }

}