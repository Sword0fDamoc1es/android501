package com.example.meet4sho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    public Map<String,String> userNames = new HashMap<String,String>();

    public EditText edtPassword;
    public EditText edtUsername;
    public Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        userNames.put("Test", "123");

        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userNames.containsKey(edtUsername.getText().toString())) {
                    if(userNames.get(edtUsername.getText().toString()).equals(edtPassword.getText().toString())){
                        Intent intent = new Intent(loginActivity.this, AppActivity.class);
                        intent.putExtra("username",edtUsername.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });

    }

}
