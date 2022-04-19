package com.example.meet4sho;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Map<String, ArrayList<String>> movies = new HashMap<String, ArrayList<String>>();
    //private OnFragmentInteractionListener mListener;


    public SearchFragment() {
        // Required empty public constructor
        // Testing Code
        ArrayList<String> movieInfo = new ArrayList<String>();
        movieInfo.add("Out Now");
        movieInfo.add("Fenway");
        movieInfo.add("2:30PM");
        movieInfo.add("3/5");
        movies.put("Batman", movieInfo);
        movies.put("Robin", movieInfo);
        movies.put("Superman", movieInfo);
        movies.put("Wonder Woman", movieInfo);
        movies.put("The Flash", movieInfo);
        movies.put("Aquaman", movieInfo);
        movies.put("Cyborg", movieInfo);
        movies.put("Justice League", movieInfo);
        movies.put("Nightwing", movieInfo);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        LinearLayout listEvents = v.findViewById(R.id.listEvents);
        int counter = 0;
        for (Map.Entry<String,ArrayList<String>> movie : movies.entrySet()) {
            TextView tvMovie = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(50, 20, 50, 20);
            tvMovie.setLayoutParams(params);
            tvMovie.setId(counter);
            ArrayList<String> temp = movie.getValue();
            String description = String.format("Name: %s\nRating: %s\nLocation: %s\nRelease Date: %s\n", movie.getKey(),
                    temp.get(3), temp.get(1), temp.get(0));
            tvMovie.setText(description);
            tvMovie.setTextSize(16);
            listEvents.addView(tvMovie);
            counter++;
            tvMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                    EventInfoFragment eventInfoFrag = new EventInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", movie.getKey());
                    bundle.putString("Rating", temp.get(3));
                    bundle.putString("Location", temp.get(1));
                    bundle.putString("Release Date", temp.get(0));
                    eventInfoFrag.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.csFullLayout, eventInfoFrag, "eventInfoFrag");
                    transaction.addToBackStack ("searchFrag");  //why do we do this?
                    transaction.commit ();

                }
            });
        }
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
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void messageFromParentFragment(Uri uri);
//    }

}