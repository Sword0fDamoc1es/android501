package com.example.meet4sho.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// This is a class that handles TicketMaster API calling
public class TMRequest extends AsyncTask<SearchFilter, Void, Void> {
    private final String TM_API_KEY = "PqFUIagPA9p1AMWB2SeEF9QFyhGnG0P7";
    private RequestListener listener;

    public TMRequest(RequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(SearchFilter... filter) {
        String url = String.format("https://app.ticketmaster.com/discovery/v2/events.json?apikey=%s",TM_API_KEY);
        url += filter[0].toString();
        Log.i("------>", url);

//        // HTTP Request Testing
//         String result = HttpUtils.sendRequestTest(url);
//         listener.updateViews(result);
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


//                JSONObject event_location = JSONUtils.getJSONObject(event, "place");
//                JSONObject event_address = JSONUtils.getJSONObject(event_location, "address");
//                String event_location_address1 = JSONUtils.getString(event_address, "line1");
//                String event_location_address2 = JSONUtils.getString(event_address, "line2");
//                String event_location_address3 = JSONUtils.getString(event_address, "line3");
//                String event_location_city = JSONUtils.getString(JSONUtils.getJSONObject(event_location, "city"), "name");
//                JSONObject event_location_state = JSONUtils.getJSONObject(event_location, "state");
//                String event_location_stateName = JSONUtils.getString(event_location_state, "name");
//                String event_location_stateCode = JSONUtils.getString(event_location_state, "stateCode");
//                JSONObject event_location_country = JSONUtils.getJSONObject(event_location, "country");
//                String event_locaiton_countryName = JSONUtils.getString(event_location_country, "name");
//                String event_locaiton_countryCode = JSONUtils.getString(event_location_country, "countryCode");
//                String event_location_zipcode = JSONUtils.getString(event_location, "postalCode");
//                JSONObject event_location_ll = JSONUtils.getJSONObject(event_location, "location");
//                String event_location_longitude = JSONUtils.getString(event_location_ll, "longitude");
//                String event_location_latitude = JSONUtils.getString(event_location_ll, "latitude");

//                JSONObject event = events.getJSONObject(i);
//                String event_id = event.getString("id");
//                String event_name = event.getString("name");
//                String event_description = event.getString("description");
//                String event_info = event.getString("info");
//                String event_url = event.getString("url");
//                JSONArray event_images = event.getJSONArray("images");
//                for (int j = 0; j < event_images.length(); j++) {
//                    JSONObject event_image = event_images.getJSONObject(j);
//                    String event_image_url = event_image.getString("url");
//                    int event_image_width = event_image.getInt("width");
//                    int event_image_height = event_image.getInt("height");
//                }
//                JSONObject event_dates = event.getJSONObject("dates");
//                String event_start_dateTime = event_dates.getJSONObject("start").getString("dateTime");
//                //String event_startDate = event_dates.getJSONObject("start").getString("localDate");
//                String event_end_dateTime = event_dates.getJSONObject("end").getString("dateTime");
//                boolean event_span = event_dates.getBoolean("spanMultipleDays");
//                String event_status = event_dates.getJSONObject("status").getString("code");
//                JSONArray event_classifications = event.getJSONArray("classifications");
//                for (int k = 0; k < event_classifications.length(); k++) {
//                    JSONObject event_classification = event_classifications.getJSONObject(k);
//                    String event_classification_segment_id = event_classification.getJSONObject("segment").getString("id");
//                    String event_classification_segment_name = event_classification.getJSONObject("segment").getString("name");
//                    String event_classification_genre_id = event_classification.getJSONObject("genre").getString("id");
//                    String event_classification_genre_name = event_classification.getJSONObject("genre").getString("name");
//                    String event_classification_subgenre_id = event_classification.getJSONObject("subGenre").getString("id");
//                    String event_classification_subgenre_name = event_classification.getJSONObject("subGenre").getString("name");
//                }
//                JSONObject event_location = event.getJSONObject("place");
//                String event_location_address1 = event_location.getJSONObject("address").getString("line1");
//                String event_location_address2 = event_location.getJSONObject("address").getString("line2");
//                String event_location_address3 = event_location.getJSONObject("address").getString("line3");
//                String event_location_city = event_location.getJSONObject("city").getString("name");
//                String event_location_stateName = event_location.getJSONObject("state").getString("name");
//                String event_location_stateCode = event_location.getJSONObject("state").getString("stateCode");
//                String event_locaiton_countryName = event_location.getJSONObject("country").getString("name");
//                String event_locaiton_countryCode = event_location.getJSONObject("country").getString("countryCode");
//                String event_location_zipcode = event_location.getString("postalCode");
//                String event_location_longitude = event_location.getJSONObject("location").getString("longitude");
//                String event_location_latitude = event_location.getJSONObject("location").getString("latitude");

                // TESTING
//                String output =   "event_id:                " + event_id + "\n"
//                                + "event_name:              " + event_name + "\n"
//                                + "event_description:       " + event_description + "\n"
//                                + "event_info:              " + event_info + "\n"
//                                + "event_url:               " + event_url + "\n"
//                                + "event_start_dateTime:    " + event_start_dateTime + "\n"
//                                + "event_end_dateTime:      " + event_end_dateTime + "\n"
//                                + "event_span:              " + event_span + "\n"
//                                + "event_status_code:       " + event_status_code + "\n"
//                                + "event_venue_address1:    " + event_venue_address1 + "\n"
//                                + "event_venue_address2:    " + event_venue_address2 + "\n"
//                                + "event_venue_address3:    " + event_venue_address3 + "\n"
//                                + "event_venue_city:        " + event_venue_city + "\n"
//                                + "event_venue_stateName:   " + event_venue_stateName + "\n"
//                                + "event_venue_stateCode:   " + event_venue_stateCode + "\n"
//                                + "event_venue_countryName: " + event_venue_countryName + "\n"
//                                + "event_venue_countryCode: " + event_venue_countryCode + "\n"
//                                + "event_venue_zipcode:     " + event_venue_zipcode + "\n"
//                                + "event_venue_longitude:   " + event_venue_longitude + "\n"
//                                + "event_venue_latitude:    " + event_venue_latitude + "\n";
            }
            listener.updateViews(allEvents);}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
