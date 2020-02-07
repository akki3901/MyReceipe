package com.webtomob.myreceipe.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {
        @PrimaryKey(autoGenerate = true)
        int id;
        String catId;
        String cateName;

        public Category() {
        }

        @Ignore
        public Category(int id, String catId, String catName) {
            this.id = id;
            this.catId = catId;
            this.cateName = catName;
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

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
