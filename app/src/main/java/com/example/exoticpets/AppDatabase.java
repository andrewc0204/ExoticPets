package com.example.exoticpets;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ExoticPet.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExoticPetDAO exoticPetDAO();

}
