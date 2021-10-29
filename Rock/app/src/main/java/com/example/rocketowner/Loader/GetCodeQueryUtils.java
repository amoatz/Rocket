package com.example.rocketowner.Loader;

import android.util.Log;

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

public class GetCodeQueryUtils {

    public static Integer fetchUrlData(String stringUrl){

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
        int customerCode = extractLocations(jsonResponse);
        return customerCode;
    }

    public static Integer extractLocations(String jsonResponse) {

//        LocationNames locationNames = new LocationNames("",0,0,"","","");
        try {
            JSONObject root = new JSONObject(jsonResponse);
            int customerCode = root.getInt("Review_id");
            String success = root.getString("success");
            if (success == "false" || success =="False"){
                return 0;
            }
            return customerCode;
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the locations JSON results", e);
        }
        return 0;
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
