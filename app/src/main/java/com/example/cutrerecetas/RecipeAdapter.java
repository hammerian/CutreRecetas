package com.example.cutrerecetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    private ArrayList<Recipe> recipeData;
    private ArrayList<Recipe> filterData;

    private DataWriter dataWr;

    private Context adapHolder;

    public RecipeAdapter(ArrayList<Recipe> lstData) {
        this.recipeData = lstData;
        this.filterData = lstData;
    }

    public void applyFilter(ArrayList<Recipe> lstData) {
        this.filterData = lstData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.filterData.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView txtView1;
        public TextView txtView2;
        public TextView txtView3;
        public CheckBox chckB1;

        public LinearLayout rltId1;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initiate Recipe Cell elements
            this.imgView = (ImageView) itemView.findViewById(R.id.imgView2);
            this.txtView1 = (TextView) itemView.findViewById(R.id.txtName);
            this.txtView2 = (TextView) itemView.findViewById(R.id.txtDesc);
            this.txtView3 = (TextView) itemView.findViewById(R.id.txtType);
            this.chckB1 = (CheckBox) itemView.findViewById(R.id.chckB1);
            rltId1 = (LinearLayout) itemView.findViewById(R.id.rltId1);

        }
    }

    // Function for return the working array in the Adapter
    public ArrayList<Recipe> getRecipeData() {
        return this.filterData;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Link Recipe Cell with the Recycler view
        LayoutInflater lytInflater = LayoutInflater.from(parent.getContext());
        adapHolder = parent.getContext();
        View lstItem = lytInflater.inflate(R.layout.recipe_card, parent, false);
        ViewHolder vwHolder = new ViewHolder(lstItem);
        return vwHolder;
    }

    // Function for populate the cell
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        // Populate cell with Recipe Data
        final Recipe mListData = filterData.get(position);
        holder.txtView1.setText(mListData.getRecipeName());
        holder.txtView2.setText(mListData.getRecipeDesc());
        holder.txtView3.setText(mListData.getRecipeType());
        holder.chckB1.setChecked(mListData.isRecipeEnd());
        int imgData = Integer.parseInt(mListData.getRecipeImage());
        holder.imgView.setImageResource(imgData);

        // Event with Checkbox // TODO: position da un error que no afecta a la ejecución
        holder.chckB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListData.setRecipeEnd(!mListData.isRecipeEnd());
                filterData.set(position,mListData);
                dataWr = new DataWriter(adapHolder);
                dataWr.setList("recipes", filterData);
            }
        });
        holder.chckB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    public void resetFilter() {
        filterData = recipeData;
    }
    public void remove(int position) {
        Recipe item = filterData.get(position);
        if (filterData.contains(item)) {
            filterData.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void addRegister(int position) {
        Recipe item = filterData.get(position);
        if (filterData.contains(item)) {
            filterData.remove(position);
            notifyItemRemoved(position);
        }
    }
}
