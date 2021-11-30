package com.example.exoticpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private ImageView checkBoxImageView;
    private ImageView defaultImage;
    private ImageButton galleryImageButton;
    private ImageButton petImageButton;
    private TextView searchForPetTextview;
    private TextView instructionView;
    private EasyImage easyImage;
    private EasyImage easyImage1;
    private View changePetPictureView;
    private View view;
    private FloatingActionButton addPetButton;
    public static Toolbar searchPetToobar;
    private ArrayList<ExoticPet> mSearchedNamesArrayList = new ArrayList<>();
    private File cameraPicture;
    private boolean pictureTaken = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "sharedPrefs";
    public static final String KEY_CONNECTIONS = "KEY_CONNECTIONS";




    //Camera Feature
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CircleImageView changePetPicture = changePetPictureView.findViewById(R.id.change_pet_picture);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            //Loads user picture into imageView
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                for (MediaFile imageFile : imageFiles) {
                    if(!mAdapter.changePicture) {
                        Glide.with(MainActivity.this)
                                .load(new File(String.valueOf(imageFile.getFile())))
                                .into(defaultImage);
                        cameraPicture = imageFile.getFile();
                        pictureTaken = true;
                        break;
                    }else{
                        Glide.with(MainActivity.this)
                                .load(new File(String.valueOf(imageFile.getFile())))
                                .into(changePetPicture);
                        cameraPicture = imageFile.getFile();
                        mAdapter.changePicture = false;
                        break;
                    }
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
        //        menuImageView = findViewById(R.id.menu);
        changePetPictureView = getLayoutInflater().inflate(R.layout.change_pet_picture, null);
        animalDetailsArrowImageView = findViewById(R.id.animal_details_arrow);
        checkBoxImageView = findViewById(R.id.iv_check_box);
        searchPetToobar = findViewById(R.id.toolbar);
        addPetButton = findViewById(R.id.addPetButton1);
        instructionView = findViewById(R.id.instructionView);
        searchForPetTextview = findViewById(R.id.textview_searchForPet);
        ImageButton changePetCameraImageButton = changePetPictureView.findViewById(R.id.change_pet_camera_ImageButton);
        ImageButton changePetGalleryImageButton = changePetPictureView.findViewById(R.id.change_pet_gallery_ImageButton);
        AppCompatButton changePetPictureButton = changePetPictureView.findViewById(R.id.change_picture_button);


        loadData();
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


        changePetPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                                    if (!pictureTaken){
                                        Glide.with(MainActivity.this)
                                                .asBitmap()
                                                .load("https://opengameart.org/sites/default/files/styles/medium/public/SpiderEnemy.png")
                                                .into(defaultImage);
                                    }
                                    break;
                                case "Amphibian":
                                    if (!pictureTaken) {
                                        Glide.with(MainActivity.this)
                                                .asBitmap()
                                                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPw_xYUc0rjL5QuYa6CIEk7z1D7eH6BI5gsg&usqp=CAU")
                                                .into(defaultImage);
                                    }
                                    break;
                                case "Reptile":
                                    if (!pictureTaken) {
                                        Glide.with(MainActivity.this)
                                                .asBitmap()
                                                .load("https://image.shutterstock.com/image-vector/vector-illustration-cartoon-snake-pixel-260nw-398666929.jpg")
                                                .into(defaultImage);
                                    }
                                    break;
                                case "Insect":
                                    if (!pictureTaken) {
                                        Glide.with(MainActivity.this)
                                                .asBitmap()
                                                .load("https://art.pixilart.com/eb6f46cc7831237.gif")
                                                .into(defaultImage);
                                    }
                                    break;
                                case "Fish":
                                    if(!pictureTaken) {
                                        Glide.with(MainActivity.this)
                                                .asBitmap()
                                                .load("https://image.shutterstock.com/image-vector/fish-icon-pixel-style-animal-260nw-1789259792.jpg")
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

                        ExoticPet exoticPet = new ExoticPet(null,null, null, null);
//                        Random r = new Random();
////                        int randomIDNumber = r.nextInt(9999 - 1001) + 1001;
////                        exoticPet.setId(String.valueOf(randomIDNumber));

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
                                exoticPet.setPetImage(String.valueOf(cameraPicture));
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

                                pictureTaken = false;
                                exoticPets.add(exoticPet);
                                //mAdapter.add -> Adds a new item to the adapter


                                mAdapter.notifyDataSetChanged();
                                //NotifyDataSetChanged basically tells the adapter, "Hey man, we have new data. Please refresh the UI to reflect the new data"
                            }

                        }
                    }
                });
            }
        });
    }

    //This method sets up the RecycleView in the app
    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        mAdapter = new RecyclerViewAdapter(this, exoticPets,changePetPictureView,cameraPicture);
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
        saveData();
    }


    public void saveData(){

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exoticPets);
        editor.putString("pet list", json);
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

    }
    public void loadData(){
        Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("pet list", null);
        Type type = new TypeToken<ArrayList<ExoticPet>>(){}.getType();
        exoticPets = gson.fromJson(json, type);

        if (exoticPets == null){
            exoticPets = new ArrayList<>();
        }
    }



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



