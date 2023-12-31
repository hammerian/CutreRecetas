package com.example.cutrerecetas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecipesList extends AppCompatActivity {

    private Button newRecipe;
    private ImageButton imgBtn;

    private ImageView imgView1;
    private Spinner spnrFilter;
    private RecyclerView rclrView;

    private Bitmap imageV;
    private static final int CAMERA_REQUEST_CODE = 100;
    private DataWriter dataWr;
    private ArrayList<Recipe> newListData;
    private ArrayList<String> arrCategories;
    private RecipeAdapter rcpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        // Initiate activity elements
        rclrView = findViewById(R.id.rclView);
        newRecipe = findViewById(R.id.ntnRecipe);
        imgBtn = findViewById(R.id.imgBtn);
        spnrFilter = findViewById(R.id.spnrFilter);
        dataWr = new DataWriter(this);

        arrCategories = new ArrayList<>();
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
                // Different option selected
                if(position == 0){
                    ArrayList<Recipe> rcpLst = new ArrayList<Recipe>(newListData);
                    rcpAdapter.applyFilter(rcpLst);
                    dataWr.clearCateg();
                } else {
                    String str = spnrFilter.getSelectedItem().toString();
                    ArrayList<Recipe> rcpLst = new ArrayList<Recipe>();
                    for (int i = 0; i < newListData.size();i++) {
                        Recipe rc = newListData.get(i);
                        if (rc.getRecipeType().toString().equals(str)) {
                         // filterListData.remove(i); //TODO: Error al actualizar el array
                            rcpLst.add(rc);
                        }
                    }
                    dataWr.setCateg(str);
                    rcpAdapter.applyFilter(rcpLst);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing selected
            }
        });

        // recovers the categories filter from previous sessions
        if (dataWr.sharedPreferenceExist("categs")) {
            String categ = dataWr.getCateg();
            for (int i = 0; i < arrCategories.size();i++) {
                String compCateg = arrCategories.get(i);
                if (categ.equals(compCateg)){
                    spnrFilter.setSelection(i);
                }
            }
        }
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
        imgView1 = (ImageView) popupView.findViewById(R.id.imgView1) ;
        Button btn2 = popupView.findViewById(R.id.btnRcp);
        Button btnPic = popupView.findViewById(R.id.btnPic);

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

        // Add Image button action
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                } else {
                    openCamera();
                }
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
                    Recipe newRecipe;
                    if (imageV == null) {
                        newRecipe = new Recipe(recipeName, recipeDesc, category, "" + R.drawable.recipe2, false);
                    } else {
                        newRecipe = new Recipe (recipeName, recipeDesc, category,imageV,false);
                    }
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

    // Function to open device camera
    private void openCamera () {
        Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamara, 1);
    }

    // Function to request camera permissions
    private boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return read == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted.", Toast.LENGTH_LONG).show();
                // Do stuff here for Action Image Capture.
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try{
                Bundle extra = data.getExtras();
                imageV = (Bitmap) extra.get("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageV.compress(Bitmap.CompressFormat.PNG, 100, baos);
               //data = baos.toByteArray();

                imgView1.setImageBitmap(imageV);
            } catch (NullPointerException npe) {
                System.out.println(npe);
            }
        }
    }

    // Function to change values in spinner
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