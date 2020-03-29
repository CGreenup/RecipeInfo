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
import java.util.Scanner;

public class RecipeInfoActivity extends AppCompatActivity
implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityRecipe();

        Log.i("TESTTT", "https://api.nal.usda.gov/fdc/v1/search?api_key=" +
                getResources().getString(R.string.api_key) + "&generalSearchInput=" + "sugar");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn){
            writeData();
            setupResults();
            readData();
            doDownload();
        }
        else {
            setupActivityRecipe();
        }
    }

    void setupActivityRecipe(){
        setContentView(R.layout.activity_recipe);
        findViewById(R.id.submit_btn).setOnClickListener(this);
    }

    void setupResults(){
        setContentView(R.layout.results);
        findViewById(R.id.result_return_btn).setOnClickListener(this);
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
                val = scanner.nextLine();
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

    private FdcDownload dataDownload;

    private void doDownload(){
        if(dataDownload == null){
            dataDownload = new FdcDownload();
            dataDownload.execute();
        }
    }
}
