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
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;



public class FdcDownload extends AsyncTask<Void, Void, String> {

    private FileInputStream fis;
    private String apiKey;

    private int totalCalories = 0;
    private int totalCarbs = 0;
    private int totalSugars = 0;
    private int totalProtein = 0;
    private int totalFat = 0;

    public ArrayList<Integer> getInformation(){
        ArrayList<Integer> arrayList = new ArrayList<>(5);

        arrayList.add(totalCalories);
        arrayList.add(totalCarbs);
        arrayList.add(totalSugars);
        arrayList.add(totalProtein);
        arrayList.add(totalFat);

        return arrayList;
    }

    FdcDownload(String key, FileInputStream fileInputStream){
        apiKey = key;
        fis = fileInputStream;
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();
        String resultString = null;

        //findInformationOf("apple");
        readUserInput();

        return result.toString();

    }

    private String readUserInput(){
        String nextIngredient = "";
        Scanner scanner = new Scanner(fis);

        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNext()) {
            nextIngredient = scanner.nextLine();
            Log.i("TESTTT", nextIngredient);
            findInformationOf(nextIngredient);
            //stringBuilder.append(findInformationOf(nextIngredient));
            //stringBuilder.append("\n");
        }

        return nextIngredient;
    }

    //Find the nutritional information of the given parameter
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
            url = new URL(makeFoodIdURL(id));
            connection = (HttpsURLConnection) url.openConnection();

            is = connection.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            jsonData = new StringBuilder();

            while ((line = br.readLine()) != null)
                jsonData.append(line);

            reader = new JSONObject(jsonData.toString());
            JSONObject labelNutrients = reader.getJSONObject("labelNutrients");

            totalCalories += getNutritionalData(labelNutrients, "calories");
            totalCarbs += getNutritionalData(labelNutrients, "carbohydrates");
            totalFat += getNutritionalData(labelNutrients, "fat");
            totalProtein += getNutritionalData(labelNutrients, "protein");
            totalSugars += getNutritionalData(labelNutrients, "sugars");

            Log.i("TESTTTNUTRINFO", "Calories " + totalCalories);
            Log.i("TESTTTNUTRINFO", "Carbs " + totalCarbs);
            Log.i("TESTTTNUTRINFO", "Fat " + totalFat);
            Log.i("TESTTTNUTRINFO", "Protein " + totalProtein);
            Log.i("TESTTTNUTRINFO", "Sugar " + totalSugars);

            //Grab all of the desired nutrient information

            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getNutritionalData(JSONObject labelNutrients, String searchTerm) throws JSONException {
        return labelNutrients.getJSONObject(searchTerm).getInt("value");
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
