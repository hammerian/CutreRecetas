package com.example.cutrerecetas;

import android.media.Image;

public class Recipe {

    private String recipeName;
    private String recipeType;

    private int recipeImage;

    public Recipe() {
    }

    public Recipe(String recipeName, String recipeType, int recipeImage) {
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.recipeImage = recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public int getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(int recipeImage) {
        this.recipeImage = recipeImage;
    }
}
