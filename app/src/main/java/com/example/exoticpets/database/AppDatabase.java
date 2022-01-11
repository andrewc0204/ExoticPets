package com.example.exoticpets.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.exoticpets.database.ExoticPetDao;
import com.example.exoticpets.models.ExoticPet;

//3 for testing
@Database(entities = {ExoticPet.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExoticPetDao exoticPetDAO();

}
