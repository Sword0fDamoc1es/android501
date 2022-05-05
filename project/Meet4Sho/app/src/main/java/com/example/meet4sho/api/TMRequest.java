package com.example.meet4sho.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class calls the Ticketmaster API and parses all the info into Ticketmaster Objects
 */
public class TMRequest extends AsyncTask<SearchFilter, Void, Void> {

    /**
     * Ticketmaster API constants
     */
    private final String TM_API_KEY = "PqFUIagPA9p1AMWB2SeEF9QFyhGnG0P7";
    private RequestListener listener;

    public TMRequest(RequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(SearchFilter... filter) {
        /**
         * Search for all event's near a location using Ticketmaster's discovery search
         */
        String url = String.format("https://app.ticketmaster.com/discovery/v2/events.json?apikey=%s",TM_API_KEY);
        url += filter[0].toString();
        Log.i("------>", url);

        JSONObject result = HttpUtils.sendRequest(url);
        try {
            JSONArray events;
            if(result != null){
            if(result.getJSONObject("_embedded").optJSONArray("events") != null){
                events = result.getJSONObject("_embedded").getJSONArray("events");
            }else{
                events = new JSONArray();
                events.put(result);
            }
            List<TMEvent> allEvents = new ArrayList<>();
            /**
             * Loop through every returned event near the provided address and
             *      create TMEvent objects
             */
            for (int i=0; i < events.length(); i++) {
                JSONObject event = JSONUtils.getJSONObject(events, i);
                String event_id = JSONUtils.getString(event, "id");
                String event_name = JSONUtils.getString(event, "name");
                String event_description = JSONUtils.getString(event, "description");
                String event_info = JSONUtils.getString(event, "info");
                String event_url = JSONUtils.getString(event, "url");

                JSONArray event_images = JSONUtils.getJSONAray(event, "images");
                List<TMEventImage> event_image_all = new ArrayList<>();
                for (int j = 0; j < event_images.length(); j++) {
                    JSONObject event_image = JSONUtils.getJSONObject(event_images, j);
                    String event_image_url = JSONUtils.getString(event_image, "url");
                    int event_image_width = JSONUtils.getInt(event_image, "width");
                    int event_image_height = JSONUtils.getInt(event_image, "height");
                    event_image_all.add(new TMEventImage(event_image_url, event_image_width, event_image_height));
                }

                JSONObject event_dates = JSONUtils.getJSONObject(event, "dates");
                JSONObject event_start = JSONUtils.getJSONObject(event_dates, "start");
                String event_start_dateTime = JSONUtils.getString(event_start, "dateTime");
                JSONObject event_end = JSONUtils.getJSONObject(event_dates, "end");
                String event_end_dateTime = JSONUtils.getString(event_end, "dateTime");
                boolean event_span = JSONUtils.getBoolean(event_dates, "spanMultipleDays");
                TMEventTime event_time = new TMEventTime(event_start_dateTime, event_end_dateTime, event_span);

                JSONObject event_status= JSONUtils.getJSONObject(event_dates, "status");
                String event_status_code = JSONUtils.getString(event_status, "code");

                /**
                 * Parse through an event's classification info and create a TMEventClassification object from it
                 */
                JSONArray event_classifications = JSONUtils.getJSONAray(event, "classifications");
                List<TMEventClassification> event_classification_all = new ArrayList<>();
                for (int k = 0; k < event_classifications.length(); k++) {
                    JSONObject event_classification = JSONUtils.getJSONObject(event_classifications, k);
                    JSONObject event_classification_segment = JSONUtils.getJSONObject(event_classification, "segment");
                    String event_classification_segment_name = JSONUtils.getString(event_classification_segment, "name");
                    JSONObject event_classification_genre = JSONUtils.getJSONObject(event_classification, "genre");
                    String event_classification_genre_name = JSONUtils.getString(event_classification_genre, "name");
                    JSONObject event_classification_subgenre = JSONUtils.getJSONObject(event_classification, "subGenre");
                    String event_classification_subgenre_name = JSONUtils.getString(event_classification_subgenre, "name");
                    event_classification_all.add(new TMEventClassification(event_classification_segment_name, event_classification_genre_name, event_classification_subgenre_name));
                }

                /**
                 * Parse through an event's venue information and create a TMEventVenue object from it
                 */
                JSONObject event_venue = JSONUtils.getJSONObject(JSONUtils.getJSONAray(JSONUtils.getJSONObject(event, "_embedded"), "venues"), 0);
                String event_venue_name = JSONUtils.getString(event_venue, "name");
                String event_venue_zipcode = JSONUtils.getString(event_venue, "postalCode");
                String event_venue_city = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "city"), "name");
                String event_venue_stateName = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "state"), "name");
                String event_venue_stateCode = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "state"), "stateCode");
                String event_venue_countryName = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "country"), "name");
                String event_venue_countryCode = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "country"), "countryCode");
                JSONObject event_venue_addresss = JSONUtils.getJSONObject(event_venue, "address");
                String event_venue_address1 = JSONUtils.getString(event_venue_addresss, "line1");
                String event_venue_address2 = JSONUtils.getString(event_venue_addresss, "line2");
                String event_venue_address3 = JSONUtils.getString(event_venue_addresss, "line3");
                String event_venue_longitude = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "location"), "longitude");
                String event_venue_latitude = JSONUtils.getString(JSONUtils.getJSONObject(event_venue, "location"), "latitude");
                TMEventVenue venue = new TMEventVenue(event_venue_name,
                        event_venue_address1,
                        event_venue_address2,
                        event_venue_address3,
                        event_venue_city,
                        event_venue_stateName,
                        event_venue_stateCode,
                        event_venue_countryName,
                        event_venue_countryCode,
                        event_venue_zipcode,
                        event_venue_longitude,
                        event_venue_latitude);

                TMEvent tmEvent = new TMEvent(event_id,
                        event_name,
                        event_description,
                        event_info,
                        event_url,
                        event_image_all,
                        event_classification_all,
                        event_time,
                        venue);
                allEvents.add(tmEvent);
            }
            listener.updateViews(allEvents);}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
