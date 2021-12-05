package com.example.exoticpets;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExoticPetDao {
    @Query("SELECT * FROM pet")
    List<ExoticPet> getAll();

    @Insert
    void insertAll(ExoticPet... pets);

    @Insert
    void insertPet(ExoticPet pet);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updatePet(ExoticPet pet);

    @Delete
    void delete(ExoticPet pet);

    //This method is returning several pets where the list ArrayList<Integer> intArray; int[] (List)
//    @Query("SELECT * FROM pet WHERE id IN (:petIds)")
//    List<ExoticPet> getPetsWithIds(int[] petIds);

//    @Query("SELECT * FROM pet WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

}
