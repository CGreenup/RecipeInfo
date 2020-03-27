package com.chrisgreenup.recipeinfo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeInfoActivity extends AppCompatActivity
implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupResults();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn){
            setupResults();
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

}
