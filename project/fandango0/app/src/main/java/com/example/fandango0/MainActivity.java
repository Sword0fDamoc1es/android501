package com.example.fandango0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fandango0.model.APIresponse;

public class MainActivity extends AppCompatActivity {
    public Button button;
    public TextView textView;
    public String zip = "66062";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponseManager mg = new ResponseManager(MainActivity.this);
                mg.getReplyByZip(listener,zip);
            }
        });
    }
    private final onFetchListener listener = new onFetchListener() {
        @Override
        public void onFetchData(APIresponse apiresponse, String message) {
            if(apiresponse == null){
                Toast.makeText(MainActivity.this,"nothing",Toast.LENGTH_SHORT).show();
                return;
            }
            showData(apiresponse);
        }

        @Override
        public void onError(String message) {

        }
    };
    private void showData(APIresponse apiresponse){
        textView.setText("count: " + apiresponse.getCount());
    }
}