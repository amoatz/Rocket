package com.example.rocket.loaders;

import android.util.Log;

import com.example.rocket.classes.LocationWord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    public static ArrayList<LocationWord> fetchUrlData(String stringUrl){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(stringUrl);
        String jsonResponse = "";
        try {
            Log.e("CHECKPOINT","The URL in the request is "+url.toString());
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Message"," ",e);
        }
        ArrayList<LocationWord> locations = new ArrayList<LocationWord>();
        locations = extractLocations(jsonResponse);
        return locations;
    }

    public static ArrayList<LocationWord> extractLocations(String jsonResponse) {
// Create an empty ArrayList that we can start adding locations to
        ArrayList<LocationWord> locations = new ArrayList<>();
// Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
// is formatted, a JSONException exception object will be thrown.
// Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
// Parse the response given by the SAMPLE_JSON_RESPONSE string and
// build up a list of  objects with the corresponding data.
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray restaurants = root.getJSONArray("restaurants");
            String success = root.getString("success");
            if (success == "false" || success =="False"){
                return locations;
            }
            for (int i =0 ; i<restaurants.length();i++){
                // add new location word
                JSONObject restaurantObject = restaurants.getJSONObject(i);
                String name = restaurantObject.getString("name");
                float rating = (float) restaurantObject.getDouble("rating");

                locations.add(new LocationWord (restaurantObject.getInt("id"),
                        name,
                        restaurantObject.getString("formatted_adress"),
                        restaurantObject.getString("formatted_phone_number"),
                        restaurantObject.getString("geomtry"),
                        restaurantObject.getString("icon"),
                        rating,
                        restaurantObject.getString("website"),
                        restaurantObject.getInt("place_id"),
                        restaurantObject.getInt("one_start"),
                        restaurantObject.getInt("two_start"),
                        restaurantObject.getInt("three_start"),
                        restaurantObject.getInt("four_start"),
                        restaurantObject.getInt("five_start"),
                        restaurantObject.getString("city"),
                        restaurantObject.getString("zone"),
                        restaurantObject.getString("type")));
            }
        } catch (JSONException e) {
// If an error is thrown when executing any of the above statements in the "try" block,
// catch the exception here, so the app doesn't crash. Print a log message
// with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the locations JSON results", e);
        }
// Return the list of locations
        return locations;
    }

    private static URL createUrl (String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e("Message","The Url is wrong ",e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;

        try{
            Log.e("CHECKPOINT","The URL in makeHttpRequest is "+url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            Log.e("CHECKPOINT","The URLConnection in makeHttpRequest is "+urlConnection.toString());
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            } else {
                Log.e("Message","Error response code number  "+urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e("Message"," Error in URL connection",e);
        } finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
