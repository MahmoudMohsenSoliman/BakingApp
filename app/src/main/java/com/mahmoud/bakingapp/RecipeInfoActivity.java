package com.mahmoud.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import com.mahmoud.bakingapp.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class RecipeInfoActivity extends AppCompatActivity  {


    private List<Recipe> mRecipes;
    private int mSelectedRecipe;
    private boolean twoPane = false;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null)
        {
            twoPane = savedInstanceState.getBoolean("twoPane");
            mSelectedRecipe = savedInstanceState.getInt(MainActivity.SELECTED_RECIPE_EXTRA);
            FragmentManager manager = getSupportFragmentManager();
            Fragment info = manager.findFragmentByTag("info");
            manager.beginTransaction().replace(R.id.recipe_info_container,info,"info").commit();
            if(twoPane){
                Fragment step = manager.findFragmentByTag("step");
                if(step!=null)
                manager.beginTransaction().replace(R.id.recipe_inst_container,step,"step").commit();
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        ButterKnife.bind(this);
        mRecipes = MainActivity.getRecipes();
        mSelectedRecipe = getIntent().getIntExtra(MainActivity.SELECTED_RECIPE_EXTRA,-1);

        if(findViewById(R.id.recipe_inst_container)!=null)
        {
            twoPane = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("twoPane",twoPane);

            RecipeInfoFragment infoFragment = new RecipeInfoFragment();




            infoFragment.setArguments(bundle);

            infoFragment.setRetainInstance(true);


            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_info_container,infoFragment,"info").commit();


        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new RecipeInfoFragment()).commit();
            twoPane=false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.recipe_controller_menu,menu);
        int size = mRecipes.size();

        MenuItem nextItem = menu.findItem(R.id.next_item);
        MenuItem prevItem = menu.findItem(R.id.prev_item);

        if(mSelectedRecipe==size-1)
        {
            invalidateOptionsMenu();
            nextItem.setVisible(false);
        }
        else if(mSelectedRecipe == 0)
        {
            invalidateOptionsMenu();
            prevItem.setVisible(false);
        }
        else {
            invalidateOptionsMenu();
            nextItem.setVisible(true);
            prevItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItem = item.getItemId();


        if(selectedItem==R.id.next_item)
        {
            mSelectedRecipe++;
            Recipe recipe = mRecipes.get(mSelectedRecipe);

            Intent intent = getIntent();
            intent.putParcelableArrayListExtra(MainActivity.INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getIngredients());
            intent.putParcelableArrayListExtra(MainActivity.INSTRUCTIONS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getInstructions());
            intent.putExtra(MainActivity.TITLE_EXTRA,recipe.getName());
            intent.putExtra(MainActivity.SELECTED_RECIPE_EXTRA,mSelectedRecipe);

            finish();
            startActivity(intent);
        }
        else if(selectedItem==R.id.prev_item)
        {
            mSelectedRecipe--;
            Recipe recipe = mRecipes.get(mSelectedRecipe);

            Intent intent = getIntent();
            intent.putParcelableArrayListExtra(MainActivity.INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getIngredients());
            intent.putParcelableArrayListExtra(MainActivity.INSTRUCTIONS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getInstructions());
            intent.putExtra(MainActivity.TITLE_EXTRA,recipe.getName());
            intent.putExtra(MainActivity.SELECTED_RECIPE_EXTRA,mSelectedRecipe);

            finish();
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MainActivity.SELECTED_RECIPE_EXTRA,mSelectedRecipe);
        outState.putBoolean("twoPane",twoPane);
    }
}
