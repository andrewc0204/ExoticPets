package com.example.exoticpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //RecycleView vars
    public static RecyclerViewAdapter mAdapter;

    //ArrayList vars
    private ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    private ArrayList<ExoticPet> mSearchedNamesArrayList = new ArrayList<>();

    //View vars
    private View view;
    private View deletePetView;
    private View changePetPictureView;

    //Toolbar vars
    public static Toolbar searchPetToobar;

    //ImageView vars
    private ImageView defaultImageView;
    private CircleImageView changePetPictureImageView;

    //Button Vars
    private AppCompatButton createPetButton;
    private Button closeButton;
    private ImageButton changePetGalleryImageButton;
    private ImageButton changePetCameraImageButton;
    private ImageButton galleryImageButton;
    private ImageButton pictureImageButton;

    private FloatingActionButton addPetButton;

    //TextView Vars
    private TextView searchForPetTextview;
    private TextView instructionTextView;

    //EasyImage Vars
    private EasyImage easyImage;
    private File cameraPicture;
    private File cameraPicture1;

    //Room vars
    private AppDatabase db;
    private ExoticPetDao exoticPetDao;
    public static Executor executor;

    //Booleans
    private boolean pictureTaken = false;
//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String TEXT = "sharedPrefs";
//    public static final String KEY_CONNECTIONS = "KEY_CONNECTIONS";


    //Camera Feature
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            //Loads user picture into imageView
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {

                for (MediaFile imageFile : imageFiles) {
                    if (!mAdapter.changePicture) {
                        Glide.with(MainActivity.this)
                                .load(new File(String.valueOf(imageFile.getFile())))
                                .into(defaultImageView);
                        cameraPicture = imageFile.getFile();

                        pictureTaken = true;
                    } else {
                        Glide.with(MainActivity.this)
                                .load(new File(String.valueOf(imageFile.getFile())))
                                .into(changePetPictureImageView);
                        cameraPicture1 = imageFile.getFile();
                        mAdapter.changeCameraPicture(cameraPicture1);
                        mAdapter.changePicture = false;
                    }
                    break;
                }
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
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

        //create new thread
        executor = Executors.newSingleThreadExecutor();
        //Database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "pet_database").build();
        exoticPetDao = db.exoticPetDAO();
        executor.execute(() -> {
            exoticPets = (ArrayList<ExoticPet>) exoticPetDao.getAll();
        });

        //Views
        deletePetView = getLayoutInflater().inflate(R.layout.delete_pet_layout, null);
        changePetPictureView = getLayoutInflater().inflate(R.layout.change_pet_picture, null);

        //ImageViews
        changePetPictureImageView = changePetPictureView.findViewById(R.id.change_pet_picture);

        //Toolbars
        searchPetToobar = findViewById(R.id.toolbar);

        //Buttons
        addPetButton = findViewById(R.id.addPetButton1);
        changePetCameraImageButton = changePetPictureView.findViewById(R.id.change_pet_camera_ImageButton);
        changePetGalleryImageButton = changePetPictureView.findViewById(R.id.change_pet_gallery_ImageButton);

        //TextViews
        instructionTextView = findViewById(R.id.instruction_textview);
        searchForPetTextview = findViewById(R.id.textview_searchForPet);

        ExoticPet exoticPet = new ExoticPet(null, R.drawable.ladybug, null, null, null);
        initViews();

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


        //Change pet picture feature
        changePetCameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyImage.openCameraForImage(MainActivity.this);
            }
        });

        changePetGalleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyImage.openGallery(MainActivity.this);
            }
        });

        //Prompts the create pet layout
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view = getLayoutInflater().inflate(R.layout.create_pet_layout, null);

                closeButton = view.findViewById(R.id.close_btn);
                galleryImageButton = view.findViewById(R.id.change_pet_gallery_ImageButton);
                pictureImageButton = view.findViewById(R.id.change_pet_camera_ImageButton);
                createPetButton = view.findViewById(R.id.create_pet_button);

                EditText petNameEditText = view.findViewById(R.id.edittext_pet_name);

                defaultImageView = view.findViewById(R.id.paw_imageview);

                Spinner spinner = view.findViewById(R.id.spinner);

                //Alert Dialog for creating the pet
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        pictureTaken = false;
                    }
                });

                //Spinner function
                String[] listOfAnimals = {"Choose Animal", "Arachnid", "Amphibian", "Reptile", "Insect", "Fish"};
                ArrayAdapter<String> arrayAdapter = new CustomSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, listOfAnimals);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String petTypeName = parent.getItemAtPosition(position).toString();
                        switch (petTypeName) {
                            case "Arachnid":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.spider)
                                            .into(defaultImageView);
                                }
                                break;
                            case "Amphibian":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.frog)
                                            .into(defaultImageView);
                                }
                                break;
                            case "Reptile":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.snake)
                                            .into(defaultImageView);
                                }
                                break;
                            case "Insect":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.ladybug)
                                            .into(defaultImageView);
                                }
                                break;
                            case "Fish":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.fish)
                                            .into(defaultImageView);
                                }
                                break;
                            default:
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.ic_pawprint)
                                            .into(defaultImageView);
                                }
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                pictureImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        easyImage.openCameraForImage(MainActivity.this);
                    }
                });

                galleryImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        easyImage.openGallery(MainActivity.this);
                    }
                });

                createPetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //Checks to see if user type in a pet name
                        String spinnerSelectedPet = spinner.getSelectedItem().toString();
                        if (petNameEditText.getText().toString().isEmpty()) {
                            petNameEditText.setError("Please type pet name");
                            petNameEditText.requestFocus();
                            //Checks to see if user picked a pet type
                        } else if (spinner.getSelectedItem().toString().equals("Choose Animal")) {
                            Toast.makeText(MainActivity.this, "Choose Animal", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cameraPicture != null) {
                                exoticPet.setCameraPicture(String.valueOf(cameraPicture));
                                exoticPet.setPetName(petNameEditText.getText().toString());
                                instructionTextView.setVisibility(View.GONE);
                                alertDialog.dismiss();
                            } else {
                                exoticPet.setPetName(petNameEditText.getText().toString());
                                //Sets text to disappear once the user adds a pet
                                instructionTextView.setVisibility(View.GONE);
                                switch (spinnerSelectedPet) {
                                    case "Arachnid":
                                        exoticPet.setPetImage(R.drawable.spider);
                                        alertDialog.dismiss();
                                        break;
                                    case "Amphibian":
                                        exoticPet.setPetImage(R.drawable.frog);
                                        alertDialog.dismiss();
                                        break;
                                    case "Reptile":
                                        exoticPet.setPetImage(R.drawable.snake);
                                        alertDialog.dismiss();
                                        break;
                                    case "Insect":
                                        exoticPet.setPetImage(R.drawable.ladybug);
                                        alertDialog.dismiss();
                                        break;
                                    case "Fish":
                                        exoticPet.setPetImage(R.drawable.fish);
                                        alertDialog.dismiss();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "Type Pet Name", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                pictureTaken = false;
                            }
                            exoticPets.add(exoticPet);
                            mAdapter.notifyItemInserted(exoticPets.size() - 1);
                            //Insert the data into offline Room on a seperate thread (highway) instead of the UI thread (The main highway)
                            executor.execute(() -> { exoticPetDao.insertPet(exoticPet);
                            });
                        }
                    }
                });

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        pictureTaken = false;
                    }
                });

            }
        });
    }

    //This method sets up the RecycleView in the app
    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        mAdapter = new RecyclerViewAdapter(this, exoticPets, changePetPictureView, cameraPicture1, deletePetView, addPetButton);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Search Pet Feature
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

//    @Override
//    protected void onStop() {
//        super.onStop();
////        saveData();
//    }


//    public void saveData(){
//
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(exoticPets);
//        editor.putString("pet list", json);
//        editor.apply();
//        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
//
//    }
//    public void loadData(){
//        Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("pet list", null);
//        Type type = new TypeToken<ArrayList<ExoticPet>>(){}.getType();
//        exoticPets = gson.fromJson(json, type);
//
//        if (exoticPets == null){
//            exoticPets = new ArrayList<>();
//        }
//    }

}



