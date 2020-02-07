package com.webtomob.myrecipe.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.webtomob.myrecipe.model.Category;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM CATEGORY ORDER BY ID")
    List<Category> getAllCategory();

    @Insert(onConflict = REPLACE)
    void insertCategoryItem(Category categoryItem);

    @Update
    void updateCategoryItem(Category categoryItem);

    @Delete
    void delete(Category categoryItem);

    @Query("SELECT * FROM CATEGORY WHERE id = :id")
    Category loadCategoryItemById(int id);

    @Query("DELETE FROM CATEGORY")
    void deleteAll();

    /*@Query("UPDATE receipe SET status = :status WHERE ticketId = :ticketId")
    int updateTicketStatus(String ticketId, String status);*/
}
