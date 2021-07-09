package com.example.exoticpets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //RecycleView vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    //Vars
    RecyclerViewAdapter mAdapter;
    Button plusButton;
    ImageButton petImageButton;
    TextView instructionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");
        getSupportActionBar().hide();

        //Vars
        plusButton = findViewById(R.id.addPetButton);
        instructionView = findViewById(R.id.instructionView);



        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * Fiz: I created a view (create_pet_layout) located in the layout folder.
                 * I then made and initialized a view from the layout (hence the view = create_pet_layout)
                 *
                 * Take a look at create_pet_layout
                 */
                View view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);
                petImageButton = view.findViewById(R.id.petImageButton);

                /**
                 * We initialized a Button from the create_pet_layout
                 */
                Button createPetButton = view.findViewById(R.id.create_pet_button);
                EditText petNameEditText = view.findViewById(R.id.edittext_pet_name);

                /**
                 * Fiz: Then, i created a new Alert Dialog, and used the view object (create_pet_layout) to make the alert
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                /**
                 * I populated the Spinner (located in the create_pet_layout) with the data from earlier (Like the arachnids, amphibians, etc) using the adapter i made earlier.
                 */

                String[] s = {"Arachnid", "Amphibian", "Reptile", "Insect", "Fish"};
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, s);

                Spinner spinner = view.findViewById(R.id.spinner);
                spinner.setAdapter(arrayAdapter);


                /**
                 * This is where i attach the custom layout (create_pet_layout) to the alert dialog
                 */
                builder.setView(view);

                /**
                 * Fiz: Then, i showed the alert (The actual alert coming into view)
                 * See how the create_pet_layout and the alert dialog look the same? I'm basically manually creating a layout file,
                 * and using it as the alert.
                 */
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                petImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });


                createPetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When the user clicks the button, whatever code we write here will be run
                        mNames.add(petNameEditText.getText().toString());
                        String spinnerSelectedPet = spinner.getSelectedItem().toString();

                        instructionView.setVisibility(View.GONE);

                        

                        switch(spinnerSelectedPet) {
                            case "Arachnid":
                                mImageUrls.add("https://i.pinimg.com/originals/b6/73/95/b67395ec910f2d6df475987efc4330e3.gif");
                                break;
                            case "Amphibian":
                                mImageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPw_xYUc0rjL5QuYa6CIEk7z1D7eH6BI5gsg&usqp=CAU");
                                break;
                            case "Reptile":
                                mImageUrls.add("https://image.shutterstock.com/image-vector/vector-illustration-cartoon-snake-pixel-260nw-398666929.jpg");
                                break;
                            case "Insect":
                                mImageUrls.add("https://art.pixilart.com/eb6f46cc7831237.gif");
                                break;
                            case "Fish":
                                mImageUrls.add("https://image.shutterstock.com/image-vector/fish-icon-pixel-style-animal-260nw-1789259792.jpg");
                                break;
                            default:
                                mImageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrADorfjbjLGdl18wl5_5bE3pJnSS5A0tg7A&usqp=CAU");
                                break;
                        }


                        //NotifyDataSetChanged basically tells the adapter, "Hey man, we have new data. Please refresh the UI to reflect the new data"
                        mAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });
                //Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
        initImageBitmaps();
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");
      /*  mImageUrls.add("https://images.ctfassets.net/cnu0m8re1exe/4p84Xg3bCdVn0Rx8HILCVx/c22c167e9f392e37ec7e8ba50bac3f4d/shutterstock_167834045.jpg?fm=jpg&fl=progressive&w=660&h=433&fit=fill");
        mNames.add("Orchid Mantis");
    */

        initRecyclerView();
    }
    //This method sets up the RecycleView in the app


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        mAdapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //Member Variables
    //Local Variables
}