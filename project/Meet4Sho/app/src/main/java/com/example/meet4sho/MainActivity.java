package com.example.meet4sho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnMovie;
    private Button btnTicket;
    private Button btnYelp;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMovie = (Button) findViewById(R.id.btnMovie);
        btnTicket = (Button) findViewById(R.id.btnTicket);
        btnYelp = (Button) findViewById(R.id.btnYelp);
        button5 = (Button) findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Firebase_playground.class);
                startActivity(i);
            }
        });

        btnMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AppActivity.class);
                startActivity(i);
            }
        });

        btnYelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Yelp_MainActivity.class);
                startActivity(i);
            }
        });

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TM_MainActivity.class);
                startActivity(i);
            }
        });
    }
}