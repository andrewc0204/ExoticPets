package com.example.exoticpets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//Hello

public class MainActivity extends AppCompatActivity {

    ListView myPets;
    String[] myList;
    Button plus;
    ArrayList<String> arrayNames = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPets = findViewById(R.id.myPets);
        plus = findViewById(R.id.addPet);

        myList = new String[]{"Wally", "Chinnok"};
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayNames);
        myPets.setAdapter(myarrayAdapter);
        myPets.setBackgroundColor(Color.parseColor("#0E86D4"));


        final EditText input = new EditText(this);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Enter Pet Name")
                        .setView(input)
                        // Specifying a listener allows you to take an action before dismissing the dialog.

                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                              myarrayAdapter.add(input.getText().toString());
                              myPets.deferNotifyDataSetChanged();
                                if(input.getParent()!=null)
                                    ((ViewGroup)input.getParent()).removeView(input);
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                //Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });




    }
}