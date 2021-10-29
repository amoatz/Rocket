package com.example.rocket.loaders;

import android.util.Log;

import com.example.rocket.classes.CommentWord;
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

public class CommentQueryUtils {

    public static ArrayList<CommentWord> fetchUrlData(String stringUrl){

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
        ArrayList<CommentWord> commentWords = new ArrayList<CommentWord>();
        commentWords = extractLocations(jsonResponse);
        return commentWords;
    }

    public static ArrayList<CommentWord> extractLocations(String jsonResponse) {
// Create an empty ArrayList that we can start
        ArrayList<CommentWord> commentWords = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray reviews = root.getJSONArray("Reviews");
            String success = root.getString("success");
            if (success == "false" || success =="False"){
                return commentWords;
            }
            for (int i =0 ; i<reviews.length();i++){
                // add new review word
                JSONObject reviewObject = reviews.getJSONObject(i);
                String name = reviewObject.getString("author_name");
                float rating = (float) reviewObject.getDouble("rating");
                String commentText = reviewObject.getString("text");

                commentWords.add(new CommentWord (name,commentText,rating));

            }
        } catch (JSONException e) {
// If an error is thrown when executing any of the above statements in the "try" block,
// catch the exception here, so the app doesn't crash. Print a log message
// with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the locations JSON results", e);
        }
// Return the list
        return commentWords;
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
