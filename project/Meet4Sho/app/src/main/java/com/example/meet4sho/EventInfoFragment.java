package com.example.meet4sho;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class EventInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public EventInfoFragment() {
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
        View v = inflater.inflate(R.layout.tm_event_info, container, false);
        Bundle bundle = this.getArguments();
        String movieName = bundle.getString("Name");
        String rating = bundle.getString("Rating");
        String location = bundle.getString("Location");
        String releaseDate = bundle.getString("Release Date");

        TextView tvTitleEvent = v.findViewById(R.id.tvTitleEvent);
        tvTitleEvent.setText(movieName);

        TextView tvDescription = v.findViewById(R.id.tvDescriptionEvent);
        tvDescription.setText(location);

        return v;
    }

}