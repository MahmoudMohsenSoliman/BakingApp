package com.mahmoud.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.google.android.material.snackbar.BaseTransientBottomBar.Behavior;
import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.bakingapp.Model.Adapter.BakingRecipeAdapter;
import com.mahmoud.bakingapp.Model.Adapter.BakingRecipeAdapter.OnCardClickListener;
import com.mahmoud.bakingapp.Model.Recipe;
import com.mahmoud.bakingapp.Retrofit.RecipeClient;
import com.mahmoud.bakingapp.Retrofit.RecipeEndPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnCardClickListener {


    @BindView(R.id.recipes_recycler_view)
     RecyclerView mRecyclerView;
    @BindView(R.id.no_internet_connection_layout)
     LinearLayout noConnectionLayout;
    @BindView(R.id.progress_bar)
     ProgressBar mProgressBar;

    private static List<Recipe> mRecipes;
    private BakingRecipeAdapter mAdapter;

    public static final String INGREDIENTS_EXTRA = "Recipe_Info_Ingredients";
    public static final String INSTRUCTIONS_EXTRA = "Recipe_Info_Instructions";
    public static final String TITLE_EXTRA = "Recipe_Info_Title";
    public static final String SELECTED_RECIPE_EXTRA = "Recipe_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                || getResources().getBoolean(R.bool.isTablet))
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        else
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getRecipesFromClient();


        mAdapter = new BakingRecipeAdapter(this,this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


    }

    private void getRecipesFromClient()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        noConnectionLayout.setVisibility(View.INVISIBLE);

        RecipeEndPoint recipeEndPoint = RecipeClient.getClient().create(RecipeEndPoint.class);

        Call<List<Recipe>> call = recipeEndPoint.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(response.isSuccessful())
                {

                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    noConnectionLayout.setVisibility(View.INVISIBLE);

                    mRecipes = response.body();
                    mAdapter.setRecipes(mRecipes);
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                noConnectionLayout.setVisibility(View.VISIBLE);

                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout),
                        getString(R.string.no_connection_snack_bar),Snackbar.LENGTH_INDEFINITE);

                snackbar.setActionTextColor(getResources().getColor(R.color.red));

                snackbar.setAction(getString(R.string.retry_action_snack_bar), v -> getRecipesFromClient());

                snackbar.setBehavior(new Behavior(){
                    @Override
                    public boolean canSwipeDismissView(View child) {
                        return false;
                    }
                });
                snackbar.show();
            }
        });
    }

    @Override
    public void onClick(int position) {
        Recipe recipe = mRecipes.get(position);

        Intent intent = new Intent(this,RecipeInfoActivity.class);
        intent.putParcelableArrayListExtra(INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        intent.putParcelableArrayListExtra(INSTRUCTIONS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getInstructions());
        intent.putExtra(TITLE_EXTRA,recipe.getName());
        intent.putExtra(SELECTED_RECIPE_EXTRA,position);

        startActivity(intent);
    }

    public static List<Recipe> getRecipes(){return mRecipes;}

}