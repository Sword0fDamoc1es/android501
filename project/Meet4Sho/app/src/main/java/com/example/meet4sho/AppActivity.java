package com.example.meet4sho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class AppActivity extends AppCompatActivity {

    public EditText searchBar;
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_page);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(item ->  {
//            switch(item.getItemId()) {
//                case R.id.nav_messages:
//                    return;
//                case R.id.nav_search:
//                case R.id.nav_profile:
//                case R.id.nav_settings:
//            }
//            return false;
//        });

        FragmentContainerView ll = (FragmentContainerView) findViewById(R.id.fmSearchFragment);
        EditText tv = (EditText) ll.findViewById(R.id.edtSearchBar);
        //tv.setText("Header Text 1");

    }

}
