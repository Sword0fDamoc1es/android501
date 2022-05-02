package com.example.meet4sho;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    //CometChat API Keys
    public String appID = "2078762eaec81f6e"; // Replace with your App ID
    public String region = "us"; // Replace with your App Region ("eu" or "us")
    public String authKey = "b5a91f16fbe450cb26f7584db9f85d3bd75785ed"; //Replace with your Auth Key.
    //

    public Map<String,String> userNames = new HashMap<String,String>();

    public EditText edtPassword;
    public EditText edtUsername;
    public Button btnLogin;
    public Button btnSignUp;

    // MARVE: comment
    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCometChat();
        setContentView(R.layout.login_page);
        edtPassword = findViewById(R.id.signup_pwd);
        edtUsername = findViewById(R.id.signup_name);
        btnLogin = findViewById(R.id.btnCreate);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),signupActivity.class);
                startActivity(i);
            }
        });



        userNames.put("Test", "123");


//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(userNames.containsKey(edtUsername.getText().toString())) {
//                    if(userNames.get(edtUsername.getText().toString()).equals(edtPassword.getText().toString())){
//                        Intent intent = new Intent(loginActivity.this, AppActivity.class);
//                        intent.putExtra("username",edtUsername.getText().toString());
//                        startActivity(intent);
//                    }
//                }
//            }
//        });

    }
    public void onLogin(View view){
        String uname = edtUsername.getText().toString();
        String upwd = edtPassword.getText().toString();
        DocumentReference docRef = pDocRef.collection("user").document(uname);
        DocumentSnapshot document;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET", "DocumentSnapshot data: " + document.getData().get("uid"));
                        if (document.getData().get("uid").toString().equals(uname)&&document.getData().get("upwd").toString().equals(upwd)){
                            Intent i = new Intent(getApplicationContext(),AppActivity.class);
                            // input here
                            i.putExtra("username",edtUsername.getText().toString());
                            startActivity(i);

                        }
                    } else {
                        Log.d("not GET", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
    }

    private void initCometChat() {
        AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();
        CometChat.init(this, appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("Comet Chat:", "Initialization Successful");
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("Comet Chat:", "Initialization Failed");
            }
        });
    }

}
