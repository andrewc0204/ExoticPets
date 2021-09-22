package com.example.exoticpets;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.exoticpets.MainActivity.mAdapter;

public class AnimalDetails extends AppCompatActivity implements Serializable {
    Button cancelButton;
    Button okButton;
   ImageView deletePetImageView;
   View view;
   ExoticPet selectedPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_details);

        getIncomingIntent();
        deletePetImageView = findViewById(R.id.delete_pet);




        deletePetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = getLayoutInflater().inflate(R.layout.delete_pet_layout, null);
                cancelButton = view.findViewById(R.id.cancel_delete_pet_alertDialog);
                okButton = view.findViewById(R.id.ok);
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
                alertDialog.show();

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent myIntent = new Intent(AnimalDetails.this, MainActivity.class);
//                        AnimalDetails.this.startActivity(myIntent);

                        /**
                         *
                         * selectedPet.getID = 1925
                         *
                         * 1 - Wally.getID (1254) = selectedPet.getID(1925)
                         * 2 - Hanna(1059) = selectedPet.getid(1925)
                         * 3 - Hafiz.getID(1925) the fish = selectedPet.getid(1925)
                         *
                         *
                         *
                         */
                        //Removes item from recycleview
                        for (ExoticPet exoticPet : mAdapter.exoticPets){
                            if (exoticPet.getId().equals(selectedPet.getId())){//True
                                mAdapter.exoticPets.remove(exoticPet);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

//
                        //Arkansas and Kansas scenario
//                        mAdapter.exoticPets.remove(selectedPet);
//                        mAdapter.notifyDataSetChanged();
                        onBackPressed();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });



        //Hides the action bar
//        getSupportActionBar().hide();
        //Hides title text from action bar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getIncomingIntent(){
//       getIntent().getSerializableExtra("exotic_pet");
        getIntent().getSerializableExtra("pet_picture");
        getIntent().getSerializableExtra("pet_name");
        selectedPet = (ExoticPet) getIntent().getSerializableExtra("exotic_pet");

        if(getIntent().hasExtra("pet_picture") && getIntent().hasExtra("pet_name")){
            String imageUrl = getIntent().getStringExtra("pet_picture");
            String imageName = getIntent().getStringExtra("pet_name");
            setImage(imageUrl, imageName);
        }


    }
    private void setImage(String imageUrl, String imageName){
        TextView selectedPetName = findViewById(R.id.textview_selectedPet);
        selectedPetName.setText(imageName);
        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }
}
