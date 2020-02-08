package com.webtomob.myrecipe.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.webtomob.myrecipe.R;
import com.webtomob.myrecipe.model.Recipe;
import com.webtomob.myrecipe.utils.Utils;

import java.io.File;
import java.util.List;

public class HomeAdpater extends RecyclerView.Adapter {

    List<Recipe> recipeList;
    private Context context;

    public HomeAdpater(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);

        return new NewsItem(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NewsItem item = (NewsItem) holder;
        final Recipe recipe = recipeList.get(position);

        item.mNameTextView.setText(recipe.getName());
        item.mDurationTextView.setText(recipe.getCookingTime() + context.getString(R.string.min));
        item.mIngredientTextView.setText(recipe.getIngredient());

        if(recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()){
            //Picasso.get().load(new File(recipe.getImageUrl())).into(item.mRecipeImageView);
            Glide.with(context)
                    .asBitmap()
                    .load(new File(recipe.getImageUrl()))
                    .format(DecodeFormat.PREFER_RGB_565)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    //  override for old devices with low memory
                    .apply(RequestOptions.overrideOf(500))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                            item.mRecipeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            item.mRecipeImageView.setImageDrawable(drawable);
                        }
                    });
        }else{
            item.mRecipeImageView.setScaleType(ImageView.ScaleType.CENTER);
            item.mRecipeImageView.setImageDrawable(context.getDrawable(R.drawable.ic_image_area_white_48dp));
        }

        Log.e("THIS IS ", recipe.getCatName() + " .. name .. " + recipe.getName());

    }

    @Override
    public int getItemCount() {
        return (null != recipeList ? recipeList.size() : 0);
    }

    private class NewsItem extends RecyclerView.ViewHolder {
        TextView mNameTextView, mDurationTextView, mIngredientTextView;
        ImageView mRecipeImageView;

        NewsItem(View v) {
            super(v);
            this.mNameTextView = v.findViewById(R.id.nameTextView);
            this.mDurationTextView = v.findViewById(R.id.durationTextView);
            this.mIngredientTextView = v.findViewById(R.id.ingredientTextView);
            this.mRecipeImageView = v.findViewById(R.id.recipeImageView);

        }
    }
}
