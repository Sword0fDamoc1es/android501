package com.example.meet4sho;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {

    private int startingIndex = 0;

    private MessagesFragment messageFrag;
    private SearchFragment searchFrag;
    private ProfileFragment profileFrag;
    private SettingsFragment settingsFrag;

    public View displayedView;
    public FragmentManager fm;
    public String username;

    public EditText searchBar;
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_page);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        messageFrag = new MessagesFragment();
        searchFrag = new SearchFragment();
        profileFrag = new ProfileFragment();
        settingsFrag = new SettingsFragment();

        fm = getFragmentManager ();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        View displayedView = (View) findViewById(R.id.displayedView);

        FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
        ft.add(R.id.displayedView, searchFrag, "searchFrag");  //now we have added our fragment to our Activity programmatically.  The other fragments exist, but have not been added yet.
        ft.addToBackStack ("myFrag1");  //why do we do this?
        ft.commit ();
        bottomNavigationView.setOnItemSelectedListener(item ->  {
            switch(item.getItemId()) {
                case R.id.nav_messages:
                    showMessages();
                    break;
                case R.id.nav_search:
                    showSearch();
                    break;
                case R.id.nav_profile:
                    showProfile();
                    break;
                case R.id.nav_settings:
                    showSettings();
                    break;
            }
            return true;
        });
    }

    public void showMessages() {
        if (messageFrag == null)
            messageFrag = new MessagesFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, messageFrag);
        fragmentTransaction.addToBackStack("messageFrag");
        fragmentTransaction.commit();

    }

    public void showSearch() {
        if (searchFrag == null)
            searchFrag = new SearchFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, searchFrag);
        fragmentTransaction.addToBackStack("searchFrag");
        fragmentTransaction.commit();

    }

    public void showProfile() {
        if (profileFrag == null)
            profileFrag = new ProfileFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, profileFrag);
        fragmentTransaction.addToBackStack("profileFrag");
        fragmentTransaction.commit();

    }

    public void showSettings() {
        if (settingsFrag == null)
            settingsFrag = new SettingsFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, settingsFrag);
        fragmentTransaction.addToBackStack("settingsFrag");
        fragmentTransaction.commit();

    }

}
