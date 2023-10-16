package com.example.cutrerecetas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

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

        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });

    }
    public void onButtonShowPopupWindowClick(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.recipe_form, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        EditText edtTxt11 = (EditText) popupView.findViewById(R.id.edtTxtName);
        EditText edtTxt12 = (EditText) popupView.findViewById(R.id.edtTxtType);
        Button btn2 = popupView.findViewById(R.id.btnRcp);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }
}