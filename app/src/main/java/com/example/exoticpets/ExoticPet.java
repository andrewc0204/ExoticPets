package com.example.exoticpets;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "pet")
public class ExoticPet implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "pet_name")
    String petName;
    @ColumnInfo(name = "pet_image")
    String petImage;
    @ColumnInfo(name = "last_fed")
    String whenPetWasLastFed;



    @ColumnInfo(name = "last_time_fed")
    String timePetWasLastFed;

    public ExoticPet(String petName, String petImage, String whenPetWasLastFed, String timePetWasLastFed) {
        this.petName = petName;
        this.petImage = petImage;
        this.whenPetWasLastFed = whenPetWasLastFed;
        this.timePetWasLastFed = timePetWasLastFed;
    }

    public String getWhenPetWasLastFed() {
        return whenPetWasLastFed;
    }

    public void setWhenPetWasLastFed(String whenPetWasLastFed) {
        this.whenPetWasLastFed = whenPetWasLastFed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }
    public String getTimePetWasLastFed() {
        return timePetWasLastFed;
    }

    public void setTimePetWasLastFed(String timePetWasLastFed) {
        this.timePetWasLastFed = timePetWasLastFed;
    }
}
