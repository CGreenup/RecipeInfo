package com.chrisgreenup.recipeinfo;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class FdcDownload extends AsyncTask<Void, Void, String> {

    String apiKey;

    FdcDownload(String key){
        apiKey = key;
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();
        String resultString = null;

        Log.i("TESTTT", makeSearchURL("apple test"));

        //TODO HERE: Changes fdcInitialSearch to include inputs from user in the EditText

        try{

            URL url = new URL(makeSearchURL("apple"));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder jsonData = new StringBuilder();
            String line;

            while((line = br.readLine()) != null) {
                jsonData.append(line);
            }

            JSONObject reader = new JSONObject(jsonData.toString());
            JSONArray foods = reader.getJSONArray("foods");
            JSONObject food = foods.getJSONObject(0);

            String id = food.getString("fdcId");

            Log.i("TESTTT", makeFoodIdURL(id));

            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

    private String makeSearchURL(String searchTerms){
        Uri.Builder builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/search").buildUpon();
        builder.appendQueryParameter("api_key", apiKey);
        builder.appendQueryParameter("generalSearchInput", searchTerms);

        return builder.toString();
    }

    private String makeFoodIdURL(String foodId){
        Uri.Builder builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/" + foodId).buildUpon();
        builder.appendQueryParameter("api_key", apiKey);

        return builder.toString();
    }
}
