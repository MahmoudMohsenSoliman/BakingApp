package com.mahmoud.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("ingredients")
    private List<RecipeIngredient> mIngredients;
    @SerializedName("steps")
    private List<RecipeInstruction> mInstructions;
    @SerializedName("servings")
    private int mServings;


    public String getName() {
        return mName;
    }

    public List<RecipeIngredient> getIngredients() {
        return mIngredients;
    }

    public List<RecipeInstruction> getInstructions() {
        return mInstructions;
    }

    public int getServings() {
        return mServings;
    }

    private Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredients = in.createTypedArrayList(RecipeIngredient.CREATOR);
        mInstructions = in.createTypedArrayList(RecipeInstruction.CREATOR);
        mServings = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mInstructions);
        dest.writeInt(mServings);
    }
}
