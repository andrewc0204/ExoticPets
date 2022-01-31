package com.kurbcreations.exoticpets.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.kurbcreations.exoticpets.models.ExoticPet;

import java.util.List;

@Dao
public interface ExoticPetDao {
    @Query("SELECT * FROM pet")
    List<ExoticPet> getAll();
    @Insert
    void insertAll(ExoticPet... pets);

    @Insert
    void insertPet(ExoticPet pet);

    @Query("UPDATE pet SET pet_name= :petName WHERE second_id = :id" )
    void updateName(String petName, String id);

    @Query("UPDATE pet SET last_date_fed= :dateFed WHERE second_id = :id" )
    void updateDateFed(String dateFed, String id);

    @Query("UPDATE pet SET last_time_fed = :timeFed WHERE second_id = :id" )
    void updateTimeFed (String timeFed, String id);

    @Query("UPDATE pet SET camera_image = :cameraImage WHERE second_id = :id" )
    void updatePetPicture (String cameraImage, String id);

    @Query("DELETE FROM pet WHERE second_id = :secondId")
    void deleteBySecondID(String secondId);



//    @Update
//    void updateNow (ExoticPet pet);

//    @Delete
//    void delete(ExoticPet pet);

    //This method is returning several pets where the list ArrayList<Integer> intArray; int[] (List)
//    @Query("SELECT * FROM pet WHERE id IN (:petIds)")
//    List<ExoticPet> getPetsWithIds(int[] petIds);

//    @Query("SELECT * FROM pet WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

}
