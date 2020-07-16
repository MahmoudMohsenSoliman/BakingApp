package com.mahmoud.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahmoud.bakingapp.Model.Adapter.StepsAdapter;
import com.mahmoud.bakingapp.Model.Adapter.StepsAdapter.OnInstructionClickListener;
import com.mahmoud.bakingapp.Model.RecipeIngredient;
import com.mahmoud.bakingapp.Model.RecipeInstruction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeInfoFragment extends Fragment implements OnInstructionClickListener {

    @BindView(R.id.ingredients_desc)
     TextView ingredientsTextView;
    @BindView(R.id.steps_recycler_view)
     RecyclerView stepsRecyclerView;

    @BindView(R.id.bookmark_button)
    FloatingActionButton mFloatingActionButton;
    private  List<RecipeInstruction> instructions;
    private String ingredientsString;
    private String title;
    public static final String EXTRA_INSTRUCTION = "Instructions";
    public static final String EXTRA_SELECTED_INSTRUCTION = "Instruction_Position";
    public static final String PREF_EXTRA_TITLE = "recipe_title";
    public static final String PREF_EXTRA_INGREDIENTS = "recipe_ingredients";

     private boolean twoPane = false;

    public RecipeInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null)
        {
            instructions = savedInstanceState.getParcelableArrayList(EXTRA_INSTRUCTION);
            title = savedInstanceState.getString("APP Title");
            ingredientsString = savedInstanceState.getString(PREF_EXTRA_INGREDIENTS);
            Fragment fragment = getFragmentManager().findFragmentByTag("step");
            if(fragment!=null)
            getFragmentManager().beginTransaction().replace(R.id.recipe_inst_container,fragment,"step");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root =inflater.inflate(R.layout.fragment_recipe_info, container, false);
        ButterKnife.bind(this,root);

        Intent intent = getActivity().getIntent();
        List<RecipeIngredient> ingredients = intent.getParcelableArrayListExtra(MainActivity.INGREDIENTS_EXTRA);
        instructions = intent.getParcelableArrayListExtra(MainActivity.INSTRUCTIONS_EXTRA);
         title = intent.getStringExtra(MainActivity.TITLE_EXTRA);

         ingredientsString = makeIngredientsText(ingredients);
        ingredientsTextView.setText(ingredientsString);

        StepsAdapter adapter = new StepsAdapter(getContext(), this);
        adapter.setInstructions(instructions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        stepsRecyclerView.setLayoutManager(layoutManager);
        stepsRecyclerView.setAdapter(adapter);
        stepsRecyclerView.setNestedScrollingEnabled(false);

        getActivity().setTitle(title);

        if (getArguments() != null) {
            twoPane = getArguments().getBoolean("twoPane");
        }


        mFloatingActionButton.setOnClickListener(v -> {



            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                    .putString(PREF_EXTRA_TITLE,title)
                    .putString(PREF_EXTRA_INGREDIENTS,ingredientsString).apply();


            Toast.makeText(getActivity(),getString(R.string.add_to_widget),Toast.LENGTH_LONG).show();

            ComponentName provider = new ComponentName(getActivity(), IngredientWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
            int[] ids = appWidgetManager.getAppWidgetIds(provider);
            IngredientWidget widget = new IngredientWidget();
            widget.onUpdate(getActivity(), appWidgetManager, ids);

        });

        return root;
    }


    private static String makeIngredientsText(List<RecipeIngredient> ingredients)
    {
        StringBuilder ingredientsStr = new StringBuilder();

        int size = ingredients.size();

        for(int i=0;i<ingredients.size();i++)
        {
            RecipeIngredient ingredient = ingredients.get(i);
            String str = "* "+ingredient.getQuantity()+" "+ingredient.getMeasure()+" "+ingredient.getIngredient();
            ingredientsStr.append(str);

            if(i == size-1)
            {
                ingredientsStr.append(".");
            }
            else
            {
                ingredientsStr.append(".\n");
            }
        }
        return ingredientsStr.toString();
    }

    @Override
    public void onClick(int position) {

        if(twoPane)
        {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>) instructions);
            bundle.putInt(EXTRA_SELECTED_INSTRUCTION,position);
            bundle.putBoolean("twoPane",twoPane);

           Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentByTag("step");
           if (fragment==null){

               RecipeStepFragment stepFragment = new RecipeStepFragment();
               stepFragment.setArguments(bundle);
               stepFragment.setRetainInstance(true);
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_inst_container,stepFragment,"step").commit();



           }
           else{

               Bundle b=fragment.getArguments();
               if(b!=null&&b.getInt(EXTRA_SELECTED_INSTRUCTION)!=position)
               {
                   RecipeStepFragment stepFragment = new RecipeStepFragment();
                   stepFragment.setArguments(bundle);
                   stepFragment.setRetainInstance(true);
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_inst_container,stepFragment,"step").commit();

               }
               else
               {
                   RecipeStepFragment stepFragment = (RecipeStepFragment) fragment;
                   stepFragment.setArguments(bundle);
                   stepFragment.setRetainInstance(true);
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_inst_container,stepFragment,"step").commit();

               }

           }
        }
        else{
            Intent intent = new Intent(getActivity(),RecipeStepActivity.class);
            intent.putParcelableArrayListExtra(EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>) instructions);
            intent.putExtra(EXTRA_SELECTED_INSTRUCTION,position);

            startActivity(intent);
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>) instructions);
        outState.putString(PREF_EXTRA_INGREDIENTS,ingredientsString);
        outState.putString("APP Title",title);
    }
}
