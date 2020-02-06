package com.webtomob.myreceipe.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.webtomob.myreceipe.model.Receipe;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ReceipeDao {

    @Query("SELECT * FROM RECEIPE ORDER BY ID")
    List<Receipe> getAllReceipe();

    @Insert(onConflict = REPLACE)
    void insertReceipeItem(Receipe receipeItem);

    @Update
    void updateReceipeItem(Receipe receipeItem);

    @Delete
    void delete(Receipe receipeItem);

    @Query("SELECT * FROM RECEIPE WHERE id = :id")
    Receipe loadReceipeItemById(int id);

    @Query("DELETE FROM RECEIPE")
    void deleteAll();

    /*@Query("UPDATE receipe SET status = :status WHERE ticketId = :ticketId")
    int updateTicketStatus(String ticketId, String status);*/
}
