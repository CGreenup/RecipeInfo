package com.chrisgreenup.recipeinfo;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RecipeInfoActivity extends AppCompatActivity
implements View.OnClickListener {

    /*
    Calories
    Carbs
    Sugars
    Protein
    Fat
     */

    int calories, carbs, sugars, protein, fat = 0;
    ArrayList<Integer> allNutritionalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityRecipe();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn){
            ArrayList<Integer> recipeInformation = new ArrayList<>(5);

            //Write everything in the edit text to a file
            writeData();

            //Set up the results activity
            setupResults();

            //Download nutritional information from the database
            allNutritionalInfo = doDownload();
            dataDownload = null;
            initializeVariables();

            //Update the result TextView to show the nutritional info
            updateTextView();
        }
        else {
            setupActivityRecipe();
            EditText et = findViewById(R.id.recipe_et);
            //Set et text to the text in the file
        }
    }

    void setupActivityRecipe(){
        setContentView(R.layout.activity_recipe);
        findViewById(R.id.submit_btn).setOnClickListener(this);
    }

    void updateTextView(){
        StringBuilder builder = new StringBuilder();
        builder.append("Nutritional Information of Recipe:\n\n");

        builder.append("Total calories: " + calories + " kCal\n");
        builder.append("Total carbs: " + carbs + "g\n");
        builder.append("Total sugars: " + sugars + "g\n");
        builder.append("Total protein: " + protein + "g\n");
        builder.append("Total fat: " + fat + "g");

        ((TextView) findViewById(R.id.result_tv)).setText(builder.toString());
    }

    void setupResults(){
        setContentView(R.layout.results);
        findViewById(R.id.result_return_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.result_tv)).setText(getResources().getText(R.string.loading_text));
    }

    private void writeData(){
        try {
            EditText editText = findViewById(R.id.recipe_et);

            FileOutputStream fos = openFileOutput("userInput.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(editText.getText().toString());

            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void readData(){
        try {
            FileInputStream fis = openFileInput("userInput.txt");
            Scanner scanner = new Scanner(fis);

            String val;
            StringBuilder stringBuilder = new StringBuilder();

            while(scanner.hasNext()){
                val = scanner.next();
                val += scanner.nextLine();
                Log.i("TESTTT", val);
                stringBuilder.append(val);
                stringBuilder.append("\n");
            }

            TextView tv = findViewById(R.id.result_tv);
            tv.setText(stringBuilder);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public FileInputStream openFile(String filename){
        FileInputStream fis = null;
        try {
            fis = openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

    private FdcDownload dataDownload;

    private ArrayList doDownload(){
        if(dataDownload == null){
            dataDownload = new FdcDownload(
                    getResources().getString(R.string.api_key),
                    openFile("userInput.txt")
            );
        }
        dataDownload.execute();
        return dataDownload.getInformation();
    }

    private void initializeVariables(){
        calories = allNutritionalInfo.get(0);
        carbs = allNutritionalInfo.get(1);
        sugars = allNutritionalInfo.get(2);
        protein = allNutritionalInfo.get(3);
        fat = allNutritionalInfo.get(4);
    }
}
