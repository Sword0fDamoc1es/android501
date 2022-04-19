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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.TMEvent;
import com.example.meet4sho.api.TMRequest;
import com.example.meet4sho.model.API_Fetch;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Map<String, ArrayList<String>> movies = new HashMap<String, ArrayList<String>>();
    private OnFragmentInteractionListener mListener;

    private EditText edtSearchBar, edtSearchCity;
    private Button btnSearch, btnMore;
    private RecyclerView rvResults;

    private int pageNum = 1;

    private List<String> names = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();
    private List<String> longitude = new ArrayList<>();
    private List<String> latitude = new ArrayList<>();

    private String city;
    private String keyword;

    private TM_RecyclerAdapter ra;


    public SearchFragment() {
        // Required empty public constructor
        // Testing Code
//        ArrayList<String> movieInfo = new ArrayList<String>();
//        movieInfo.add("Out Now");
//        movieInfo.add("Fenway");
//        movieInfo.add("2:30PM");
//        movieInfo.add("3/5");
//        movies.put("Batman", movieInfo);
//        movies.put("Robin", movieInfo);
//        movies.put("Superman", movieInfo);
//        movies.put("Wonder Woman", movieInfo);
//        movies.put("The Flash", movieInfo);
//        movies.put("Aquaman", movieInfo);
//        movies.put("Cyborg", movieInfo);
//        movies.put("Justice League", movieInfo);
//        movies.put("Nightwing", movieInfo);

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
//        LinearLayout listEvents = v.findViewById(R.id.listEvents);
//        int counter = 0;
//        for (Map.Entry<String,ArrayList<String>> movie : movies.entrySet()) {
//            TextView tvMovie = new TextView(getActivity());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(50, 20, 50, 20);
//            tvMovie.setLayoutParams(params);
//            tvMovie.setId(counter);
//            ArrayList<String> temp = movie.getValue();
//            String description = String.format("Name: %s\nRating: %s\nLocation: %s\nRelease Date: %s\n", movie.getKey(),
//                    temp.get(3), temp.get(1), temp.get(0));
//            tvMovie.setText(description);
//            tvMovie.setTextSize(16);
//            listEvents.addView(tvMovie);
//            counter++;
//            tvMovie.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // TODO
//                    mListener.messageFromParentFragment(movie);
//                }
//            });
//        }
        edtSearchBar = (EditText) v.findViewById(R.id.edtSearchBar);
        edtSearchCity = (EditText) v.findViewById(R.id.edtSearchCity);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        btnMore = (Button) v.findViewById(R.id.btnMore);
        rvResults = (RecyclerView) v.findViewById(R.id.rvResults);

//        if(getIntent().getExtras()!=null){
//            extras = getIntent().getExtras();
//            names = extras.getStringArrayList("names");
//            descriptions = extras.getStringArrayList("descriptions");
//            imageURLs = extras.getStringArrayList("urls");
//
//        }
        ra = new TM_RecyclerAdapter(getActivity(), names, descriptions, imageURLs, longitude, latitude, getActivity().getFragmentManager());
        rvResults.setAdapter(ra);
        rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));


        // view actions
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFilter filter = new SearchFilter();
                filter.add("city", edtSearchCity.getText().toString());
                filter.add("keyword", edtSearchBar.getText().toString());
                new TMRequest(new TMListener()).execute(filter);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                SearchFilter filter = new SearchFilter();
                filter.add("city", edtSearchCity.getText().toString());
                filter.add("keyword", edtSearchBar.getText().toString());
                filter.add("page", String.valueOf(pageNum));
                new TMRequest(new TMListener()).execute(filter);
                //API_Fetch.Search(ra, names, descriptions, imageURLs, longitude, latitude, pageNum, city, keyword);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromParentFragment(Map.Entry<String, ArrayList<String>> temp);
    }

    private class TMListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";
                    names = new ArrayList<>();
                    descriptions = new ArrayList<>();
                    imageURLs = new ArrayList<>();
                    longitude = new ArrayList<>();
                    latitude = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        TMEvent event = (TMEvent) events.get(i);
                        names.add(event.getName());
                        descriptions.add(event.getDescription());
                        imageURLs.add(event.getImages().get(0).getUrl());
                        longitude.add(event.getVenue().getLongitude());
                        latitude.add(event.getVenue().getLatitude());
                        output += event.getName() + "\n";
                    }
                    ra = new TM_RecyclerAdapter(getActivity(), names, descriptions, imageURLs, longitude, latitude, getActivity().getFragmentManager());
                    rvResults.setAdapter(ra);
                    rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            });
        }
    }

}