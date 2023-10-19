package com.example.cutrerecetas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public RecipeAdapter(ArrayList<Recipe> lstData) {
        this.recipeData = lstData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView txtView1;
        public TextView txtView2;
        private ArrayList<Recipe> recipeList;

        public LinearLayout rltId1;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initiate Recipe Cell elements
            this.imgView = (ImageView) itemView.findViewById(R.id.imgView2);
            this.txtView1 = (TextView) itemView.findViewById(R.id.txtName);
            this.txtView2 = (TextView) itemView.findViewById(R.id.txtType);
            rltId1 = (LinearLayout) itemView.findViewById(R.id.rltId1);
        }
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Link Recipe Cell with the Recycler view
        LayoutInflater lytInflater = LayoutInflater.from(parent.getContext());
        View lstItem = lytInflater.inflate(R.layout.recipe_card, parent, false);
        ViewHolder vwHolder = new ViewHolder(lstItem);
        return vwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {

        // Populate cell with Recipe Data
        final Recipe mListData = recipeData.get(position);
        holder.txtView1.setText(mListData.getRecipeName());
        holder.txtView2.setText(mListData.getRecipeType());
        int imgData = Integer.parseInt(mListData.getRecipeImage());
        holder.imgView.setImageResource(imgData);

    }

    @Override
    public int getItemCount() {
        return this.recipeData.size();
    }


    public void remove(int position) {
        Recipe item = recipeData.get(position);
        if (recipeData.contains(item)) {
            recipeData.remove(position);
            notifyItemRemoved(position);
        }
    }

}
