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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeData;

    public RecipeAdapter(ArrayList<Recipe> lstData) {
        this.recipeData = lstData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView txtView1;
        public TextView txtView2;

        public LinearLayout rltId1;

        public ViewHolder(View itemView) {
            super(itemView);

            this.imgView = (ImageView) itemView.findViewById(R.id.imgView2);

            this.txtView1 = (TextView) itemView.findViewById(R.id.txtName);
            this.txtView2 = (TextView) itemView.findViewById(R.id.txtType);
            rltId1 = (LinearLayout) itemView.findViewById(R.id.rltId1);
        }
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater lytInflater = LayoutInflater.from(parent.getContext());
        View lstItem = lytInflater.inflate(R.layout.recipe_card, parent, false);
        ViewHolder vwHolder = new ViewHolder(lstItem);
        return vwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {

        final Recipe mListData = recipeData.get(position);
        holder.txtView1.setText(mListData.getRecipeName());
        holder.txtView2.setText(mListData.getRecipeType());
        holder.imgView.setImageResource(mListData.getRecipeImage());

    }

    @Override
    public int getItemCount() {
        return this.recipeData.size();
    }
}
