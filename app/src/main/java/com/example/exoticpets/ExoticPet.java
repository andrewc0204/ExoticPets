package com.example.exoticpets;

import java.io.Serializable;

public class ExoticPet implements Serializable {
    String id;
    String petName;
    String petImage;
    String whenPetWasLastFed;
    boolean isSelected = false;

    public String getWhenPetWasLastFed() {
        return whenPetWasLastFed;
    }

    public void setWhenPetWasLastFed(String whenPetWasLastFed) {
        this.whenPetWasLastFed = whenPetWasLastFed;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
