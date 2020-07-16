package com.mahmoud.bakingapp.Retrofit;

import com.mahmoud.bakingapp.BuildConfig;
import com.mahmoud.bakingapp.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeEndPoint {

    @GET(BuildConfig.RECIPE_PATH)
     Call<List<Recipe>> getRecipes();
}
