package com.cst2335.finalproject;

import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This API is used to retrieve events from the ticketmaster web server.
 */
public class TicketMasterAPI {
    private static String ticketMasterURL="https://app.ticketmaster.com/discovery/v2/events?apikey=NjZGf0D34V8kn07CphoKZPWxQZeiPKYj&radius=%s&unit=km&locale=*&page=%s&city=%s&countryCode=CA";

    /**
     * This method will return a list of TicketmasterEvent. Each page has 20 elements
     * @param city
     * @param radius
     * @param page
     * @return
     */
    public static List<TicketMasterEvent> getEventsFromTicketmaster(String city, String radius, String page)
    {
       List<TicketMasterEvent> result = new ArrayList<>();
        Thread t = new Thread(() -> {
            try {

                URL url = new URL(String.format(ticketMasterURL,radius,page, URLEncoder.encode(city,"UTF-8")));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                String inputString = (new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));
                JSONObject mainObject = new JSONObject(inputString);//this is the root json object
                JSONArray eventArray=mainObject.getJSONObject("_embedded").getJSONArray("events");
                for(int i=0;i<eventArray.length();i++)
                    result.add(eventConverter(eventArray.getJSONObject(i),city));


            }

            catch (Exception e) {
                e.printStackTrace();
            }
        });

        t.start(); // spawn thread

        try {
            t.join();  // wait for thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
    public static int totalNumberOfEvent (String city, String radius)
    {
        AtomicInteger size = new AtomicInteger();
        Thread t = new Thread(() -> {
            try {

                URL url = new URL(String.format(ticketMasterURL,radius,"1", URLEncoder.encode(city,"UTF-8")));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                String inputString = (new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));
                JSONObject mainObject = new JSONObject(inputString);//this is the root json object
                size.set(mainObject.getJSONObject("page").getInt("totalElements"));
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        });

        t.start(); // spawn thread

        try {
            t.join();  // wait for thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     return size.get();
    }
    public static int totalNumberOfPages(String city, String radius)
    {
        return (int)Math.ceil((double)totalNumberOfEvent(city,radius)/20.0);
    }

    /**
     * used to retrieve the image from web and term it into a drawable object
     * @param url
     * @return
     */
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private static TicketMasterEvent eventConverter(JSONObject event,String city) throws JSONException {
        TicketMasterEvent result = new TicketMasterEvent();
        result.setName(event.getString("name"));
        result.setEventUrl(event.getString("url"));
        result.setID(event.getString("id"));
        result.setDate(event.getJSONObject("dates").getJSONObject("start").getString("localDate"));
        result.setTime(event.getJSONObject("dates").getJSONObject("start").getString("localTime"));
        try {
            result.setMinPrice(((JSONObject) event.getJSONArray("priceRanges").get(0)).getString("min"));
            result.setMaxPrice(((JSONObject) event.getJSONArray("priceRanges").get(0)).getString("max"));
        } catch (JSONException e) {
            result.setMaxPrice("N/A");
            result.setMinPrice("N/A");
        }


        result.setCity(city);
        result.setImageUrl(((JSONObject)event.getJSONArray("images").get(0)).getString("url"));
        return result;
    }
}
