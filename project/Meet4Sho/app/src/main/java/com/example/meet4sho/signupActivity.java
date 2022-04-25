package com.example.meet4sho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.example.meet4sho.messages.MessageWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class signupActivity extends AppCompatActivity {
    //CometChat API Keys
    public String appID = "2078762eaec81f6e"; // Replace with your App ID
    public String region = "us"; // Replace with your App Region ("eu" or "us")
    public String authKey = "b5a91f16fbe450cb26f7584db9f85d3bd75785ed"; //Replace with your Auth Key.
    //

    public EditText signup_name;
    public EditText signup_pwd;
    public Button btnCreate;
    private Boolean valid = false;

    private DocumentReference pDocRef = FirebaseFirestore.getInstance().document("front_end/user");

    // here is my work-flow of signup:
    // onCreate only generate layout.
    // using late binding to bind func to button.
    // onClick should contain two parts:
    //      part1:
    //          check whether  exsists?
    //          using a  SEPERATE algo func  to return  a boolean : <= checkValid.
    //      part2:
    //          if valid:
    //          get  info and save .

    //  details of the above:
    //  how to  check?
    //  version 4/18: use str of name  only. if contains this COLEECTION: invalid; else , create collection based on  name.
    //  TODO: genereate  ID?



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        signup_name = findViewById(R.id.signup_name);
        signup_pwd = findViewById(R.id.signup_pwd);
        btnCreate = findViewById(R.id.btnCreate);
    }

    public void onClickToCreate(View view){
        String uname = signup_name.getText().toString();
        checkValid(uname);


    }
    public void createAccount(){
        String uname = signup_name.getText().toString();

        //CometChat Account Creation
        CreateCometChatUser(uname);

        // get info
        String  upwd = signup_pwd.getText().toString();
        Map<String,Object> dataToSave =  new HashMap<>();
        dataToSave.put("uid",uname);
        dataToSave.put("upwd",upwd);

        Log.d("HERE","end here");
        pDocRef.collection("user").document(uname).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("YES","save success");
                    Intent i = new Intent(getApplicationContext(),loginActivity.class);
                    // this is where  we  add  data.
                    startActivity(i);
                }else{
                    Log.w("NO","save failed");
                }
            }
        });
    }

    public void checkValid(String name){
        DocumentReference docCheck =  pDocRef.collection("user").document(name);
        String checkName = "";
        docCheck.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET", "DocumentSnapshot data: " + document.getData().get("uid"));
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
//                        flag[0] = true;
                        valid = true;
                        createAccount();
                        Log.d("doc Reached", "No such document");
//                        if (!document.getData().get("uid").toString().equals(name)){
////                                valid = true;
//
//
//
////                            Log.d("flag true","valid");
//                        }else{
//                            Log.d("flag false","invalid");
//                        }
                    }
//                    flag[0] = false;
//                    System.out.println("FFFFFFFFFFFFF!");
                }else{
                    System.out.println("failed to get!");
                }
            }
        });
        if (valid){
            createAccount();
        }

    }

    public void CreateCometChatUser(String name) {
        List<String> userIDsInCometChat = new ArrayList<>();
        UsersRequest usersRequest = new UsersRequest.UsersRequestBuilder().build();
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List <User> list) {
                for(User user: list) {
                    userIDsInCometChat.add(user.getUid());
                }
                Log.d("CometChat-Fetch UserIDs", "User list received: " + list.size());
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("CometChat-Fetch UserIDs", "User list fetching failed with exception: " + e.getMessage());
            }
        });
        if(!userIDsInCometChat.contains(name)) {
            User user = new User();
            user.setUid(name); // Replace with your uid for the user to be created.
            user.setName(name);
            CometChat.createUser(user, authKey, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Log.d("CometChat_CreateUser", user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.e("CometChat_CreateUser", e.getMessage());
                }
            });
        }
    }
}