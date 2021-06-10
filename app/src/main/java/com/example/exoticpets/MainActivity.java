package com.example.exoticpets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
//Hello

public class MainActivity extends AppCompatActivity {

    ListView myPets;
    String[] myList;
    Button plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPets = findViewById(R.id.myPets);
        myList = new String[]{"Wally", "Chinnok"};
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        myPets.setAdapter(myarrayAdapter);

        plus = findViewById(R.id.addPet);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
        



    }
}