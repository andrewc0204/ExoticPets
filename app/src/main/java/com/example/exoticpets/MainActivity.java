package com.example.exoticpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
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
    private ArrayList<ExoticPet> exoticPets = new ArrayList<>();

    //The ghetto term for static is basically, access that shiz anywhere (Bad practice)


    //Vars
    public static RecyclerViewAdapter mAdapter;
    private ImageView animalDetailsArrowImageView;
    private ImageView defaultImage;
    CircleImageView petPictureImageView;
    CircleImageView changePetPicture;
    private AppCompatButton changePetPictureButton;
    private ImageButton galleryImageButton;
    private ImageButton petImageButton;
    private TextView searchForPetTextview;
    private TextView instructionView;
    private EasyImage easyImage;
    View recycleViewLayout;
    private View deletePetView;
    private View changePetPictureView;
    private View view;
    private FloatingActionButton addPetButton;
    public static Toolbar searchPetToobar;
    private ArrayList<ExoticPet> mSearchedNamesArrayList = new ArrayList<>();
    private File cameraPicture;
    private File cameraPicture1;
    private AppDatabase db;
    private ExoticPetDao exoticPetDao;
    public static Executor executor;
    private boolean pictureTaken = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "sharedPrefs";
    public static final String KEY_CONNECTIONS = "KEY_CONNECTIONS";
    private Context context;


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
                                .into(defaultImage);
                        cameraPicture = imageFile.getFile();

                        pictureTaken = true;
                    } else {
                        Glide.with(MainActivity.this)
                                .load(new File(String.valueOf(imageFile.getFile())))
                                .into(changePetPicture);
                        cameraPicture1 = imageFile.getFile();
                        mAdapter.changeCameraPicture(cameraPicture1);

                        mAdapter.changePicture = false;

                    }
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


        //create new thread
        executor = Executors.newSingleThreadExecutor();
        //Database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "pet_database").build();
        exoticPetDao = db.exoticPetDAO();
        executor.execute(() -> {
            exoticPets = (ArrayList<ExoticPet>) exoticPetDao.getAll();
        });

        //Vars
        //        menuImageView = findViewById(R.id.menu);
        deletePetView = getLayoutInflater().inflate(R.layout.delete_pet_layout, null);
        recycleViewLayout = getLayoutInflater().inflate(R.layout.layout_listitem, null);
        changePetPictureView = getLayoutInflater().inflate(R.layout.change_pet_picture, null);
        animalDetailsArrowImageView = findViewById(R.id.animal_details_arrow);
        petPictureImageView = recycleViewLayout.findViewById(R.id.pet_image_recycleview);
        searchPetToobar = findViewById(R.id.toolbar);
        addPetButton = findViewById(R.id.addPetButton1);
        changePetPicture = changePetPictureView.findViewById(R.id.change_pet_picture);
        instructionView = findViewById(R.id.instructionView);
        searchForPetTextview = findViewById(R.id.textview_searchForPet);
        ImageButton changePetCameraImageButton = changePetPictureView.findViewById(R.id.change_pet_camera_ImageButton);
        ImageButton changePetGalleryImageButton = changePetPictureView.findViewById(R.id.change_pet_gallery_ImageButton);
        changePetPictureButton = changePetPictureView.findViewById(R.id.change_picture_button);

        ExoticPet exoticPet = new ExoticPet(null, R.drawable.ladybug, null, null);

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
                Button closeBtn = view.findViewById(R.id.close_btn);
                galleryImageButton = view.findViewById(R.id.change_pet_gallery_ImageButton);
                petImageButton = view.findViewById(R.id.change_pet_camera_ImageButton);
                AppCompatButton createPetButton = view.findViewById(R.id.create_pet_button);
                EditText petNameEditText = view.findViewById(R.id.edittext_pet_name);
                defaultImage = view.findViewById(R.id.paw_imageview);
                Spinner spinner = view.findViewById(R.id.spinner);


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
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.spider)
                                            .into(defaultImage);
                                }
                                break;
                            case "Amphibian":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.frog)
                                            .into(defaultImage);
                                }
                                break;
                            case "Reptile":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.snake)
                                            .into(defaultImage);
                                }
                                break;
                            case "Insect":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.ladybug)
                                            .into(defaultImage);
                                }
                                break;
                            case "Fish":
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.fish)
                                            .into(defaultImage);
                                }
                                break;
                            default:
                                if (!pictureTaken) {
                                    Glide.with(MainActivity.this)
                                            .asBitmap()
                                            .load(R.drawable.ic_pawprint)
                                            .into(defaultImage);
                                }
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                /**
                 * Fiz: Then, i created a new Alert Dialog, and used the view object (create_pet_layout) to make the alert
                 */


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

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

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        pictureTaken = false;
                    }
                });

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        pictureTaken = false;
                    }
                });

                //Camera Button
                petImageButton.setOnClickListener(new View.OnClickListener() {
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

                        //When the user clicks the button, whatever code we write here will be run
                        //Checks to see if user type in a pet name
                        String spinnerSelectedPet = spinner.getSelectedItem().toString();
                        if (petNameEditText.getText().toString().isEmpty()) {
                            petNameEditText.setError("Please type pet name");
                            petNameEditText.requestFocus();
                        } else if (spinner.getSelectedItem().toString().equals("Choose Animal")) {
                            Toast.makeText(MainActivity.this, "Choose Animal", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cameraPicture != null) {
//                                exoticPet.setPetImage(cameraPicture);
                                exoticPets.add(exoticPet);
                                exoticPet.setPetName(petNameEditText.getText().toString());
                                instructionView.setVisibility(View.GONE);
                                //NotifyDataSetChanged basically tells the adapter, "Hey man, we have new data. Please refresh the UI to reflect the new data"
                                mAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            } else {
                                exoticPet.setPetName(petNameEditText.getText().toString());
                                //Sets text to disappear once the user adds a pet
                                instructionView.setVisibility(View.GONE);
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
                            //Insert the data into offline Room on a seperate thread (highway) instead of the UI thread (The main highway)
                            mAdapter.notifyDataSetChanged();
                            exoticPets.add(exoticPet);
                            executor.execute(() -> {
                                exoticPetDao.insertPet(exoticPet);
                            });




                        }
                    }
                });

            }
        });
    }



    //This method sets up the RecycleView in the app
    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        mAdapter = new RecyclerViewAdapter(this, exoticPets, changePetPictureView, cameraPicture1, deletePetView);
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


    @Override
    protected void onStop() {
        super.onStop();
//        saveData();
    }


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

//    public void getAllNames(){
//        ArrayList<String> arrayOfNames = new ArrayList<>();
//        arrayOfNames.add("Greg");
//        arrayOfNames.add("Charlie");
//        arrayOfNames.add("Bartholmoew");
//
//        return arrayOfNames;
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.feedpetmenu, menu);
//        return true;
//    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        ivCheckBoxImageView = findViewById(R.id.iv_check_box);
//        animalDetailsArrowImageView = findViewById(R.id.animal_details_arrow);
//
//        switch (item.getItemId()) {
//            case R.id.menu_delete:
//                for (ExoticPet exoticPet : mAdapter.selectedPetIdsToDeleteArrayList) {
//                    mAdapter.exoticPets.remove(exoticPet);
//                }
//                mAdapter.selectedPetIdsToDeleteArrayList.clear();
//                mAdapter.notifyDataSetChanged();
//                return true;
//
//            case R.id.menu_select_all:
//                //When click on select all, check condition
//                if (mAdapter.selectedPetIdsToDeleteArrayList.size() == exoticPets.size()) {
//                    //When all item selected
//                    //Set isSelectAll false
//                    isSelectAll = false;
//                    //Clear select array list
//                    mAdapter.selectedPetIdsToDeleteArrayList.clear();
//                } else {
//                    //When all item unselected
//                    //Set isSelectAll true
//                    isSelectAll = true;
//                    //Clear select array list
//                    mAdapter.selectedPetIdsToDeleteArrayList.clear();
//                    //Add all value in select array list
//                    mAdapter.selectedPetIdsToDeleteArrayList.addAll(exoticPets);
//                    //Check condition
//                }
//                mAdapter.notifyDataSetChanged();
//
//            default:
//
//                return super.onOptionsItemSelected(item);
//        }
//    }


}



