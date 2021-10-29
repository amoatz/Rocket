package com.example.rocket.loaders;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class RegisterQueryUtils {

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
        Integer feedback = extractLocations(jsonResponse);
        return feedback;

    }

    public static Integer extractLocations(String jsonResponse) {

        Integer feedback=0;

        try {
// Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject root = new JSONObject(jsonResponse);
            feedback = root.getInt("customerId");

        } catch (JSONException e) {
// If an error is thrown when executing any of the above statements in the "try" block,
// catch the exception here, so the app doesn't crash. Print a log message
// with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the locations JSON results", e);
        }
// Return
        return feedback;
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
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            String jsonInputString = "{\"name\": \"testname\", \"mail\": \"testmail\"}";
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
            }

//            urlConnection.connect();
//            inputStream = urlConnection.getInputStream();
//            jsonResponse=readFromStream(inputStream);

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                jsonResponse=response.toString();
            }



//            Log.e("CHECKPOINT","The URLConnection in makeHttpRequest is "+urlConnection.toString());
//            urlConnection.connect();

//            if(urlConnection.getResponseCode()==200){
//                inputStream=urlConnection.getInputStream();
//                jsonResponse=readFromStream(inputStream);
//            } else {
//                Log.e("Message","Error response code number  "+urlConnection.getResponseCode());
//            }
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
