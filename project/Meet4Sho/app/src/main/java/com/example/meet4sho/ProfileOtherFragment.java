package com.example.meet4sho;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.TMEvent;
import com.example.meet4sho.api.TMRequest;

import com.cometchat.pro.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment displays all the info pertaining to another user
 *      (i.e. their profile page)
 *      (Contains their bio, name, and a recyclerView of all their interested events)
 *      (Similar to the ProfileFragment fragment only here there's a Text button which
 *        if the logged in user clicks on it, it'll take them to a chat-room with the user
 *        who's profile they were just on)
 */
public class ProfileOtherFragment extends Fragment {
    private EditText edtBio;
    private TextView tvName;
    private RecyclerView rvInterestedEvents;

    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/user");
    private DocumentReference pDocRefUser = FirebaseFirestore.getInstance().document("front_end/user_event");
    private ArrayList<String> bufferUser;

    private String otherUser;


    private List<String> ids;
    private List<String> names;
    private List<String> descriptions;
    private List<String> imageURLs;
    private List<String> longitude;
    private List<String> latitude;

    private TM_RecyclerAdapter ra;

    public ProfileOtherFragment() {
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
        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");
        View v = inflater.inflate(R.layout.profile_page, container, false);

        ids = new ArrayList<>();
        names = new ArrayList<>();
        descriptions = new ArrayList<>();
        imageURLs = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();

        tvName = v.findViewById(R.id.tvName);
        tvName.setText(username);
        otherUser = username;

        edtBio = v.findViewById(R.id.edtBio);
        setDescription();
        edtBio.setFocusable(false);
        edtBio.setEnabled(false);

        Button btnSave = v.findViewById(R.id.btnProfSave);
        btnSave.setVisibility(View.GONE);

        Button btnText = v.findViewById(R.id.btnText);
        btnText.setOnClickListener(this::onClick);

        rvInterestedEvents = v.findViewById(R.id.rvInterestedEvents);

        ra = new TM_RecyclerAdapter(getActivity(),ids, names, descriptions, imageURLs, longitude, latitude, getActivity().getFragmentManager(), username);
        rvInterestedEvents.setAdapter(ra);
        rvInterestedEvents.setLayoutManager(new LinearLayoutManager(getActivity()));

        checkExistsUser(username);

        return v;
    }

    public void checkExistsUser(String name){
        // input name, will be username
        DocumentReference docCheck =  pDocRefUser.collection("interest").document(name);
        docCheck.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Log.d("id is:",name);
                    if (document.exists()) {
                        Log.d("GET", "DocumentSnapshot data: " + document.getData().get("uid"));
                        bufferUser =(ArrayList<String>) document.getData().get("event_list");

                        for(int i = 0; i < bufferUser.size(); i++) {
                            SearchFilter filter = new SearchFilter();
                            filter.add("id", bufferUser.get(i));
                            new TMRequest(new TMListener()).execute(filter);
                        }

                    } else {

//                        valid = true;
                        bufferUser = new ArrayList<>();
                        Log.d("doc Reached", "No such document");

                    }

                }else{
                    System.out.println("failed to get!");
                }
            }
        });

    }

    public void setDescription(){
        String uid = tvName.getText().toString();
        DocumentReference docCheck =  pDocRef.collection("user-bio").document(uid);
        System.out.println("profile here "+uid);
        docCheck.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        edtBio.setText(document.getData().get("descript").toString());
                        System.out.print("description success");
                    }
                }else{
                    System.out.println("failed to get!");
                }
            }
        });
    }

    public void onClick(View view){
        MessageChatFragment chatRoom = new MessageChatFragment();
        Bundle bundle = new Bundle();
//                Intent i = new Intent(context, TM_EventInfoActivity.class);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        String currentLoggedInUsername = sharedPref.getString(getString(R.string.preference_user_name),"");

        bundle.putString("Chat ID", currentLoggedInUsername + "_user_" + otherUser);
        Log.d("Chat ID", currentLoggedInUsername + "_user_" + otherUser);
        bundle.putString("Recipient", otherUser);


        chatRoom.setArguments(bundle);

        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, chatRoom);
        fragmentTransaction.addToBackStack("chatRoomFrag");
        fragmentTransaction.commit();

    }

    private class TMListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";

                    for (int i = 0; i < events.size(); i++) {
                        TMEvent event = (TMEvent) events.get(i);
                        // TODO add id here.
                        ids.add(event.getId());
                        // END TODO.
                        names.add(event.getName());
                        descriptions.add(event.getTime().getStartDateTime());
                        imageURLs.add(event.getImages().get(0).getUrl());
                        longitude.add(event.getVenue().getLongitude());
                        latitude.add(event.getVenue().getLatitude());
                        output += event.getName() + "\n";
                    }
                    ra.notifyData(ids, names, descriptions, imageURLs, longitude, latitude);
                }
            });
        }
    }
}