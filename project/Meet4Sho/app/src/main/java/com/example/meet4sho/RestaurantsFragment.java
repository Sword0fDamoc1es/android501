package com.example.meet4sho;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.model.API_Fetch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RestaurantsFragment extends Fragment {

    private RecyclerView rvResResults;

    List<Restaurant> restaurants = new ArrayList<>();

    private RestaurantRecyclerAdapter ra;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private OnFragmentInteractionListener mListener;


    public RestaurantsFragment() {
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
        View v = inflater.inflate(R.layout.restaurants_page, container, false);
        Bundle bundle = this.getArguments();
//        String title = bundle.getString("name");
//        String description = bundle.getString("description");
////        String location = bundle.getString("Location");
////        String releaseDate = bundle.getString("Release Date");
//
//        TextView tvTitleEvent = v.findViewById(R.id.tvTitleEvent);
//        tvTitleEvent.setText(title);
        EditText edtResSearchBar = (EditText) v.findViewById(R.id.edtResSearchBar);
        Button btnResSearch = (Button) v.findViewById(R.id.btnResSearch);
        Spinner spnResSort = (Spinner) v.findViewById(R.id.spnResSort);
//
        RecyclerView rvResResults = v.findViewById(R.id.rvResResults);

        ra = new RestaurantRecyclerAdapter(getActivity(), restaurants, getActivity().getFragmentManager());
        rvResResults.setAdapter(ra);
        rvResResults.setLayoutManager(new LinearLayoutManager(getActivity()));
        API_Fetch.RestaurantSearch(ra, "", "", bundle.getString("lg"), bundle.getString("lt"));

        btnResSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurants.clear();
                String keyword = edtResSearchBar.getText().toString();
                String sort = spnResSort.getSelectedItem().toString();
                API_Fetch.RestaurantSearch(ra, keyword, sort, bundle.getString("lg"), bundle.getString("lt"));
            }
        });

        return v;
    }

}