package com.example.cutrerecetas;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

public class Recipe implements Serializable  {

    private String recipeName;
    private String recipeDesc;
    private String recipeType;
    private boolean recipeEnd;

    private String recipeImage;
    private Bitmap imageV;

    public Recipe() {
    }

    public Recipe(String recipeName, String recipeDesc, String recipeType, String recipeImage, boolean recipeEnd) {
        this.recipeName = recipeName;
        this.recipeDesc = recipeDesc;
        this.recipeType = recipeType;
        this.recipeImage = recipeImage;
        this.recipeEnd = recipeEnd;
    }

    public Recipe(String recipeName, String recipeDesc, String recipeType, Bitmap imageV, boolean recipeEnd) {
        this.recipeName = recipeName;
        this.recipeDesc = recipeDesc;
        this.recipeType = recipeType;
        this.imageV = imageV;
        this.recipeEnd = recipeEnd;
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

    public Bitmap getImageV() {
        return imageV;
    }

    public void setImageV(Bitmap imageV) {
        this.imageV = imageV;
    }

    public boolean isRecipeEnd() {
        return recipeEnd;
    }

    public void setRecipeEnd(boolean recipeEnd) {
        this.recipeEnd = recipeEnd;
    }
}
