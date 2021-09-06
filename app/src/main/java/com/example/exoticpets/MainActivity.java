package com.example.exoticpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //RecycleView vars
    private ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    //Vars
    private RecyclerViewAdapter mAdapter;
    private Button plusButton;
    private ImageButton petImageButton;
    private TextView searchForPetTextview;
    private TextView instructionView;
    private EasyImage easyImage;
    private View view;
    private ImageView defaultImage;
    private FloatingActionButton addPetButton;
    private Toolbar searchPetToobar;
    private ArrayList<ExoticPet> mSearchedNamesArrayList = new ArrayList<>();



    //Camera Feature
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                for (MediaFile imageFile : imageFiles) {
                    Glide.with(MainActivity.this)
                            .load(new File(String.valueOf(imageFile.getFile())))
                            .into(defaultImage);
                    break;
                }
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

        //Hides the action bar
        //getSupportActionBar().hide();

        //Vars
        searchPetToobar = findViewById(R.id.toolbar);
        addPetButton = findViewById(R.id.addPetButton1);
        instructionView = findViewById(R.id.instructionView);
        searchForPetTextview = findViewById(R.id.textview_searchForPet);

        this.setSupportActionBar(searchPetToobar);
        this.getSupportActionBar().setTitle("");

        //Allows user to take pictures
        easyImage = new EasyImage.Builder(MainActivity.this)
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
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Fiz: I created a view (create_pet_layout) located in the layout folder.
                 * I then made and initialized a view from the layout (hence the view = create_pet_layout)
                 * Take a look at create_pet_layout
                 */

                view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);

                /**
                 * We initialized a Button from the create_pet_layout
                 */
                petImageButton = view.findViewById(R.id.petImageButton);
                AppCompatButton createPetButton = view.findViewById(R.id.create_pet_button);
                EditText petNameEditText = view.findViewById(R.id.edittext_pet_name);
                defaultImage = view.findViewById(R.id.paw_imageview);
                Spinner spinner = view.findViewById(R.id.spinner);

                /**
                 * Fiz: Then, i created a new Alert Dialog, and used the view object (create_pet_layout) to make the alert
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                /**
                 * I populated the Spinner (located in the create_pet_layout) with the data from earlier (Like the arachnids, amphibians, etc) using the adapter i made earlier.
                 */
                String[] listOfAnimals = {"Choose Animal", "Arachnid", "Amphibian", "Reptile", "Insect", "Fish"};
                ArrayAdapter<String> arrayAdapter = new CustomSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, listOfAnimals);


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
                                Glide.with(MainActivity.this)
                                        .asBitmap()
                                        .load(R.drawable.ic_baseline_image_24)
                                        .into(defaultImage);
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
                        ExoticPet exoticPet = new ExoticPet();
                        Random r = new Random();
                        int randomIDNumber = r.nextInt(9999 - 1001) + 1001;
                        exoticPet.setId(String.valueOf(randomIDNumber));


                        //When the user clicks the button, whatever code we write here will be run
                        //Checks to see if user type in a pet name
                        String spinnerSelectedPet = spinner.getSelectedItem().toString();
                        if (petNameEditText.getText().toString().isEmpty()) {
                            petNameEditText.setError("Please type pet name");
                            petNameEditText.requestFocus();
                        } else if (spinner.getSelectedItem().toString().equals("Choose Animal")) {
                            Toast.makeText(MainActivity.this, "Choose Animal", Toast.LENGTH_SHORT).show();
                        } else {
                            exoticPet.setPetName(petNameEditText.getText().toString());
//                            mNames.add(petNameEditText.getText().toString());
                            //Sets text to disappear once the user adds a pet
                            instructionView.setVisibility(View.GONE);

                            switch (spinnerSelectedPet) {
                                case "Arachnid":
                                    exoticPet.setPetImage("https://opengameart.org/sites/default/files/styles/medium/public/SpiderEnemy.png");
//                                    mImageUrls.add("https://opengameart.org/sites/default/files/styles/medium/public/SpiderEnemy.png");
                                    alertDialog.dismiss();
                                    break;
                                case "Amphibian":
                                    exoticPet.setPetImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPw_xYUc0rjL5QuYa6CIEk7z1D7eH6BI5gsg&usqp=CAU");
//                                    mImageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPw_xYUc0rjL5QuYa6CIEk7z1D7eH6BI5gsg&usqp=CAU");
                                    alertDialog.dismiss();
                                    break;
                                case "Reptile":
                                    exoticPet.setPetImage("https://image.shutterstock.com/image-vector/vector-illustration-cartoon-snake-pixel-260nw-398666929.jpg");
//                                    mImageUrls.add("https://image.shutterstock.com/image-vector/vector-illustration-cartoon-snake-pixel-260nw-398666929.jpg");
                                    alertDialog.dismiss();
                                    break;
                                case "Insect":
                                    exoticPet.setPetImage("https://art.pixilart.com/eb6f46cc7831237.gif");
//                                    mImageUrls.add("https://art.pixilart.com/eb6f46cc7831237.gif");
                                    alertDialog.dismiss();
                                    break;
                                case "Fish":
                                    exoticPet.setPetImage("https://image.shutterstock.com/image-vector/fish-icon-pixel-style-animal-260nw-1789259792.jpg");
//                                    mImageUrls.add("https://image.shutterstock.com/image-vector/fish-icon-pixel-style-animal-260nw-1789259792.jpg");
                                    alertDialog.dismiss();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Type Pet Name", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            exoticPets.add(exoticPet);
                            //NotifyDataSetChanged basically tells the adapter, "Hey man, we have new data. Please refresh the UI to reflect the new data"
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        initImageBitmaps();
    }

    private void initImageBitmaps() {
        initViews();
    }

    //This method sets up the RecycleView in the app
    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        mAdapter = new RecyclerViewAdapter(this, exoticPets);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //While user is typing
        searchForPetTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence pet, int start, int before, int count) {


            }

            //OnSearchEmpty
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSearchedNamesArrayList.clear();

            }

            //After Search has been completed (stopped typing)
            @Override
            public void afterTextChanged(Editable userTypingInSearchBar) {
                //The Search
                for (ExoticPet exoticPet : exoticPets) {
                    if (exoticPet.getPetName().contains(userTypingInSearchBar)) {
                        mSearchedNamesArrayList.add(exoticPet);
                    }
                }
                //This is updating data
                mAdapter.updateDisplayedPets(mSearchedNamesArrayList);
                mAdapter.notifyDataSetChanged();//Equivalent of hitting F5 (refresh) on browser
            }
        });
    }

//    //enables user to search for pets
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.search_view);
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//        });
//
//        return true;
//    }
}