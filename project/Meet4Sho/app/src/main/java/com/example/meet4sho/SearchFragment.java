package com.example.meet4sho;

import android.content.Context;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.TMEvent;
import com.example.meet4sho.api.TMRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    // 4/24 marv's comments:
    // the TODO is to add id.
    // the parts with <> is the on-going modification on latest-eventID branch.
    // walk through: searchFragment -- <create ID array> -- <get info from TM-event> -- <then send info to TM_recycler>
    //               TM-Recycler -- <create id array> -- <get id in CONSTRUCTOR> -- <add id to bundle in holders' onClick>
    //               EventInfoFragment -- <create id string> -- <get id from bundle>
    // !!!! further goal: once the sign-up button is created in EventInfoFragment:
    // TODO: send id into bundle as a key for the sign-up in eventInfoFragment -> event sign-up page.
    // TODO: In event sign-up page: create a sign-up form and upload <eid> <uid>, <uid> is in a preference file.
    // TODO: return chatroomID.


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Map<String, ArrayList<String>> movies = new HashMap<String, ArrayList<String>>();
    private OnFragmentInteractionListener mListener;

    private EditText edtSearchBar, edtSearchCity;
    private Button btnSearch, btnMore;
    private RecyclerView rvResults;

    private int pageNum = 1;


    // TODO: add private id ?

    private List<String> names = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();
    private List<String> longitude = new ArrayList<>();
    private List<String> latitude = new ArrayList<>();

    private TM_RecyclerAdapter ra;


    public SearchFragment() {
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
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        edtSearchBar = (EditText) v.findViewById(R.id.edtSearchBar);
        edtSearchCity = (EditText) v.findViewById(R.id.edtSearchCity);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        btnMore = (Button) v.findViewById(R.id.btnMore);
        rvResults = (RecyclerView) v.findViewById(R.id.rvResults);

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
                        // TODO add id here.
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