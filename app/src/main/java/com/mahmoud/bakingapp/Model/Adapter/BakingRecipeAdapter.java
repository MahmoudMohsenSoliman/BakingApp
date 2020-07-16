package com.mahmoud.bakingapp.Model.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mahmoud.bakingapp.Model.Adapter.BakingRecipeAdapter.BakingRecipeViewHolder;
import com.mahmoud.bakingapp.Model.Recipe;
import com.mahmoud.bakingapp.Model.RecipeInstruction;
import com.mahmoud.bakingapp.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingRecipeAdapter extends Adapter<BakingRecipeViewHolder>   {

    private final Context mContext;
    private List<Recipe> mRecipes;
    private final OnCardClickListener mOnCardClickListener;


    public BakingRecipeAdapter(Context context,OnCardClickListener onCardClickListener) {
        mContext = context;
        mOnCardClickListener = onCardClickListener;

    }
    public interface OnCardClickListener  {

        void onClick(int position);
    }
    @NonNull
    @Override
    public BakingRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rowItem = LayoutInflater.from(mContext).inflate(R.layout.row_recipe_recycler_item,parent,false);

        return new BakingRecipeViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BakingRecipeViewHolder holder, int position) {

        Recipe recipe = mRecipes.get(position);

        int instructionSize = recipe.getInstructions().size();
        RecipeInstruction instruction = recipe.getInstructions().get(instructionSize-1);

        holder.recipeName.setText(recipe.getName());
        String servings = "Servings: "+recipe.getServings();
        holder.recipeServings.setText(servings);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(mContext.getResources().getColor(R.color.colorAccent));
        circularProgressDrawable.start();


            Glide.with(mContext).load(instruction.getVideoUrl())
                    .override(344,194)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(circularProgressDrawable)
                    .error(R.mipmap.cloud)
                    .into(holder.recipeImg);

    }

    @Override
    public int getItemCount() {
        if(mRecipes==null||mRecipes.size()==0)return 0;

        return mRecipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    class BakingRecipeViewHolder extends ViewHolder implements OnClickListener {

      //  SimpleExoPlayerView recipeVideo;
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_servings)
        TextView recipeServings;
        @BindView(R.id.recipe_img)
        ImageView recipeImg;

         BakingRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
             ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnCardClickListener.onClick(position);
        }
    }


}
