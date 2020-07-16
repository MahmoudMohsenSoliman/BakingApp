package com.mahmoud.bakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        getSupportFragmentManager().beginTransaction().replace(R.id.inst_container,new RecipeStepFragment()).commit();
    }
}
