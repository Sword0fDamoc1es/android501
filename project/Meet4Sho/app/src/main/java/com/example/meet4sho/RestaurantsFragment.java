package com.example.meet4sho;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
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

/**
 * Fragment from where the user can search for restaurants that are near a specific event
 */
public class RestaurantsFragment extends Fragment {

    public EditText edtResSearchBar;
    public Button btnResSearch;
    public Spinner spnResSort;
    public RecyclerView rvResResults;

    public List<YelpRestaurant> restaurants = new ArrayList<>();

    public RestaurantRecyclerAdapter ra;

    public RestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 1.) Retrieve the longitude and latitude of the event/cinema that the user clicked on in the SearchFragment fragment
     * 2.) Set up views
     * 3.) Set RecyclerView of all the restaurants that are nearby
     * 4.) Set onClick listener to take the user to the RestaurantInfoFragment when they select a specific restaurant
     */
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
                    filter.add("term", edtResSearchBar.getText().toString());
                String sort = getSort(spnResSort.getSelectedItem().toString());
                Log.d("Sort: ", sort);
                filter.add("sort_by", sort);
                new YelpRequest(new YelpListener()).execute(filter);
                ra = new RestaurantRecyclerAdapter(getActivity(), restaurants, getActivity().getFragmentManager());
                rvResResults.setAdapter(ra);
                rvResResults.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

        return v;
    }

    /**
     * Set up the sort filter
     */
    private String getSort(String s){
        String sort = "";
        switch(s){
            case "Distance":
                sort = "distance";
                break;
            case "Best Match":
                sort = "best_match";
                break;
            case "Rating":
                sort = "rating";
                break;
            case "Review Count":
                sort = "review_count";
                break;
        }
        return sort;
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