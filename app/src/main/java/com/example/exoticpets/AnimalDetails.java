package com.example.exoticpets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import static com.example.exoticpets.MainActivity.mAdapter;

public class AnimalDetails extends AppCompatActivity implements Serializable {
    Button cancelButton;
    Button okButton;
    ImageView backImageView;
    ImageView deletePetImageView;
    View view;
    ExoticPet selectedPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_details);

        getIncomingIntent();

        backImageView = findViewById(R.id.back_imageview);
        deletePetImageView = findViewById(R.id.delete_pet);


        deletePetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = getLayoutInflater().inflate(R.layout.delete_pet_layout, null);
                cancelButton = view.findViewById(R.id.cancel_delete_pet_alertDialog);
                okButton = view.findViewById(R.id.ok_delete_pet);
                /**
                 * Fiz: Then, i created a new Alert Dialog, and used the view object (create_pet_layout) to make the alert
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(AnimalDetails.this);

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
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *
                         * selectedPet.getID = 1925
                         * 1 - Wally.getID (1254) = selectedPet.getID(1925)
                         * 2 - Hanna(1059) = selectedPet.getid(1925)
                         * 3 - Hafiz.getID(1925) the fish = selectedPet.getid(1925)
                         */
                        //Removes item from recycleview
                        for (ExoticPet exoticPet : mAdapter.exoticPets) {
                            if (exoticPet.getId() == (selectedPet.getId())) {//True
                                mAdapter.exoticPets.remove(exoticPet);
                                mAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        onBackPressed();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

      backImageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(AnimalDetails.this, MainActivity.class);
              startActivity(intent);
          }
      });
        //Hides the action bar
        //getSupportActionBar().hide();
        //Hides title text from action bar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void getIncomingIntent() {

        getIntent().getSerializableExtra("pet_picture");
        getIntent().getSerializableExtra("pet_name");
        selectedPet = (ExoticPet) getIntent().getSerializableExtra("exotic_pet");

        if (getIntent().hasExtra("pet_picture") && getIntent().hasExtra("pet_name")) {
            String imageUrl = getIntent().getStringExtra("pet_picture");
            String imageName = getIntent().getStringExtra("pet_name");
            setImage(imageUrl, imageName);
        }

    }

    private void setImage(String imageUrl, String imageName) {
        TextView selectedPetName = findViewById(R.id.textview_selectedPet);
        selectedPetName.setText(imageName);
        ImageView image = findViewById(R.id.pet_image_recycleview);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }
}
