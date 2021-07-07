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
import android.widget.Spinner;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //RecycleView vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    RecyclerViewAdapter mAdapter;

    Button plusButton;
    //ArrayList<String> arrayNames = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");
        plusButton = findViewById(R.id.addPet);
        initImageBitmaps();

        /**
         * This is the List View, the actual list.
         */
        //myPetsListView = findViewById(R.id.myPets);


        /**
         * A Adapter, which is used to populate data (array) into the listview (UI)
         */
        //ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,R.layout.arraytext, arrayNames);
        //myPetsListView.setAdapter(myArrayAdapter);

        /**
         * UI
         * Adapter
         * Data
         *
         *
         * UI.setAdapter(adapter)
         *
         *
         *
         */


        //       myPetsListView.setBackgroundColor(Color.parseColor("#0E86D4"));
//        final EditText input = new EditText(MainActivity.this);


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * Fiz: I created a list of different animals, and created something called an adapter.
                 * The adapter is what is "between" the UI (what people see) and the data (what is in "the back").
                 */


                /**
                 * Fiz: I created a view (create_pet_layout) located in the layout folder.
                 * I then made and initialized a view from the layout (hence the view = create_pet_layout)
                 *
                 * Take a look at create_pet_layout
                 */
                View view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);


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
                 *
                 */

                String[] s = {"Arachnid", "Amphibian", "Reptile", "Insect", "Snake", "Fish"};
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, s);

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
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                createPetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When the user clicks the button, whatever code we write here will be run
                        mNames.add(petNameEditText.getText().toString());
                        mImageUrls.add("https://ohiobiota.com/wp-content/uploads/Vertebrates/Reptiles/Butlers_Garter_Snake/Butlers-Garter-Snake-Andy-Avram-1.jpg");

                        //NotifyDataSetChanged basically tells the adapter, "Hey man, we have new data. Please refresh the UI to reflect the new data"
                        mAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });
                //Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");
        mImageUrls.add("https://images.ctfassets.net/cnu0m8re1exe/4p84Xg3bCdVn0Rx8HILCVx/c22c167e9f392e37ec7e8ba50bac3f4d/shutterstock_167834045.jpg?fm=jpg&fl=progressive&w=660&h=433&fit=fill");
        mNames.add("Orchid Mantis");

        mImageUrls.add("https://static.wixstatic.com/media/f72937_9af787ad188d4d0f915e667bb24e89be~mv2.jpg/v1/fill/w_1000,h_667,al_c,q_90,usm_0.66_1.00_0.01/f72937_9af787ad188d4d0f915e667bb24e89be~mv2.jpg");
        mNames.add("Bold Jumping Spider");

        mImageUrls.add("https://www.galapagospet.com/wp-content/uploads/2020/10/Bearded-Dragon_HERO.jpg");
        mNames.add("Bearded Dragon");

        mImageUrls.add("https://www.lvzoo.org/wp-content/uploads/2015/11/Leopard-Gecko-image.jpg");
        mNames.add("Leopard Gecko");

        mImageUrls.add("data:image/jpeg;base64,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");
        mNames.add("Whipless Tail Scorpion");

        mImageUrls.add("https://animals.net/wp-content/uploads/2018/10/Assassin-Bug-5-650x425.jpg");
        mNames.add("Assassin Bug");

        mImageUrls.add("https://www.nwf.org/-/media/NEW-WEBSITE/Shared-Folder/Wildlife/Invertebrates/invertebrate_rhinoceros-beetle_600x300.ashx");
        mNames.add("Rhinoceros Beetle");

        mImageUrls.add("https://ohiobiota.com/wp-content/uploads/Vertebrates/Reptiles/Butlers_Garter_Snake/Butlers-Garter-Snake-Andy-Avram-1.jpg");
        mNames.add("Garter Snake");

        mImageUrls.add("https://ohiobiota.com/wp-content/uploads/Vertebrates/Reptiles/Butlers_Garter_Snake/Butlers-Garter-Snake-Andy-Avram-1.jpg");
        mNames.add("Rainbow Unicorn");


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