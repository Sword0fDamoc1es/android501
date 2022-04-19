package com.example.meet4sho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet4sho.model.API_Fetch;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity {
    private EditText etName, etLocation;
    private Button btnSearch, btnMore;
    private RecyclerView rvResults;

    private int pageNum = 1;

    private List<String> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();

    private String city;
    private String keyword;

    private TM_RecyclerAdapter ra;

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


        etName = (EditText) findViewById(R.id.etName);
        etLocation = (EditText) findViewById(R.id.etLocation);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnMore = (Button) findViewById(R.id.btnMore);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);

        if(getIntent().getExtras()!=null){
            extras = getIntent().getExtras();
            names = extras.getStringArrayList("names");
            descriptions = extras.getStringArrayList("descriptions");
            imageURLs = extras.getStringArrayList("urls");

        }
        ra = new TM_RecyclerAdapter(AppActivity.this, names, descriptions, imageURLs);
        rvResults.setAdapter(ra);
        rvResults.setLayoutManager(new LinearLayoutManager(AppActivity.this));


        // view actions
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.clear();
                descriptions.clear();
                imageURLs.clear();
                pageNum = 1;
                city = etLocation.getText().toString();
                keyword = etName.getText().toString();
                API_Fetch.Search(ra, names, descriptions, imageURLs,pageNum, city, keyword);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                API_Fetch.Search(ra, names, descriptions, imageURLs,pageNum, city, keyword);
            }
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
