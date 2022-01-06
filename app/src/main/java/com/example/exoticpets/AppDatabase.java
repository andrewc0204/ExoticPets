package com.example.exoticpets;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//3 for testing
@Database(entities = {ExoticPet.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExoticPetDao exoticPetDAO();

}
