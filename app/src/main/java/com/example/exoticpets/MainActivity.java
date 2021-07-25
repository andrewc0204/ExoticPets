package com.example.exoticpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;


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
    EasyImage easyImage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            View view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);
            ImageView defaultImage = view.findViewById(R.id.paw_imageview);
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(imageFiles)
                        .into(defaultImage);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        //Hides the action bar
        getSupportActionBar().hide();

        //Vars
        plusButton = findViewById(R.id.addPetButton);
        instructionView = findViewById(R.id.instructionView);

        //For Loops Tutorial (Not part of app)
        /*
        What is a for loop? A for loop is used for iterating through a array or objects.

        ArrayList<String> colors = new ArrayList<>;
        colors = {"red", "blue", "green", "orange", "yellow"};

        for(color in colors)
        if(color == orange){
        Toast("Found orange!")
        }

        for(all of the pets){
        comparePetThatNeedsToBeFedRightMostUrgently()

        Wally (In 2 hours)
        Spider Pig (Sunday at 2pm)
        Rainbow Unicorn (Monday at 4pm)


        To summarize, a for loop goes through all of the items (or in this case, the pets and their feeding times)
        }

         */


         easyImage = new EasyImage.Builder(MainActivity.this)

// Chooser only
// Will appear as a system chooser title, DEFAULT empty string
//.setChooserTitle("Pick media")
// Will tell chooser that it should show documents or gallery apps
//.setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)  you can use this or the one below
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
// saving EasyImage state (as for now: last camera file link)


// Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
                .setCopyImagesToPublicGalleryFolder(false)
// Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
                .setFolderName("EasyImage sample")

// Allow multiple picking
                .allowMultiple(true)
                .build();


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * Fiz: I created a view (create_pet_layout) located in the layout folder.
                 * I then made and initialized a view from the layout (hence the view = create_pet_layout)
                 * Take a look at create_pet_layout
                 */

                View view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);
                petImageButton = view.findViewById(R.id.petImageButton);

                /**
                 * We initialized a Button from the create_pet_layout
                 */
                Button createPetButton = view.findViewById(R.id.create_pet_button);
                EditText petNameEditText = view.findViewById(R.id.edittext_pet_name);
                ImageView defaultImage = view.findViewById(R.id.paw_imageview);

                /**
                 * Fiz: Then, i created a new Alert Dialog, and used the view object (create_pet_layout) to make the alert
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                /**
                 * I populated the Spinner (located in the create_pet_layout) with the data from earlier (Like the arachnids, amphibians, etc) using the adapter i made earlier.
                 */





                String[] listOfAnimals = {"Choose Animal", "Arachnid", "Amphibian", "Reptile", "Insect", "Fish"};
                ArrayAdapter<String> arrayAdapter = new CustomSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, listOfAnimals);

                Spinner spinner = view.findViewById(R.id.spinner);
                spinner.setAdapter(arrayAdapter);
                //sets default image if user has not selected one from gallery
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String petTypeName = parent.getItemAtPosition(position).toString();
                        switch (petTypeName) {
                            case "Arachnid":
                                Glide.with(MainActivity.this)
                                        .asBitmap()
                                        .load("https://opengameart.org/sites/default/files/styles/medium/public/SpiderEnemy.png")
                                        .into(defaultImage);
                                break;
                            case "Amphibian":
                                Glide.with(MainActivity.this)
                                        .asBitmap()
                                        .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPw_xYUc0rjL5QuYa6CIEk7z1D7eH6BI5gsg&usqp=CAU")
                                        .into(defaultImage);
                                break;
                            case "Reptile":
                                Glide.with(MainActivity.this)
                                        .asBitmap()
                                        .load("https://image.shutterstock.com/image-vector/vector-illustration-cartoon-snake-pixel-260nw-398666929.jpg")
                                        .into(defaultImage);
                                break;
                            case "Insect":
                                Glide.with(MainActivity.this)
                                        .asBitmap()
                                        .load("https://art.pixilart.com/eb6f46cc7831237.gif")
                                        .into(defaultImage);
                                break;
                            case "Fish":
                                Glide.with(MainActivity.this)
                                        .asBitmap()
                                        .load("https://image.shutterstock.com/image-vector/fish-icon-pixel-style-animal-260nw-1789259792.jpg")
                                        .into(defaultImage);
                                break;
                            default:
                                mImageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrADorfjbjLGdl18wl5_5bE3pJnSS5A0tg7A&usqp=CAU");
                                break;
                        }

                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button closeBtn = view.findViewById(R.id.close_btn);

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


                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                //Camera Button
                petImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        easyImage.openCameraForImage(MainActivity.this);
                    }
                });




                createPetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When the user clicks the button, whatever code we write here will be run
                        //Checks to see if user type in a pet name
                        if (petNameEditText.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Type Pet Name", Toast.LENGTH_SHORT).show();
                        } else {
                            mNames.add(petNameEditText.getText().toString());
                            String spinnerSelectedPet = spinner.getSelectedItem().toString();
                            createPetButton.setEnabled(true);

                            //Sets text to disappear once the user adds a pet
                            instructionView.setVisibility(View.GONE);


                            switch (spinnerSelectedPet) {
                                case "Arachnid":
                                    mImageUrls.add("https://opengameart.org/sites/default/files/styles/medium/public/SpiderEnemy.png");
                                    alertDialog.dismiss();
                                    break;
                                case "Amphibian":
                                    mImageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPw_xYUc0rjL5QuYa6CIEk7z1D7eH6BI5gsg&usqp=CAU");
                                    alertDialog.dismiss();
                                    break;
                                case "Reptile":
                                    mImageUrls.add("https://image.shutterstock.com/image-vector/vector-illustration-cartoon-snake-pixel-260nw-398666929.jpg");
                                    alertDialog.dismiss();
                                    break;
                                case "Insect":
                                    mImageUrls.add("https://art.pixilart.com/eb6f46cc7831237.gif");
                                    alertDialog.dismiss();
                                    break;
                                case "Fish":
                                    mImageUrls.add("https://image.shutterstock.com/image-vector/fish-icon-pixel-style-animal-260nw-1789259792.jpg");
                                    alertDialog.dismiss();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Type Pet Name", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            //NotifyDataSetChanged basically tells the adapter, "Hey man, we have new data. Please refresh the UI to reflect the new data"
                            mAdapter.notifyDataSetChanged();
                            //alertDialog.dismiss();
                        }
                    }


                });
                //Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
        initImageBitmaps();
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");
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


}