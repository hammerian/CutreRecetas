package com.example.cutrerecetas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipesList extends AppCompatActivity {

    private Button newRecipe;
    private ImageButton imgBtn;

    private Spinner spnrFilter;
    private RecyclerView rclrView;
    private DataWriter dataWr;
    private ArrayList<Recipe> newListData;
    private ArrayList<String> arrCategories;
    private RecipeAdapter rcpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        // Initiate activity elements
        rclrView = (RecyclerView) findViewById(R.id.rclView);
        newRecipe = (Button) findViewById(R.id.ntnRecipe);
        imgBtn = (android.widget.ImageButton) findViewById(R.id.imgBtn);
        spnrFilter = (Spinner) findViewById(R.id.spnrFilter);
        dataWr = new DataWriter(this);

        arrCategories = new ArrayList<String>();
        arrCategories.add("--Seleccione Una Categoría--");
        arrCategories.add("Bizcocho");
        arrCategories.add("Copa");
        arrCategories.add("Tarta");

        changeCategs(spnrFilter);

        // Loading App content data
        if (dataWr.sharedPreferenceExist("recipes")) {
            // App already executed
            newListData = dataWr.getList("recipes");
        } else {
            // first execution of the app
            newListData = new ArrayList<Recipe>();
            // Load data from scratch
            newListData.add(new Recipe("Galletas con crema", "Ingredientes: Harina, aceite, leches, azucar, ...", "Copa", ""+R.drawable.recipe1,false));
            newListData.add(new Recipe("Arandanos y coco", "Ingredientes: Harina, aceite, leches, azucar, ...", "Tarta", ""+R.drawable.recipe2,false));
            newListData.add(new Recipe("Chocolate con almendras", "Ingredientes: Harina, aceite, leches, azucar, ...", "Bizcocho", ""+R.drawable.recipe3,false));
            newListData.add(new Recipe("Chocolate y fresas", "Ingredientes: Harina, aceite, leches, azucar, ...", "Tarta", ""+R.drawable.recipe4,false));
            // saving data to device
            dataWr.setList("recipes", newListData);
        }


        // Recycler view initiation
        rclrView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Recipe> rcpLst = new ArrayList<Recipe>(newListData);
        rcpAdapter = new RecipeAdapter(rcpLst);
        rclrView.setAdapter(rcpAdapter);
        rclrView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        setUpItemTouchHelper();

        // Add recipe button action
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });

        // Change to DrawerNavigation
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itn = new Intent(RecipesList.this, DetailActivity.class);
                startActivity(itn);
            }
        });

        // Change filter Categories
        spnrFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String str = spnrFilter.getSelectedItem().toString();
                // Diferent option selected
                if(position == 0){
                    ArrayList<Recipe> rcpLst = new ArrayList<Recipe>(newListData);
                    rcpAdapter.applyFilter(rcpLst);
                } else {
                    ArrayList<Recipe> rcpLst = new ArrayList<Recipe>();
                    for (int i = 0; i < newListData.size();i++) {
                        Recipe rc = newListData.get(i);
                        if (rc.getRecipeType().toString().equals(str)) {
                         // filterListData.remove(i); //TODO: Error al actualizar el array
                            rcpLst.add(rc);
                        }
                    }
                    rcpAdapter.applyFilter(rcpLst);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing selected
            }
        });
    }

    private void setUpItemTouchHelper() {
        // https://github.com/nemanja-kovacevic/recycler-view-swipe-to-deletes
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(RecipesList.this, R.drawable.ic_clear_24);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) RecipesList.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                xMark.setVisible(false,false);
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                RecipeAdapter testAdapter = (RecipeAdapter) recyclerView.getAdapter();
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RecipeAdapter adapter = (RecipeAdapter) rclrView.getAdapter();
                adapter.remove(swipedPosition);
             // newListData.remove(swipedPosition); //TODO: Error al actualizar el array
                newListData = adapter.getRecipeData();
                dataWr.setList("recipes", adapter.getRecipeData());
                adapter.notifyDataSetChanged();
                initiated = false;
                xMark.setVisible(false,false);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // Victor: Created safe clear of Delete button that previously don't dismiss :( --{
                boolean isCanceled = (dX == 0f) && !isCurrentlyActive;
                if (isCanceled) {
                    xMark.setVisible(false,true);
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    return;
                }
                // }--s

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(rclrView);
    }


    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        Paint pnt = new Paint();
        c.drawRect(left, top, right, bottom, pnt);
    }

    public void onButtonShowPopupWindowClick(View view) {
        // Open PopupView to create a new Recipe Object
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.recipe_form, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // Initiate Popup elements
        EditText edtTxt11 = (EditText) popupView.findViewById(R.id.edtTxtName);
        EditText edtTxt12 = (EditText) popupView.findViewById(R.id.edtTxtDesc);
        Spinner spnCat = (Spinner) popupView.findViewById(R.id.spnRecipe);
        Button btn2 = popupView.findViewById(R.id.btnRcp);

        changeCategs(spnCat);
        // Launch PopupView
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Dismiss PopupView
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        // Save recipe button action
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recollection of Form Fields
                String category = spnCat.getSelectedItem().toString();
                String recipeName = edtTxt11.getText().toString().trim();
                String recipeDesc = edtTxt12.getText().toString().trim();

                if (testForm(category,recipeName,recipeDesc)) {
                    Recipe newRecipe = new Recipe(recipeName, recipeDesc, category, "" + R.drawable.recipe2,false);
                    newListData.add(newRecipe);
                    dataWr.setList("recipes", newListData);
                    rcpAdapter.applyFilter(newListData);
                    spnrFilter.setSelection(0);
                    rcpAdapter.notifyDataSetChanged();

                    Toast.makeText(RecipesList.this, "Registro agregado", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void changeCategs(Spinner spnR) {
        ArrayAdapter<String> categAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrCategories);
        categAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnR.setAdapter(categAdapter);
    }

    // Function to test correct completion of the new recipe Form
    private boolean testForm(String category, String recipeName, String recipeDesc) {
        if(category.equals("--Seleccione Una Categoría--")){
            Toast.makeText(RecipesList.this, "Debes seleccionar una categoría para continuar", Toast.LENGTH_SHORT).show();
            return false;
        }else if (recipeName.isEmpty()) {
            Toast.makeText(RecipesList.this, "Debes escribir un nombre para continuar", Toast.LENGTH_SHORT).show();
            return false;
        }else if (recipeDesc.isEmpty()) {
            Toast.makeText(RecipesList.this, "Debes escribir una descripción para continuar", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}