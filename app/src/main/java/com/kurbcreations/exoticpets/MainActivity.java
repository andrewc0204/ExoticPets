package com.kurbcreations.exoticpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kurbcreations.exoticpets.ViewModels.MainActivityViewModel;
import com.kurbcreations.exoticpets.adapters.CustomSpinnerAdapter;
import com.kurbcreations.exoticpets.adapters.RecyclerViewAdapter;
import com.kurbcreations.exoticpets.database.AppDatabase;
import com.kurbcreations.exoticpets.database.ExoticPetDao;
import com.kurbcreations.exoticpets.models.ExoticPet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //RecycleView vars
    public static RecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;

    //ArrayList vars
    private ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    private ArrayList<ExoticPet> mSearchedNamesArrayList = new ArrayList<>();

    //View vars

    private View view;
    private View deletePetView;
    private View changePetPictureView;
    private View changePetNameView;
    private View addNotificationView;

    //Toolbar vars
    public static Toolbar searchPetToobar;

    //ImageView vars
    private ImageView defaultImageView;
    private ImageView changePetPictureImageView;

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
    private AppDatabase appDatabase;
    private ExoticPetDao exoticPetDao;
    public static Executor executor;

    //Booleans
    private boolean pictureTaken = false;

    //FireBase
    private FirebaseAnalytics mFirebaseAnalytics;
//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String TEXT = "sharedPrefs";
//    public static final String KEY_CONNECTIONS = "KEY_CONNECTIONS";

    //ViewModels
    private MainActivityViewModel mainActivityViewModel;


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
                                .apply(RequestOptions.circleCropTransform())
                                .into(defaultImageView);
                        cameraPicture = imageFile.getFile();
                        pictureTaken = true;
                        mAdapter.changePicture = false;
                    } else {
                        Glide.with(MainActivity.this)
                                .load(new File(String.valueOf(imageFile.getFile())))
                                .apply(RequestOptions.circleCropTransform())
                                .into(changePetPictureImageView);
                        cameraPicture1 = imageFile.getFile();
                        mAdapter.changeCameraPicture(cameraPicture1);
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

        //create new thread
        executor = Executors.newSingleThreadExecutor();
        //Database
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "pet_database")
                .fallbackToDestructiveMigration()
                .build();
        exoticPetDao = appDatabase.exoticPetDAO();

        executor.execute(() -> {
            exoticPets = (ArrayList<ExoticPet>) exoticPetDao.getAll();
        });



        //RecycleViews
        recyclerView = findViewById(R.id.recyclerv_view);

        //Views
        deletePetView = getLayoutInflater().inflate(R.layout.delete_pet_layout, null);
        changePetPictureView = getLayoutInflater().inflate(R.layout.change_pet_picture, null);
        changePetNameView = getLayoutInflater().inflate(R.layout.change_pet_name, null);
        addNotificationView = getLayoutInflater().inflate(R.layout.add_notification, null);


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

        //FireBase Analytics
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        createNotificationChannel();
        initViews();

//        Toast.makeText(MainActivity.this, exoticPets.size() + "", Toast.LENGTH_SHORT).show();


        instructionTextView.setText("Click add button\n  to create pet");
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

                String[] phone_numbers;
                //Spinner function
                String[] listOfAnimals = {"Choose Animal", "Arachnid", "Amphibian", "Reptile", "Insect", "Fish", "Other"};
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
                                            .load(R.drawable.spidercaca)
                                            .into(defaultImageView);
                                }
                                break;
                            case "Amphibian":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.frogtest)
                                            .into(defaultImageView);
                                }
                                break;
                            case "Reptile":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.ic_snake)
                                            .into(defaultImageView);

                                }
                                break;
                            case "Insect":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.ic_ant)
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
                                            .load(R.drawable.ic_paw_print)
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
                            Snackbar.make(view, "Choose Animal", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                        } else {
                            ExoticPet exoticPet = new ExoticPet(UUID.randomUUID().toString(),null, R.drawable.ladybug, null, null, null);

                            if (cameraPicture != null && pictureTaken == true) {
                                exoticPet.setCameraPicture(String.valueOf(cameraPicture));
                                exoticPet.setPetName(petNameEditText.getText().toString());
                                alertDialog.dismiss();
                            } else {
                                pictureTaken = false;
                                exoticPet.setPetName(petNameEditText.getText().toString());

                                switch (spinnerSelectedPet) {
                                    case "Arachnid":
                                        exoticPet.setPetImage(R.drawable.spidercaca);
                                        alertDialog.dismiss();
                                        break;
                                    case "Amphibian":
                                        exoticPet.setPetImage(R.drawable.frogtest);
                                        alertDialog.dismiss();
                                        break;
                                    case "Reptile":
                                        exoticPet.setPetImage(R.drawable.ic_snake);
                                        alertDialog.dismiss();
                                        break;
                                    case "Insect":
                                        exoticPet.setPetImage(R.drawable.ic_ant);
                                        alertDialog.dismiss();
                                        break;
                                    case "Fish":
                                        exoticPet.setPetImage(R.drawable.fish);
                                        alertDialog.dismiss();
                                        break;
                                    case "Other":
                                        exoticPet.setPetImage(R.drawable.ic_black_paw_print);
                                        alertDialog.dismiss();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "Type Pet Name", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }

                            pictureTaken = false;
                            exoticPets.add(exoticPet);
                            mAdapter.updatePets(exoticPets);
                            mAdapter.notifyItemInserted(exoticPets.size());
                            //Insert the data into offline Room on a seperate thread (highway) instead of the UI thread (The main highway)
                            executor.execute(() -> {
                                exoticPetDao.insertPet(exoticPet);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MyReminderChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    //This method sets up the RecycleView in the app
    private void initViews() {
        mAdapter = new RecyclerViewAdapter(this, this, exoticPets, changePetPictureView, cameraPicture1, deletePetView, addPetButton,changePetNameView,instructionTextView,addNotificationView);
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
//
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



