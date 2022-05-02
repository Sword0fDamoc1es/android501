package com.example.yelpapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class ThirdActivity extends AppCompatActivity {
    // views
    private ImageView ivRestoImg;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        // instantiate views
        ivRestoImg = (ImageView) findViewById(R.id.ivRestoImg);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        // get bundle info
        Bundle extras = getIntent().getExtras();
        String image_url = extras.getString("image_url");
        String name = extras.getString("name");
        String is_closed = extras.getString("is_closed");
        String url = extras.getString("url");
        String review_count = extras.getString("review_count");
        String rating = extras.getString("rating");
        String price = extras.getString("price");
        String display_address = extras.getString("display_address");
        String display_phone = extras.getString("display_phone");
        String distance = extras.getString("distance");
        // render views
        String description = "Name: " + name + "\n";
        description += "Status: " + is_closed + "\n";
        description += "Rating: " + rating + "\n";
        description += "Review Count: " + review_count + "\n";
        description += "Price: " + price + "\n";
        description += "Distance: " + distance + "\n";
        description += "Address: " + display_address + "\n";
        description += "Phone: " + display_phone + "\n";
        tvDescription.setText(description);
        new DownloadImageTask(ivRestoImg).execute(image_url);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {this.bmImage = bmImage;}

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }
}
