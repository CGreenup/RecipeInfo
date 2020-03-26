package com.chrisgreenup.recipeinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_button).setOnClickListener(this);
        findViewById(R.id.app_info_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(view.getId() == R.id.start_button) {
            intent = new Intent(getBaseContext(), RecipeInfoActivity.class);
        }else{
            intent = new Intent(getBaseContext(), AboutAppActivity.class);
        }
        startActivity(intent);
    }
}
