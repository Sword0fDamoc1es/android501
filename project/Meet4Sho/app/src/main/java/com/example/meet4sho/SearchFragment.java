package com.example.meet4sho;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private EditText etName, etLocation;
    private Button btnSearch, btnMore;
    private RecyclerView rvResults;

    private int pageNum = 1;

    private List<String> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();

    private String city;
    private String keyword;

    private TM_RecyclerAdapter ra;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        etName = (EditText) view.findViewById(R.id.edtSearchBar);
        etLocation = (EditText) view.findViewById(R.id.edtSearchCity);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnMore = (Button) view.findViewById(R.id.btnMore);
        rvResults = (RecyclerView) view.findViewById(R.id.rvResults);

//        if(getIntent().getExtras()!=null){
//            extras = getIntent().getExtras();
//            names = extras.getStringArrayList("names");
//            descriptions = extras.getStringArrayList("descriptions");
//            imageURLs = extras.getStringArrayList("urls");
//
//        }
        ra = new TM_RecyclerAdapter(getActivity(), names, descriptions, imageURLs);
        rvResults.setAdapter(ra);
        rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));


        // view actions
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.clear();
                descriptions.clear();
                imageURLs.clear();
                pageNum = 1;
                city = etLocation.getText().toString();
                keyword = etName.getText().toString();
                API_Fetch.Search(ra, names, descriptions, imageURLs,pageNum, city, keyword);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                API_Fetch.Search(ra, names, descriptions, imageURLs,pageNum, city, keyword);
            }
        });
        return view;
    }
}