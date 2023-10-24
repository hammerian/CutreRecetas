package com.example.cutrerecetas;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class DataWriter {

    private static Context context;

    public DataWriter(Context context){
        this.context = context;
    }

    public final static String PREFS_NAME = "cutrerecetas_prefs";

    public boolean sharedPreferenceExist(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.contains(key);
    }

    public static void setList(String key, ArrayList<Recipe> list) {
        // Save the Arraylist to preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
        prefs.edit().remove(key).apply();
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    @NonNull
    public static ArrayList<Recipe> getList(String key) {
        // Load the Arraylist to preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Recipe[] recipes = gson.fromJson(json, Recipe[].class);
        return new ArrayList<Recipe>(Arrays.asList(recipes));
    }

    public static void setCateg(String categ) {
        // Save the String to Categories
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().remove("categs").apply();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("categs", categ);
        editor.apply();
    }

    public static String getCateg (){
        // Load the String to Categories
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("categs", null);
    }

    public static void clearCateg (){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
        prefs.edit().remove("categs").apply();
    }

}