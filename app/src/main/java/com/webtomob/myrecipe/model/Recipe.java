package com.webtomob.myrecipe.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    int id;
    String catName;
    String name;
    String imageUrl;
    String steps;
    String ingredient;
    String cookingTime;

    public Recipe() {
    }

    @Ignore
    public Recipe(int id, String catName, String name, String imageUrl, String steps, String ingredient, String cookingTime) {
        this.id = id;
        this.catName = catName;
        this.name = name;
        this.imageUrl = imageUrl;
        this.steps = steps;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    @Ignore
    public Recipe(String catName, String name, String imageUrl, String steps, String ingredient, String cookingTime) {
        this.catName = catName;
        this.name = name;
        this.imageUrl = imageUrl;
        this.steps = steps;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

}
