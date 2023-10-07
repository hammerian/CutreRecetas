package com.example.cutrerecetas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class RecipesList extends AppCompatActivity {

    private Button newRecipe;


    private ArrayList<Recipe> newListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        RecyclerView rclrView = (RecyclerView) findViewById(R.id.rclView);
        newRecipe = (Button) findViewById(R.id.ntnRecipe);

        newListData = new ArrayList<Recipe>();

        newListData.add(new Recipe("Galletas con crema","Copa",R.drawable.recipe1));
        newListData.add(new Recipe("Arandanos y coco","Tarta",R.drawable.recipe2));
        newListData.add(new Recipe("Chocolate con almendras","Bizcocho",R.drawable.recipe3));
        newListData.add(new Recipe("Chocolate y fresas","Tarta",R.drawable.recipe4));

        rclrView.setLayoutManager(new LinearLayoutManager(this));

        RecipeAdapter adapter = new RecipeAdapter(newListData);
        rclrView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rclrView.setAdapter(adapter);

    }
}