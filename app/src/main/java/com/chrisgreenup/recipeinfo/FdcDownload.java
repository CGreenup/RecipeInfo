package com.chrisgreenup.recipeinfo;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;



public class FdcDownload extends AsyncTask<Void, Void, String> {

    private FileInputStream fis;
    private String apiKey;

    FdcDownload(String key, FileInputStream fileInputStream){
        apiKey = key;
        fis = fileInputStream;
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();
        String resultString = null;

        //TODO HERE: Changes fdcInitialSearch to include inputs from user in the EditText

        //findInformationOf("apple");
        readUserInput();

        return result.toString();

    }

    private String readUserInput(){
        String val = "";
        Scanner scanner = new Scanner(fis);

        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNext()) {
            val = scanner.nextLine();
            Log.i("TESTTT", val);
            findInformationOf(val);
            //stringBuilder.append(val);
            //stringBuilder.append("\n");
        }

        return val;
    }

    private void findInformationOf(String searchTerms) {
        try{

            //Open the database by making a URL
            URL url = new URL(makeSearchURL(searchTerms));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //Initialise things to read the JSON data
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder jsonData = new StringBuilder();
            String line;

            //gather all of the JSON data
            while((line = br.readLine()) != null) {
                jsonData.append(line);
            }

            //Dump the JSON data into a JSON object
            //In this data, find the array labeled "foods"
            //In this array, take the first object
            JSONObject reader = new JSONObject(jsonData.toString());
            JSONArray foods = reader.getJSONArray("foods");
            JSONObject food = foods.getJSONObject(0);

            //Put the id of this object into a string in order to find its nutritional data
            String id = food.getString("fdcId");

            //Go to this food's specific information url
            makeFoodIdURL(id);

            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String makeSearchURL(String searchTerms){
        Uri.Builder builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/search").buildUpon();
        builder.appendQueryParameter("api_key", apiKey);
        builder.appendQueryParameter("generalSearchInput", searchTerms);

        Log.i("TESTTT", "food search url: " + builder.toString());

        return builder.toString();
    }

    private String makeFoodIdURL(String foodId){
        Uri.Builder builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/" + foodId).buildUpon();
        builder.appendQueryParameter("api_key", apiKey);

        Log.i("TESTTT", "food id url: " + builder.toString());

        return builder.toString();
    }
}
