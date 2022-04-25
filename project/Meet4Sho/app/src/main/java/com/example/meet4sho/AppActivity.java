package com.example.meet4sho;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Map;

public class AppActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener {

    private int startingIndex = 0;

    private MessagesListFragment messageFrag;
    private SearchFragment searchFrag;
    private ProfileFragment profileFrag;
    private SettingsFragment settingsFrag;

    public View displayedView;
    public FragmentManager fm;
    public String username;

    public String appID = "2078762eaec81f6e"; // Replace with your App ID
    public String region = "us"; // Replace with your App Region ("eu" or "us")
    public String authKey = "b5a91f16fbe450cb26f7584db9f85d3bd75785ed"; //Replace with your Auth Key.

    AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();

    public EditText searchBar;
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_page);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        SharedPreferences.Editor seditr = sharedPref.edit();
        seditr.putString(getString(R.string.preference_user_name), username);
        seditr.apply();

        initCometChat();

        messageFrag = new MessagesListFragment();
        searchFrag = new SearchFragment();
        profileFrag = new ProfileFragment();
        settingsFrag = new SettingsFragment();

        fm = getFragmentManager ();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        View displayedView = (View) findViewById(R.id.displayedView);

        FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
        ft.add(R.id.displayedView, searchFrag, "searchFrag");  //now we have added our fragment to our Activity programmatically.  The other fragments exist, but have not been added yet.
        ft.addToBackStack ("searchFrag");  //why do we do this?
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
            messageFrag = new MessagesListFragment();
        initMessagesList();
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
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        profileFrag.setArguments(bundle);
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

    @Override
    public void messageFromParentFragment(Map.Entry<String, ArrayList<String>> temp) {
        EventInfoFragment eventInfoFrag = new EventInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Name", temp.getKey());
        bundle.putString("Rating", temp.getValue().get(3));
        bundle.putString("Location", temp.getValue().get(1));
        bundle.putString("Release Date", temp.getValue().get(0));
        eventInfoFrag.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, eventInfoFrag);
        fragmentTransaction.addToBackStack("eventInfoFrag");
        fragmentTransaction.commit();
    }

    private void initMessagesList() {
        CometChat.login(username, authKey, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Log.d("Comet Chat: ", "Login Successful");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("Comet Chat: ", "Login Failed");
            }
        });
    }

    private void initCometChat() {
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

//    @Override
//    public void messageFromChildFragment(Uri uri) {
//        Log.i("TAG", "received communication from child fragment");
//    }

}
