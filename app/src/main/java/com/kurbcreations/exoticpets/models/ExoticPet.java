package com.kurbcreations.exoticpets.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "pet")
public class ExoticPet implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "second_id")
    String secondId;
    @ColumnInfo(name = "pet_name")
    String petName;
    @ColumnInfo(name = "pet_image")
    int  petImage;
    @ColumnInfo(name = "last_date_fed")
    String datePetWasLastFed;
    @ColumnInfo(name = "last_time_fed")
    String timePetWasLastFed;
    @ColumnInfo(name = "camera_image")
    String cameraPicture;



    public ExoticPet(String secondId, String petName, int petImage, String datePetWasLastFed, String timePetWasLastFed, String cameraPicture) {
        this.secondId = secondId;
        this.petName = petName;
        this.petImage = petImage;
        this.datePetWasLastFed = datePetWasLastFed;
        this.timePetWasLastFed = timePetWasLastFed;
        this.cameraPicture = cameraPicture;
    }


    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getCameraPicture() {
        return cameraPicture;
    }

    public void setCameraPicture(String cameraPicture) {
        this.cameraPicture = cameraPicture;
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
