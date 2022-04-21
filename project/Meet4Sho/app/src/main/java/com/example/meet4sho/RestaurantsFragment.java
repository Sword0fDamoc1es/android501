package com.example.meet4sho;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.YelpRequest;
import com.example.meet4sho.api.YelpRestaurant;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment {

    public EditText edtResSearchBar;
    public Button btnResSearch;
    public Spinner spnResSort;
    public RecyclerView rvResResults;

    public List<YelpRestaurant> restaurants = new ArrayList<>();


    public RestaurantRecyclerAdapter ra;

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
        SearchFilter filter = new SearchFilter();
        filter.add("longitude", bundle.getString("lg"));
        filter.add("latitude", bundle.getString("lt"));
        new YelpRequest(new YelpListener()).execute(filter);

        edtResSearchBar = (EditText) v.findViewById(R.id.edtResSearchBar);
        btnResSearch = (Button) v.findViewById(R.id.btnResSearch);
        spnResSort = (Spinner) v.findViewById(R.id.spnResSort);

        rvResResults = (RecyclerView) v.findViewById(R.id.rvResResults);

        ra = new RestaurantRecyclerAdapter(getActivity(), restaurants, getActivity().getFragmentManager());
        rvResResults.setAdapter(ra);
        rvResResults.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnResSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //restaurants.clear();
                filter.add("longitude", bundle.getString("lg"));
                filter.add("latitude", bundle.getString("lt"));
                if(!edtResSearchBar.getText().toString().equals(""))
                    filter.add("keyword", edtResSearchBar.getText().toString());
                filter.add("sort", spnResSort.getSelectedItem().toString());
                new YelpRequest(new YelpListener()).execute(filter);
                ra = new RestaurantRecyclerAdapter(getActivity(), restaurants, getActivity().getFragmentManager());
                rvResResults.setAdapter(ra);
                rvResResults.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

        return v;
    }

    private class YelpListener implements RequestListener {
        @Override
        public void updateViews(List providedRestaurants) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";
                    restaurants = new ArrayList<>();
                    for (int i = 0; i < providedRestaurants.size(); i++) {
                        YelpRestaurant restaurant = (YelpRestaurant) providedRestaurants.get(i);
                        restaurants.add(restaurant);
                        output += restaurant.getName() + "\n";
                    }
                    ra = new RestaurantRecyclerAdapter(getActivity(), restaurants, getActivity().getFragmentManager());
                    rvResResults.setAdapter(ra);
                    rvResResults.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            });
        }
    }

}