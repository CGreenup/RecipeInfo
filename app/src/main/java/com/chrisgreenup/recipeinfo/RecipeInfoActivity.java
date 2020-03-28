package com.chrisgreenup.recipeinfo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn){
            //setupResults();
            handle();
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

    //When the user clicks the [SUBMIT] button after entering a recipe, this function is called
    //Handles the backend, and TODO needs to be done in background thread probably
    public void handle(){
        EditText et = findViewById(R.id.recipe_et);
        writeData(et);
        readData();

    }

    private void writeData(EditText editText){
        try {
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

            while(scanner.hasNext()){
                Log.i("TESTTT", scanner.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
