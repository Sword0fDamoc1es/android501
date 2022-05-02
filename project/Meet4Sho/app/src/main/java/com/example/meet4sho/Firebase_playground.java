package com.example.meet4sho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Firebase_playground extends AppCompatActivity {
    public static final String UID = "uid";
    public static final String UPWD = "upwd";
    public static final String UNAME = "uname";
    public EditText uid;
    public EditText upwd;
    public EditText uname;
    public Button save;
    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("playground/user_test");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_playground);
        uid = (EditText) findViewById(R.id.uid);
        upwd = (EditText) findViewById(R.id.upwd);
        uname = (EditText) findViewById(R.id.uname);
        save = (Button) findViewById(R.id.save);

    }
    public void onClickSave(View view){

        String uidStr = uid.getText().toString();
        String upwdStr = upwd.getText().toString();
        String unameStr = uname.getText().toString();

        Map<String, Object> dataToSave = new HashMap<>();

        dataToSave.put(UID,uidStr);
        dataToSave.put(UPWD,upwdStr);
        dataToSave.put(UNAME,unameStr);

//        pDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d("addSuccess","add success");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w("ERROR","not success");
//            }
//        });

        //
        // Important comments to understand the database:
        // database structure:
        // playground --
        //  ---- user_test     <== this is where pDocRef points to.
        //
        // for the line bellow, it creates a collection from pointer pDocRef.
        // thus:
        // ---- ---- user_test
        // ---- ---- ---- 007
        // ---- ---- ---- :(007's field)

        pDocRef.collection("user_test").document("007").set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
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