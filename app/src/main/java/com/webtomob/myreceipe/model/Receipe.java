package com.webtomob.myreceipe.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "receipe")
public class Receipe {
    @PrimaryKey(autoGenerate = true)
    int id;
    String catId;
    String name;
    String imageUrl;
    String steps;
    String ingredient;
    String cookingTime;

    public Receipe() {
    }

    @Ignore
    public Receipe(int id, String catId, String name, String imageUrl, String steps, String ingredient, String cookingTime) {
        this.id = id;
        this.catId = catId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.steps = steps;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    @Ignore
    public Receipe(String catId, String name, String imageUrl, String steps, String ingredient, String cookingTime) {
        this.catId = catId;
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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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
