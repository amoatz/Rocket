package com.example.rocket.loaders;

import android.util.Log;

import com.example.rocket.classes.QuestionWord;

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
import java.util.List;

public class PostAnsQuestionsQueryUtils {

    public static String fetchUrlData(String stringUrl, int mclientId, int locationId,
                                      List<QuestionWord> questionWordList){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(stringUrl);
        String jsonResponse = "";
        try {
            Log.e("CHECKPOINT","The URL in the request is "+url.toString());
            jsonResponse = makeHttpRequest(url,mclientId,locationId,questionWordList);
        } catch (IOException e) {
            Log.e("Message"," ",e);
        }
        String feedback = extractLocations(jsonResponse);
        return feedback;

    }

    public static String extractLocations(String jsonResponse) {

        String feedback;

        try {
// Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject root = new JSONObject(jsonResponse);
            String success = root.getString("success");
            if (success == "false" || success =="False"){
                return "Fail";
            }

        } catch (JSONException e) {
// If an error is thrown when executing any of the above statements in the "try" block,
// catch the exception here, so the app doesn't crash. Print a log message
// with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the locations JSON results", e);
        }
// Return
        return "Success";
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

    private static String makeHttpRequest(URL url,
                                          int clientID,
                                          int locationId,
                                          List<QuestionWord> questionWordList) throws IOException{

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

            String jsonInputString = "{\"customer_id\":\""+clientID
                    +"\",\"location_id\": \"" +locationId
                    +"\",\"questions\": [";
            for (int i=0;i<questionWordList.size()-1;i++){

                if (i !=0 ){
                    jsonInputString = jsonInputString+",";
                }
                jsonInputString = jsonInputString + "{\"question\":\""+questionWordList.get(i).getQuestion()
                        +"\",\"rating\":\""+questionWordList.get(i).getRating()+"\"}";
            }
            jsonInputString = jsonInputString+"]}";

            Log.v("RATING","The string is "+jsonInputString);
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
