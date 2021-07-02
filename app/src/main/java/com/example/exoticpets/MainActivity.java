package com.example.exoticpets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//Hello

public class MainActivity extends AppCompatActivity {

    ListView myPets;

    Button plus;
    ArrayList<String> arrayNames = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPets = findViewById(R.id.myPets);
        plus = findViewById(R.id.addPet);
        

        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this,R.layout.arraytext, arrayNames);
        myPets.setAdapter(myarrayAdapter);
        myPets.setBackgroundColor(Color.parseColor("#0E86D4"));


//        final EditText input = new EditText(MainActivity.this);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * Fiz: I created a list of different animals, and created something called an adapter.
                 * The adapter is what is "between" the UI (what people see) and the data (what is in "the back").
                 *
                 */

                String[] s = { "Arachnids", "Amphibians", "Reptiles", "Dogs", "Cats "};
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, s);


                /**
                 * Fiz: I created a view (create_pet_layout) located in the layout folder.
                 * I then made and initialized a view from the layout (hence the view = create_pet_layout)
                 *
                 * Take a look at create_pet_layout
                 */
                View view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);

                /**
                 * Fiz: Then, i created a new Alert Dialog, and used the view object (create_pet_layout) to make the alert
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


                /**
                 * I populated the Spinner (located in the create_pet_layout) with the data from earlier (Like the arachnids, amphibians, etc) using the adapter i made earlier.
                 *
                 */
                Spinner spinner = view.findViewById(R.id.spinner);
                spinner.setAdapter(arrayAdapter);


                /**
                 * This is where i attach the custom layout (create_pet_layout) to the alert dialog
                 *
                 */
                builder.setView(view);

                /**
                 *
                 * Fiz: Then, i showed the alert (The actual alert coming into view)
                 *
                 * See how the create_pet_layout and the alert dialog look the same? I'm basically manually creating a layout file,
                 * and using it as the alert.
                 */
                builder.create().show();

                //Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
    }
}