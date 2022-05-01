package com.example.meet4sho;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditText editSettingsEmail;
    private EditText editSettingsPhone;
    private EditText editSettingsPassword;
    private SeekBar sbSettingsDistance;
    private Button btnSettingSave;
    private String username;

    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/user");


    public SettingsFragment() {
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);

//        SharedPreferences.Editor seditr = sharedPref.edit();
//        seditr.putString(getString(R.string.preference_user_name), username);
//        seditr.apply();
        username = sharedPref.getString(getString(R.string.preference_user_name),"");


        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        editSettingsEmail = v.findViewById(R.id.edtSettingsEmail);
        editSettingsPassword = v.findViewById(R.id.edtSettingsPassword);
        editSettingsPhone = v.findViewById(R.id.edtSettingsPhone);
        sbSettingsDistance = v.findViewById(R.id.sbSettingsDistance);
        btnSettingSave = v.findViewById(R.id.btnSettingsSave);

        btnSettingSave.setOnClickListener(this::onClick);

        return v;
    }
    public void onClick(View view){
        // TODO save get distance:
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        SharedPreferences.Editor seditr = sharedPref.edit();
        // change the following into put distance.
//        seditr.putString(getString(R.string.preference_user_name), username);
        seditr.apply();


        Map<String,Object> dataToSaveInfo =  new HashMap<>();
        dataToSaveInfo.put("email",editSettingsEmail.getText().toString());
        dataToSaveInfo.put("phone",editSettingsPhone.getText().toString());

        pDocRef.collection("user-info").document(username).set(dataToSaveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("YES","save success");
                }else{
                    Log.w("NO","save failed");
                }
            }
        });

        Map<String,Object> dataToSave =  new HashMap<>();
        dataToSave.put("uid",username);
        dataToSave.put("upwd",editSettingsPassword.getText().toString());
        pDocRef.collection("user").document(username).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
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

}