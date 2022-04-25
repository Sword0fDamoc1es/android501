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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


public class EventInfoFragment extends Fragment implements View.OnClickListener {

    private Bundle bundle;
    // TODO create id.
    private String lat;
    private String lon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private OnFragmentInteractionListener mListener;


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
        bundle = this.getArguments();
        // TODO get id
        String title = bundle.getString("name");
        String description = bundle.getString("description");
        lon = bundle.getString("lg");
        lat = bundle.getString("lt");

//        String location = bundle.getString("Location");
//        String releaseDate = bundle.getString("Release Date");

        TextView tvTitleEvent = v.findViewById(R.id.tvTitleEvent);
        tvTitleEvent.setText(title);

        TextView tvDescription = v.findViewById(R.id.tvDescriptionEvent);
        tvDescription.setText(description);

        TextView tvLatLon = v.findViewById(R.id.tvLatLon);
        tvLatLon.setText(lon+", "+lat);

        Button btnGoToRes = v.findViewById(R.id.btnGoToRes);
        btnGoToRes.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v){
        Bundle b = new Bundle();
        b.putString("lg", lon);
        b.putString("lt", lat);
        RestaurantsFragment resFrag = new RestaurantsFragment();
        resFrag.setArguments(b);
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, resFrag);
        fragmentTransaction.addToBackStack("resFrag");
        fragmentTransaction.commit();
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