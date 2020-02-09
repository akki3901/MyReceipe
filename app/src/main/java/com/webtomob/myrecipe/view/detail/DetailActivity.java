package com.webtomob.myrecipe.view.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.webtomob.myrecipe.R;
import com.webtomob.myrecipe.database.AppDatabase;
import com.webtomob.myrecipe.database.AppExecutor;
import com.webtomob.myrecipe.model.Recipe;
import com.webtomob.myrecipe.utils.Utils;

import java.io.File;

public class DetailActivity extends AppCompatActivity {
    private Recipe recipeItem;
    private ImageView mRecipeImageView;
    private TextView mCatTextView, mNameTextView, mIngredientTextView, mStepsTextView, mDurationTextView;
    private Button mDeleteBtn, mEditBtn;
    private AppDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        mDatabase = AppDatabase.getInstance(this);
        if (getIntent() != null) {
            recipeItem = Utils.g.fromJson(getIntent().getStringExtra("recipeItem"), Recipe.class);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(recipeItem != null) {
            toolbar.setTitle(recipeItem.getName());
        }
        setSupportActionBar(toolbar);

        setupUI();

        if(recipeItem != null) {
            showData();
        }
    }

    private void setupUI(){
        mRecipeImageView = findViewById(R.id.recipeImageView);
        mCatTextView = findViewById(R.id.catTextView);
        mNameTextView = findViewById(R.id.nameTextView);
        mIngredientTextView = findViewById(R.id.ingredientTextView);
        mStepsTextView = findViewById(R.id.stepsTextView);
        mDurationTextView = findViewById(R.id.durationTextView);
        mDeleteBtn = findViewById(R.id.deleteButton);
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlertDialog(recipeItem.getId());
            }
        });

    }

    private void showData(){
        mNameTextView.setText(recipeItem.getName());
        mDurationTextView.setText(recipeItem.getCookingTime() + getString(R.string.min));
        mIngredientTextView.setText(recipeItem.getIngredient());
        mCatTextView.setText(recipeItem.getCatName());
        mStepsTextView.setText(recipeItem.getSteps());

        if(recipeItem.getImageUrl() != null && !recipeItem.getImageUrl().isEmpty()){
            //Picasso.get().load(new File(recipe.getImageUrl())).into(item.mRecipeImageView);
            Glide.with(this)
                    .asBitmap()
                    .load(new File(recipeItem.getImageUrl()))
                    .format(DecodeFormat.PREFER_RGB_565)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    //  override for old devices with low memory
                    .apply(RequestOptions.overrideOf(500))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(getResources(), resource);
                            mRecipeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            mRecipeImageView.setImageDrawable(drawable);
                        }
                    });
        }else{
            mRecipeImageView.setScaleType(ImageView.ScaleType.CENTER);
            mRecipeImageView.setImageDrawable(getDrawable(R.drawable.ic_image_area_white_48dp));
        }
    }

    private void deleteAlertDialog(int recipeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_confirmation))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        deleteRecipe(recipeId);

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(getString(R.string.not_saved));
        alert.show();
    }

    private void deleteRecipe(int recipeId){
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.receipeDao().deleteRecipeById(recipeId);

            }
        });

        Toast.makeText(getBaseContext(), getString(R.string.delete_success), Toast.LENGTH_LONG).show();
        finish();
    }

}
