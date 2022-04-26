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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meet4sho.api.RequestListener;
import com.example.meet4sho.api.SearchFilter;
import com.example.meet4sho.api.TMEvent;
import com.example.meet4sho.api.TMRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class EventInfoFragment extends Fragment implements View.OnClickListener {

    // comments from Marv, 4/24
    // TODO in this fragment, we need to create a sign-up button and send id as the key to it.
    //

    private Bundle bundle;
    // TODO create id.
    private String id;
    // END TODO
    private String url;

    private String username;

    TMEvent event;

    private String lat;
    private String lon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private OnFragmentInteractionListener mListener;

    TextView tvDescription;
    ImageView ivEventImg;

    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/event_event");
    private ArrayList<String> buffer;
    public boolean valid = false;
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
        // TODO get id
        id = bundle.getString("id");
//        Log.d("ID INFO: ",id);

        url = bundle.getString("url");

        username = bundle.getString("username");

        event = (TMEvent) bundle.getSerializable("event");

        // END TODO
        // the following code get the user name from sharedPref
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String getFromShared = sharedPref.getString(getString(R.string.preference_user_name), "nothing");
        Log.d("user name : ", getFromShared);

        String title = bundle.getString("name");
        lon = bundle.getString("lg");
        lat = bundle.getString("lt");

//        String location = bundle.getString("Location");
//        String releaseDate = bundle.getString("Release Date");

        TextView tvTitleEvent = v.findViewById(R.id.tvTitleEvent);
        tvTitleEvent.setText(title);

        tvDescription = v.findViewById(R.id.tvDescriptionEvent);

        ivEventImg = v.findViewById(R.id.ivEventImg);
        new TM_EventInfoActivity.DownloadImageTask(ivEventImg).execute(url);

        TextView tvLatLon = v.findViewById(R.id.tvLatLon);
//        tvLatLon.setText(lon+", "+lat + " " + username);

        Button btnGoToRes = v.findViewById(R.id.btnGoToRes);
        btnGoToRes.setOnClickListener(this);

        Button btnRegister = v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        Button btnUserSearch = v.findViewById(R.id.btnUserSearch);
        btnUserSearch.setOnClickListener(this);

        SearchFilter filter = new SearchFilter();
        filter.add("id", id);
        checkExists(id);
        new TMRequest(new TMListener()).execute(filter);

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
                        buffer =(ArrayList<String>) document.getData().get("list");
                        System.out.println(buffer.get(0));
                    } else {

                        valid = true;
                        buffer = new ArrayList<>();
                        Log.d("doc Reached", "No such document");

                    }

                }else{
                    System.out.println("failed to get!");
                }
            }
        });

    }



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
//                setInterest();

                // eid -> string
                // list -> arrayList

                // first fetch.
                // how to fetch: if interested list is null:
                //      create a arraylist<String> for it.
                //


                Map<String,Object> dataToSave =  new HashMap<>();
                dataToSave.put("eid",id);
                boolean bbb = buffer.contains(username);
                System.out.println(buffer.size());
                if(!bbb){

                    buffer.add(buffer.size(),username);
                }
                dataToSave.put("list",buffer);
                pDocRef.collection("interest").document(id).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                // then push.




                break;
            case R.id.btnUserSearch:
                Bundle b_us = new Bundle();
                b_us.putString("username", username);
                b_us.putString("id", id);
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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void messageFromChildFragment(Map.Entry<String, ArrayList<String>> temp);
//    }
    //A function to update the page after TMListener retrieves the event. If this is done during onCreateView
    //then event will still be null since TMListener has not finished running
    private void updatePage(){
        String classification = "";
        String when = "";
        String[] DateTime = event.getTime().getStartDateTime().split("T");
        if(DateTime.length >= 2){
            for(int i = 0; i < DateTime.length; i++){
                when += DateTime[i] + " ";
            }
            when = when + "\n";
        }
        for(int i = 0; i < event.getClassifications().size(); i++){
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
                event.getUrl();
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