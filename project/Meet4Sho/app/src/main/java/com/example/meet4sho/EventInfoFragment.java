package com.example.meet4sho;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meet4sho.api.MGCinema;
import com.example.meet4sho.api.MGTime;
import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.TMEvent;
import com.example.meet4sho.api.TMRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class displays all the info pertaining to a specific event/movie when a user
 *      clicks on it from the RecyclerView in the SearchFragment class
 */
public class EventInfoFragment extends Fragment implements View.OnClickListener {

    private EditText edtEventUsrInput;
    private Button btnRegister;
    private Bundle bundle;
    private String id;
    private String url;
    private String username;
    TMEvent event;
    private String lat;
    private String lon;

    TextView tvDescription;
    ImageView ivEventImg;

    /**
     * The database path for event and user.
     * event: pDocRef.
     * user: pDocRefUser.
     *
     * Buffers to hold the data from database.
     */
    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/event_event");
    private DocumentReference pDocRefUser = FirebaseFirestore.getInstance().document("front_end/user_event");
    private ArrayList<ArrayList<String>> eventContainer = new ArrayList<>();
    private ArrayList<String> buffer;
    private ArrayList<String> bufferUser;

    /**
     * cname to check whether this is a movie input or a ticketmaster event input.
     */
    private String cname;

    public boolean valid = false;
    private List<MGTime> movieTimes;
    boolean movieEvent = false;

    public EventInfoFragment() {
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
        View v = inflater.inflate(R.layout.tm_event_info, container, false);
        bundle = this.getArguments();


        id = bundle.getString("id");
        url = bundle.getString("url");
        cname = bundle.getString("cinema_name");
        if(cname==null){
            Log.d("whatwhat","here");
        }
        username = bundle.getString("username");
        event = (TMEvent) bundle.getSerializable("event");


        // the following code get the user name from sharedPref
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String getFromShared = sharedPref.getString(getString(R.string.preference_user_name), "nothing");
        Log.d("user name : ", getFromShared);


        /**
         * Get name, and longitude and latitude of the specific event we clicked on
         */
        String title = bundle.getString("name");
        lon = bundle.getString("lg");
        lat = bundle.getString("lt");


        /**
         * Set the views with the specific event's info
         *      (i.e. Title, description, event times)
         * How we set that information up depends on whether it is a movie event or ticketmaster event
         *      Thus why we have a boolean called movieEvent
         */
        TextView tvTitleEvent = v.findViewById(R.id.tvTitleEvent);
        tvTitleEvent.setText(title);

        tvDescription = v.findViewById(R.id.tvDescriptionEvent);
        movieTimes = (List<MGTime>) bundle.getSerializable("dates");
        if(movieTimes == null){
            movieEvent = false;
        }
        else {
            movieEvent = true;
        }
        String date = bundle.getString("date");
        if(date!=null) {
            if(movieEvent) {
                String description = cname + " Show Times: \n";
                for (MGTime item: movieTimes) {
                    description += item.getStartTime() + "; ";
                }
                System.out.println(description);
                tvDescription.setText(description);
            }
        }

        edtEventUsrInput = v.findViewById(R.id.edtEventUsrInput);

        ivEventImg = v.findViewById(R.id.ivEventImg);
        new TM_EventInfoActivity.DownloadImageTask(ivEventImg).execute(url);


        /**
         * Set up buttons with onClickListeners
         */
        Button btnGoToRes = v.findViewById(R.id.btnGoToRes);
        btnGoToRes.setOnClickListener(this);
        btnRegister = v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        Button btnUserSearch = v.findViewById(R.id.btnUserSearch);
        btnUserSearch.setOnClickListener(this);

        eventCheck(username,id);
        SearchFilter filter = new SearchFilter();
        filter.add("id", id);
        checkExists(id);
        checkExistsUser(username);
        new TMRequest(new TMListener()).execute(filter);

        return v;
    }

    /**
     * Checks whether a user already signed-up
     * Get the document, forcing the SDK to use the offline cache
     */
    public void eventCheck(String name, String eventname){
        DocumentReference docCheck =  pDocRefUser.collection("interest").document(name);
        docCheck.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        ArrayList<String> tmp =(ArrayList<String>) document.getData().get("event_list");
                        Boolean bb = tmp.contains(eventname);
                        if(bb){
                            btnRegister.setBackgroundColor(getResources().getColor(R.color.red_500));
                            Log.d("eventCHECK","here?????????");
                        }
                    }
                } else {
                    Log.d("here", "Cached get failed: ", task.getException());
                }
            }
        });
    }


    /**
     * Function fetches the events data from the database.
     *      includes: userid list and user bio list.
     */
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
                        buffer =(ArrayList<String>) document.getData().get("list");
                        eventContainer.add(buffer);
                        eventContainer.add((ArrayList<String>) document.getData().get("bio"));
                        System.out.println(buffer.get(0));
                    } else {

                        valid = true;
                        buffer = new ArrayList<>();
                        eventContainer.add(buffer);
                        eventContainer.add(new ArrayList<String>());
                        Log.d("doc Reached", "No such document");

                    }

                }else{
                    System.out.println("failed to get!");
                }
            }
        });

    }

    /**
     * Function fetches data from database,
     *      Includes users' event list.
     */
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
                    } else {
                        bufferUser = new ArrayList<>();
                        Log.d("doc Reached", "No such document");
                    }
                }else{
                    System.out.println("failed to get!");
                }
            }
        });

    }

    /**
     * For the event, we need to upload userid and new user bio to corresponding event.
     */
    public void eventUpload(){
        Map<String,Object> dataToSave =  new HashMap<>();
        dataToSave.put("eid",id);
        boolean bbb = buffer.contains(username);
        String bio = edtEventUsrInput.getText().toString();

        System.out.println(buffer.size());
        // this means user not yet registered.
        if(!bbb){
            // not contains.
            buffer.add(buffer.size(),username);
            eventContainer.get(1).add(eventContainer.get(1).size(),bio);
        }else{
            // contains, we need to switch the bio.
            eventContainer.get(1).set(buffer.indexOf(username),bio);
        }
        dataToSave.put("list",buffer);
        dataToSave.put("bio",eventContainer.get(1));
        pDocRef.collection("interest").document(id).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("YES","save success");
                }else{
                    Log.w("NO","save failed");
                }
            }
        });

    }

    /**
     * Upload user's info
     */
    public void userUpload(){
        Map<String,Object> dataToSave =  new HashMap<>();
        dataToSave.put("uid",username);
        boolean bbb = bufferUser.contains(id);
        System.out.println(bufferUser.size());
        if(!bbb){

            bufferUser.add(bufferUser.size(),id);
        }
        dataToSave.put("event_list",bufferUser);
        pDocRefUser.collection("interest").document(username).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("YES","save success");
//                            Intent i = new Intent(getApplicationContext(),loginActivity.class);
//                            // this is where  we  add  data.
//                            startActivity(i);
                }else{
                    Log.w("NO","save failed");
                }
            }
        });

    }


    /**
     * On click events depending on what button the user clicks
     * btnGoToRes: Switch to the RestaurantsFragment fragment that shows all the restaurants near the event's location
     * btnRegister: Sign up user to the event's interested list so that other user's can see that they're
     *              interested in going
     * btnUserSearch: Switch to UserSearchFragment fragment that shows a list of all the user's who have indicated
     *                that they're interested in going to the event
     */
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnGoToRes:
                Bundle b = new Bundle();
                b.putString("lg", lon);
                b.putString("lt", lat);
                RestaurantsFragment resFrag = new RestaurantsFragment();
                resFrag.setArguments(b);
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, resFrag);
                fragmentTransaction.addToBackStack("resFrag");
                fragmentTransaction.commit();
                break;
            case R.id.btnRegister:
                Button btnRegisterTmp = v.findViewById(R.id.btnRegister);
                btnRegisterTmp.setBackgroundColor(getResources().getColor(R.color.red_500));
                eventUpload();
                userUpload();
                break;
            case R.id.btnUserSearch:
                checkExists(id);
                Bundle b_us = new Bundle();
                b_us.putString("username", username);
                b_us.putString("id", id);
                // we need to pass the latest buffer and bio to the next fragment.
                // this is because those info need to be display at onCreateView life cycle.
                // there's no way to fetch these data in a previos life-cycle than that.
                b_us.putStringArrayList("users",buffer);
                b_us.putStringArrayList("userBio",eventContainer.get(1));
                UserSearchFragment usFrag = new UserSearchFragment();
                usFrag.setArguments(b_us);
                FragmentManager fmus = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransactionUserSearch = fmus.beginTransaction();
                fragmentTransactionUserSearch.replace(R.id.displayedView, usFrag);
                fragmentTransactionUserSearch.addToBackStack("usFrag");
                fragmentTransactionUserSearch.commit();
                break;
        }
    }

    /**
     * Function to update the page after TMListener retrieves the event. If this is done during onCreateView
     *      then event will still be null since TMListener has not finished running
     */
    private void updatePage(){
        String classification = "";
        String when = "";
        String[] DateTime = event.getTime().getStartDateTime().split("T");
        if (DateTime.length >= 2) {
            for (int i = 0; i < DateTime.length; i++) {
                when += DateTime[i] + " ";
            }
            when = when + "\n";
        }
        for (int i = 0; i < event.getClassifications().size(); i++) {
            classification += event.getClassifications().get(i).getSegment() + ", " +
                    event.getClassifications().get(i).getGenre() + ", " +
                    event.getClassifications().get(i).getSubgenre();
        }
        String description = event.getVenue().getName() + "\n" +
                event.getVenue().getAddress1() + ", " +
                event.getVenue().getCity() + ", " +
                event.getVenue().getStateCode() + "\n " +
                when +
                classification + "\n" +
                event.getUrl() + "\n" +
                lon + ", " + lat;
        tvDescription.setText(description);
    }

    private class TMListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    event = (TMEvent) events.get(0);
                    updatePage();

                }
            });
        }
    }
}