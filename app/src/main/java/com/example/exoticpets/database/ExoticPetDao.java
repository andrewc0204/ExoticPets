package com.example.exoticpets.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.exoticpets.models.ExoticPet;

import java.util.List;

@Dao
public interface ExoticPetDao {
    @Query("SELECT * FROM pet")
    List<ExoticPet> getAll();

    @Insert
    void insertAll(ExoticPet... pets);


    @Insert
    void insertPet(ExoticPet pet);

//    @Query ("UPDATE pet SET pet_name = :petName ")
//    void updatePetName(String petName);

    @Update
    void updateNow (ExoticPet pet);

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateNow (ExoticPet pet);

    @Delete
    void delete(ExoticPet pet);

    @Query("DELETE FROM pet WHERE second_id = :secondId")
    void deleteBySecondID(String secondId);


//    fiz — Today at 2:02 PM
//    @Query("DELETE FROM action_object WHERE shared_task_action_id = :sharedActionTaskId")
//    void delete(String sharedActionTaskId);


    //This method is returning several pets where the list ArrayList<Integer> intArray; int[] (List)
//    @Query("SELECT * FROM pet WHERE id IN (:petIds)")
//    List<ExoticPet> getPetsWithIds(int[] petIds);

//    @Query("SELECT * FROM pet WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

}
