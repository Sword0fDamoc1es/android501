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


/**
 * The main activity where all the other fragments are contained within
 * Sets up the bottom navigation bar as well for traversing the app
 */
public class AppActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener {


    /**
     * Set up class specific variables
     */
    private MessagesListFragment messageFrag;
    private SearchFragment searchFrag;
    private ProfileFragment profileFrag;
    private SettingsFragment settingsFrag;
    public FragmentManager fm;
    public String username;
    public EditText searchBar;
    public BottomNavigationView bottomNavigationView;

    /**
     * CometChat constants that are needed to start CometChat for user messaging
     */
    public String appID = "2078762eaec81f6e"; // Replace with your App ID
    public String region = "us"; // Replace with your App Region ("eu" or "us")
    public String authKey = "b5a91f16fbe450cb26f7584db9f85d3bd75785ed"; //Replace with your Auth Key.
    AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_page);

        /**
         * 1.) Retrieve username from login page and save it to SharedPreferences
         * 2.) Log in a User into Cometchat using the username and authKey
         * 3.) Instantiate the fragments
         * 4.) Set up bottom navigation bar
         * 5.) Initiate Search Fragment so that it's the first view that is displayed upon login
         * 6.) Set up listeners so that fragment switches when user clicks on an icon on the bottom navigation bar
         */
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor seditr = sharedPref.edit();
        seditr.putString(getString(R.string.preference_user_name), username);
        seditr.apply();

        initMessagesList();

        messageFrag = new MessagesListFragment();
        searchFrag = new SearchFragment();
        profileFrag = new ProfileFragment();
        settingsFrag = new SettingsFragment();
        fm = getFragmentManager ();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        searchFrag.setArguments(bundle);
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

    /**
     * Show Message fragment
     */
    public void showMessages() {
        if (messageFrag == null)
            messageFrag = new MessagesListFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, messageFrag);
        fragmentTransaction.addToBackStack("messageFrag");
        fragmentTransaction.commit();

    }

    /**
     * Show Search Fragment
     */
    public void showSearch() {
        if (searchFrag == null)
            searchFrag = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        searchFrag.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, searchFrag);
        fragmentTransaction.addToBackStack("searchFrag");
        fragmentTransaction.commit();

    }

    /**
     * Search Profile fragment
     */
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

    /**
     * Show Settings fragment
     */
    public void showSettings() {
        if (settingsFrag == null)
            settingsFrag = new SettingsFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayedView, settingsFrag);
        fragmentTransaction.addToBackStack("settingsFrag");
        fragmentTransaction.commit();

    }

    /**
     * Retrieve a specific event from the Search fragment
     * and instantiate the eventInfoFrag fragment with the specific event's info
     */
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

    /**
     * Login in user into CometChat so that we may retrieve all the conversations/chats they have
     */
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


}
