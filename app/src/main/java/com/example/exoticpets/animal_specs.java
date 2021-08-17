package com.example.exoticpets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class animal_specs extends AppCompatActivity {

    TextView selectedPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_specs);

//        selectedPet = findViewById(R.id.textview_selectedPet);
//
//        Intent intent = getIntent();
//
//        if(intent.getExtras() != null){
//            UserModel userModel = (UserModel) intent.getSerializableExtra("data");
//
//            selectedPet.setText(userModel.getUserName());
//
//        }




    }
}