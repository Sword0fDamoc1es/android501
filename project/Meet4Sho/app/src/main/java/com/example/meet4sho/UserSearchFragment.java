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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.YelpRequest;
import com.example.meet4sho.api.YelpRestaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays all the user's that are interested in an event/movie
 *      via UserRecyclerAdapter's RecyclerView
 */
public class UserSearchFragment extends Fragment {

    public RecyclerView rvUsers;
    String id;

    public List<String> usernames = new ArrayList<>();
    public List<String> bios = new ArrayList<>();


    public UserRecyclerAdapter ra;

    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/event_event");

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
        usernames = bundle.getStringArrayList("users");
        System.out.println(usernames.size());
        bios = bundle.getStringArrayList("userBio");
        String username = bundle.getString("username");
        
        rvUsers = (RecyclerView) v.findViewById(R.id.rvUsers);
        ra = new UserRecyclerAdapter(getActivity(), usernames, bios,getActivity().getFragmentManager());
        rvUsers.setAdapter(ra);
        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    public void checkExists(String name){
        DocumentReference docCheck =  pDocRef.collection("interest").document(name);
        docCheck.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Log.d("id is:",name);
                    if (document.exists()) {
                        Log.d("GET", "DocumentSnapshot data: " + document.getData().get("eid"));
                        usernames =(ArrayList<String>) document.getData().get("list");
                        System.out.println(usernames.get(2));
                    } else {
                        usernames = new ArrayList<>();
                        Log.d("doc Reached", "No such document");
                    }
                }else{
                    System.out.println("failed to get!");
                }
            }
        });

    }

}