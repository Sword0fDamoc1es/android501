package com.example.meet4sho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Yelp_LocList extends AppCompatActivity {
    // views
    private LinearLayout llBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yelp_loc_list);
        // instantiate views
        llBackground = (LinearLayout) findViewById(R.id.llBackground);
        // get bundle info
        Bundle extras = getIntent().getExtras();
        String httpResponse = extras.getString("httpResponse");
        JSONObject jsonObject = null;
        // parse json string
        try {
            jsonObject = new JSONObject(httpResponse);
            if (jsonObject != null) {
                JSONArray restaurants = jsonObject.getJSONArray("businesses");
                for (int i = 0; i < restaurants.length(); i++) {
                    // parse json data
                    JSONObject resto = restaurants.getJSONObject(i);
                    String id = resto.getString("id");
                    String name = resto.getString("name");
                    String image_url = resto.getString("image_url");
                    String is_closed = resto.getString("is_closed");
                    String url = resto.getString("url");
                    String review_count = resto.getString("review_count");
                    JSONArray categories = resto.getJSONArray("categories");
                    String rating  = resto.getString("rating");
                    String latitude = resto.getJSONObject("coordinates").getString("latitude");
                    String longitude = resto.getJSONObject("coordinates").getString("longitude");
                    JSONArray transactions = resto.getJSONArray("transactions");
                    String price = resto.getString("price");
                    JSONObject location = resto.getJSONObject("location");
                    String address1 = location.getString("address1");
                    String address2 = location.getString("address2");
                    String address3 = location.getString("address3");
                    String city = location.getString("city");
                    String zip_code = location.getString("zip_code");
                    String country = location.getString("country");
                    String state = location.getString("state");
                    JSONArray disp_address = location.getJSONArray("display_address");
                    String display_address = disp_address.getString(0) + disp_address.getString(1);
                    String phone = resto.getString("phone");
                    String display_phone = resto.getString("display_phone");
                    String distance = resto.getString("distance");
                    // create views & display data
                    TextView tvResto = new TextView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(50,20,50,20);
                    tvResto.setLayoutParams(params);
                    tvResto.setId(i);
                    String description = String.format("name: %s\nrating: %s\nprice: %s\ndistance: %sm\n", name,rating,price,distance);
                    tvResto.setText(description);
                    tvResto.setTextSize(16);
                    tvResto.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    llBackground.addView(tvResto);
                    tvResto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // TODO
                            Intent i = new Intent(getApplicationContext(), Yelp_LocInfo.class);
                            i.putExtra("name", name);
                            i.putExtra("image_url", image_url);
                            i.putExtra("is_closed", is_closed);
                            i.putExtra("url",url);
                            i.putExtra("review_count", review_count);
                            i.putExtra("rating", rating);
                            i.putExtra("latitude", latitude);
                            i.putExtra("longitude",longitude);
                            i.putExtra("price", price);
                            i.putExtra("address1", address1);
                            i.putExtra("address2",address2);
                            i.putExtra("address3", address3);
                            i.putExtra("city", city);
                            i.putExtra("zip_code", zip_code);
                            i.putExtra("country", country);
                            i.putExtra("state", state);
                            i.putExtra("display_address", display_address);
                            i.putExtra("phone", phone);
                            i.putExtra("display_phone", display_phone);
                            i.putExtra("distance", distance);
                            startActivity(i);
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}