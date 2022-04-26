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


public class UserSearchFragment extends Fragment {

    public RecyclerView rvUsers;
    String id;

    public List<String> usernames = new ArrayList<>();
    public List<String> bios = new ArrayList<>();


    public UserRecyclerAdapter ra;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private OnFragmentInteractionListener mListener;


    public UserSearchFragment() {
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
        View v = inflater.inflate(R.layout.user_search_page, container, false);

        Bundle bundle = this.getArguments();
        id = bundle.getString("id");

        String username = bundle.getString("username");
        usernames.add(username);
        bios.add("");

        rvUsers = (RecyclerView) v.findViewById(R.id.rvUsers);

        ra = new UserRecyclerAdapter(getActivity(), usernames, bios,getActivity().getFragmentManager());
        rvUsers.setAdapter(ra);
        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

}