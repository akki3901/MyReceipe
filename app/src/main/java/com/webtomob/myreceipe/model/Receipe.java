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
    String shortDesc;
    String cookingTime;
    String date;

    public Receipe() {
    }

    @Ignore
    public Receipe(int id, String catId, String name, String imageUrl, String steps, String shortDesc, String cookingTime, String date) {
        this.id = id;
        this.catId = catId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.steps = steps;
        this.shortDesc = shortDesc;
        this.cookingTime = cookingTime;
        this.date = date;

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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
