package com.example.exoticpets.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.exoticpets.models.ExoticPet;


@Database(entities = {ExoticPet.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ExoticPetDao exoticPetDAO();

}
