package com.example.meet4sho;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.api.MGCinema;
import com.example.meet4sho.api.MGRequest;
import com.example.meet4sho.api.MGTime;
import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.TMEvent;
import com.example.meet4sho.api.TMRequest;
import com.google.type.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Fragment from where the user can search for events/movies
 */
public class SearchFragment extends Fragment {

    public Map<String, ArrayList<String>> movies = new HashMap<String, ArrayList<String>>();
    private OnFragmentInteractionListener mListener;

    private EditText edtSearchBar, edtSearchCity, edtSearchDate;
    private Button btnSearch, btnMore;
    private Spinner spnCategories;
    private RecyclerView rvResults;

    private double inputLatitude = 42.350444;
    private double inputLongitude = -71.105377;
    private int pageNum = 1;

    private List<String> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();
    private List<String> longitude = new ArrayList<>();
    private List<String> latitude = new ArrayList<>();
    private List<String> cinemaNames = new ArrayList<>();
    private List<List<MGTime>> movieTimes = new ArrayList<>();

    private TM_RecyclerAdapter ra;
    private MG_RecyclerAdapter ta;

    String username;
    private boolean ticketmaster;


    public SearchFragment() {
        // Required empty public constructor
        ticketmaster = true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 1.) Retrieve the username that was passed in a bundle from the login page
     * 2.) Set up views and RecyclerViews
     * 3.) Depending on whether user is searching for events or movies
     *          display that specific RecyclerView
     * 4.) Set up onClick listeners that act differently depending on whether
     *          user is searching for events or movies
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        edtSearchBar = (EditText) v.findViewById(R.id.edtSearchBar);
        edtSearchCity = (EditText) v.findViewById(R.id.edtSearchCity);
        edtSearchDate = (EditText) v.findViewById(R.id.edtSearchDate);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        btnMore = (Button) v.findViewById(R.id.btnMore);
        rvResults = (RecyclerView) v.findViewById(R.id.rvResults);
        spnCategories = (Spinner) v.findViewById(R.id.spnCategories);
        ra = new TM_RecyclerAdapter(getActivity(),ids, names, descriptions, imageURLs, longitude, latitude, getActivity().getFragmentManager(), username);
        ta = new MG_RecyclerAdapter(getActivity(),ids, names, imageURLs, longitude, latitude, cinemaNames, movieTimes, getActivity().getFragmentManager(), username);

        if(ticketmaster)
            rvResults.setAdapter(ra);
        else
            rvResults.setAdapter(ta);
        rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));

        // view actions
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String text = spnCategories.getSelectedItem().toString();
                if(text.equals("Movies")) {
                    ticketmaster = false;
                    SearchFilter filter = new SearchFilter();
                    filter.add("city", edtSearchCity.getText().toString());
                    filter.add("query", edtSearchBar.getText().toString());
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if(!edtSearchDate.getText().toString().equals(""))
                        date = edtSearchDate.getText().toString();
                    filter.add("date", date);
                    findLatAndLon();
                    Log.d("LatAndLon", inputLatitude + " " +inputLongitude);
                    new MGRequest(new MGListener(),inputLatitude,inputLongitude).execute(filter);
                }
                else {
                    ticketmaster = true;
                    SearchFilter filter = new SearchFilter();
                    filter.add("city", edtSearchCity.getText().toString());
                    filter.add("keyword", edtSearchBar.getText().toString());
                    if(!edtSearchDate.getText().toString().equals(""))
                        filter.add("startDateTime", edtSearchDate.getText().toString() + "T00:00:00Z");
                    new TMRequest(new TMListener()).execute(filter);
                }
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                pageNum++;
                String text = spnCategories.getSelectedItem().toString();
                if(text.equals("Movies")) {
                    SearchFilter filter = new SearchFilter();
                    filter.add("city", edtSearchCity.getText().toString());
                    filter.add("keyword", edtSearchBar.getText().toString());
                    findLatAndLon();
                    new MGRequest(new MGListener(),inputLatitude,inputLongitude).execute(filter);
                }
                else {
                    SearchFilter filter = new SearchFilter();
                    filter.add("city", edtSearchCity.getText().toString());
                    filter.add("keyword", edtSearchBar.getText().toString());
                    filter.add("startDateTime", edtSearchDate.getText().toString());
                    filter.add("page", String.valueOf(pageNum));
                    new TMRequest(new TMListener()).execute(filter);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Find the latitude and longitude of the address/city that the user inputted into the edtSearchCity field
     */
    public void findLatAndLon() {
        if (Geocoder.isPresent()) {
            try {
                String location = edtSearchCity.getText().toString();
                Geocoder gc = new Geocoder(getActivity());
                List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        inputLatitude = a.getLatitude();
                        inputLongitude = a.getLongitude();
                    }
                }
            } catch (IOException e) {
                // handle the exception
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void messageFromParentFragment(Map.Entry<String, ArrayList<String>> temp);
    }

    /**
     * Passes in the info that was returned by the TMRequest class into the TM_RecyclerAdapter (RecyclerView)
     * 1.) Reset the ArrayLists
     * 2.) Loop through every TMEvent and add the info from it to each respective ArrayList
     */
    private class TMListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";
                    ids = new ArrayList<>();
                    names = new ArrayList<>();
                    descriptions = new ArrayList<>();
                    imageURLs = new ArrayList<>();
                    longitude = new ArrayList<>();
                    latitude = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        TMEvent event = (TMEvent) events.get(i);
                        ids.add(event.getId());
                        names.add(event.getName());
                        descriptions.add(event.getTime().getStartDateTime());
                        imageURLs.add(event.getImages().get(0).getUrl());
                        longitude.add(event.getVenue().getLongitude());
                        latitude.add(event.getVenue().getLatitude());
                        output += event.getName() + "\n";
                    }
                    ra = new TM_RecyclerAdapter(getActivity(),ids, names, descriptions, imageURLs, longitude, latitude, getActivity().getFragmentManager(), username);
                    rvResults.setAdapter(ra);
                    rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            });
        }
    }

    /**
     * Passes in the info that was returned by the MGRequest class into the MG_RecyclerAdapter (RecyclerView)
     * 1.) Reset the ArrayLists
     * 2.) Loop through every MGCinema and add the info from it to each respective ArrayList
     */
    private class MGListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";
                    ids = new ArrayList<>();
                    names = new ArrayList<>();
                    descriptions = new ArrayList<>();
                    imageURLs = new ArrayList<>();
                    longitude = new ArrayList<>();
                    latitude = new ArrayList<>();
                    cinemaNames = new ArrayList<>();
                    movieTimes = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        MGCinema event = (MGCinema) events.get(i);
                        ids.add(event.getFilm_id());
                        names.add(event.getFilm_name());
                        imageURLs.add(event.getFilm_img());
                        longitude.add(String.valueOf(event.getCinema_lng()));
                        latitude.add(String.valueOf(event.getCinema_lat()));
                        cinemaNames.add(event.getCinema_name());
                        movieTimes.add(event.getTimes());
                        output += event.getFilm_name() + "\n";
                    }
                    ta = new MG_RecyclerAdapter(getActivity(),ids, names, imageURLs, longitude, latitude, cinemaNames, movieTimes, getActivity().getFragmentManager(), username);
                    rvResults.setAdapter(ta);
                    rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            });
        }
    }

}