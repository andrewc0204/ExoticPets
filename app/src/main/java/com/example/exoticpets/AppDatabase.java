package com.example.exoticpets;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ExoticPet.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExoticPetDao exoticPetDAO();

}
