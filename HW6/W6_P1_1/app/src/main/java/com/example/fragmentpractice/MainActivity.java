package com.example.fragmentpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements FirstFragment.onFirstFragmentListener {
    public Button btn1;
    public Button btn2;
//    public Button btn3;
//    public Button btn4;
//    public Fragment fragment1;

    public FirstFragment firstFragment;
    public SecondFragment secondFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // first do fragment manager:
        FragmentManager fm = getSupportFragmentManager();
        firstFragment = (FirstFragment) fm.findFragmentByTag("first");
        if(firstFragment == null){
            firstFragment = new FirstFragment();
            fm.beginTransaction().add(R.id.first_flFragments,firstFragment,"first").commit();

        }
        secondFragment = (SecondFragment) fm.findFragmentByTag("second");
        if(secondFragment == null){
            secondFragment = new SecondFragment();
            fm.beginTransaction().add(R.id.second_flFragments,secondFragment,"second").commit();
        }

//        btn1 = (Button) findViewById(R.id.btn1);
//        btn2 = (Button) findViewById(R.id.btn2);
        // view the created fragement as a class.
//        fragment1 = new FirstFragment();
//        btn3 = (Button) findViewById(R.id.btn3);
//        btn4 = (Button) findViewById(R.id.btn4);
//        fragment1 = (Fragment) findViewById(R.id.fragment1);

//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }


    @Override
    public void messageFromFirstFragment(String text) {
        secondFragment.receive(text);
    }
}