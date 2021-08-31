package com.example.exoticpets;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class AnimalDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_details);
        getIncomingIntent();
        //Hides the action bar
        getSupportActionBar().hide();

    }
    private void getIncomingIntent(){
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
