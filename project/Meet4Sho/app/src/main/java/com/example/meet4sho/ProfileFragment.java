package com.example.meet4sho;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {
    public EditText edtBio;
    public TextView tvName;
    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/user");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public ProfileFragment() {
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

        tvName = v.findViewById(R.id.tvName);
        tvName.setText(username);
        edtBio = v.findViewById(R.id.edtBio);
        setDescription();
        Button btnSave = v.findViewById(R.id.btnProfSave);
        btnSave.setOnClickListener(this::onClick);

        return v;
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
        String uid = tvName.getText().toString();
        Map<String,Object> dataToSave =  new HashMap<>();
        String descript = edtBio.getText().toString();
        dataToSave.put("uid",uid);
        dataToSave.put("descript",descript);
        pDocRef.collection("user-bio").document(uid).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
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