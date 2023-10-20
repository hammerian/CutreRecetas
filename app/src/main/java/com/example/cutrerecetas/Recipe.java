package com.example.cutrerecetas;

import android.media.Image;

import java.io.Serializable;

public class Recipe implements Serializable  {

    private String recipeName;
    private String recipeDesc;
    private String recipeType;

    private String recipeImage;

    public Recipe() {
    }

    public Recipe(String recipeName, String recipeDesc, String recipeType, String recipeImage) {
        this.recipeName = recipeName;
        this.recipeDesc = recipeDesc;
        this.recipeType = recipeType;
        this.recipeImage = recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

    public void setRecipeDesc(String recipeDesc) {
        this.recipeDesc = recipeDesc;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
}
