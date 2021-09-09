package com.example.exoticpets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

public class AnimalDetails extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_details);
        getIncomingIntent();
        //Hides the action bar
//        getSupportActionBar().hide();

    }
    private void getIncomingIntent(){

        getIntent().getSerializableExtra("exotic_pets");
        getIntent().getSerializableExtra("image_url");
        getIntent().getSerializableExtra("image_name");

        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")){
            String imageUrl = getIntent().getStringExtra("image_url");
            String imageName = getIntent().getStringExtra("image_name");

            setImage(imageUrl, imageName);
        }
    }
    private void setImage(String imageUrl, String imageName){
        TextView selectedPet = findViewById(R.id.textview_selectedPet);
        selectedPet.setText(imageName);
        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }
}
