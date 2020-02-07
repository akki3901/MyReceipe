package com.webtomob.myrecipe.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.webtomob.myrecipe.model.Recipe;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM RECIPE ORDER BY ID")
    List<Recipe> getAllReceipe();

    @Insert(onConflict = REPLACE)
    void insertRecipeItem(Recipe recipeItem);

    @Update
    void updateReceipeItem(Recipe recipeItem);

    @Delete
    void delete(Recipe recipeItem);

    @Query("SELECT * FROM RECIPE WHERE id = :id")
    Recipe loadReceipeItemById(int id);

    @Query("DELETE FROM RECIPE")
    void deleteAll();

    /*@Query("UPDATE receipe SET status = :status WHERE ticketId = :ticketId")
    int updateTicketStatus(String ticketId, String status);*/
}
