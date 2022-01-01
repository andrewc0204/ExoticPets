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
    int  petImage;
    @ColumnInfo(name = "last_fed")
    String datePetWasLastFed;
    @ColumnInfo(name = "last_time_fed")
    String timePetWasLastFed;

    public ExoticPet(String petName, int petImage, String datePetWasLastFed, String timePetWasLastFed) {
        this.petName = petName;
        this.petImage = petImage;
        this.datePetWasLastFed = datePetWasLastFed;
        this.timePetWasLastFed = timePetWasLastFed;
    }

    public String getWhenPetWasLastFed() {
        return datePetWasLastFed;
    }

    public void setWhenPetWasLastFed(String whenPetWasLastFed) {
        this.datePetWasLastFed = whenPetWasLastFed;
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


    public int getPetImage() {
        return petImage;
    }

    public void setPetImage(int petImage) {
        this.petImage = petImage;
    }

    public String getDatePetWasLastFed() {
        return datePetWasLastFed;
    }

    public void setDatePetWasLastFed(String datePetWasLastFed) {
        this.datePetWasLastFed = datePetWasLastFed;
    }

    public String getTimePetWasLastFed() {
        return timePetWasLastFed;
    }

    public void setTimePetWasLastFed(String timePetWasLastFed) {
        this.timePetWasLastFed = timePetWasLastFed;
    }
}
