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

public class AnimalDetails extends AppCompatActivity implements Serializable {
    Button cancelButton;
    Button okButton;
   ImageView deletePetImageView;
   View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_details);


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
                        Intent myIntent = new Intent(AnimalDetails.this, MainActivity.class);
                        AnimalDetails.this.startActivity(myIntent);
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



        getIncomingIntent();
        //Hides the action bar
//        getSupportActionBar().hide();
        //Hides title text from action bar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);



    }


    private void getIncomingIntent(){

        getIntent().getSerializableExtra("exotic_pets");
        getIntent().getSerializableExtra("image_url");
        getIntent().getSerializableExtra("image_name");

        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")){
            String imageUrl = getIntent().getStringExtra("image_url");
            String imageName = getIntent().getStringExtra("image_name");

            setImage(imageUrl, imageName);
        }
    }
    private void setImage(String imageUrl, String imageName){
        TextView selectedPet = findViewById(R.id.textview_selectedPet);
        selectedPet.setText(imageName);
        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }
}
