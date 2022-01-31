package com.kurbcreations.exoticpets.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.kurbcreations.exoticpets.models.ExoticPet;

import java.util.List;

public class MainActivityViewModel {

    public MutableLiveData <List<ExoticPet>> mExoticPets = new MutableLiveData<>();

//    public LiveData <List<ExoticPet>> getExoticPets;

    public MutableLiveData<List<ExoticPet>> getmExoticPets() {
        return mExoticPets;
    }


    public void setmExoticPets(List<ExoticPet> mExoticPets) {
        this.mExoticPets.setValue(mExoticPets);
    }
}
