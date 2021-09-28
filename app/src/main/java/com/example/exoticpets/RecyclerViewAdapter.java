package com.example.exoticpets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    private Context mContext;




    public RecyclerViewAdapter(Context context, ArrayList<ExoticPet> exoticPets) {
        this.mContext = context;
        this.exoticPets = exoticPets;
    }

    //This method is responsible for inflating the view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    //This method gets called everytime a new item gets added to the list in RecycleView
    //Treat this as a for loop method
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /**
         * onStickDataToView
         *
         * This is what literally "sticks" the data to the UI (recyclerview)
         */

        /**
         * I'm going to get the first index/position/element of the arraylist you provided,
         * and use it to fill the layout_listitem that you provided me.
         *
         * Position 1 - ExoticPet exoticpet1; //Bob
         * Position 2 - ExoticPet exoticpet1; //Greg
         * Position 3 - ExoticPet exoticpet1; //Hafish
         */


        Glide.with(mContext)
                .asBitmap()
                .load(exoticPets.get(position).getPetImage())
                .into(holder.pet_ImageView);


        holder.petName.setText(exoticPets.get(position).getPetName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,mImageNames.get(position), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(mContext, AnimalDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("exotic_pet", exoticPets.get(position));
                bundle.putSerializable("pet_picture", exoticPets.get(position).getPetImage());
                bundle.putSerializable("pet_name", exoticPets.get(position).getPetName());

                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "Long Click", Toast.LENGTH_SHORT).show();
                holder.ivCheckBoxImageView.setVisibility(View.VISIBLE);
                holder.animalDetailsArrowImageView.setVisibility(View.GONE);


                return false;

            }
        });
    }




    //This tell the adapter how many list items are in the list
    @Override
    public int getItemCount() {
        return exoticPets.size();
    }

    //ViewHolder holds the each individual widget in memory
    //Where we initialize data
    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView pet_ImageView;
        TextView petName;
        ImageView ivCheckBoxImageView;
        ImageView animalDetailsArrowImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            pet_ImageView = itemView.findViewById(R.id.pet_image);
            petName = itemView.findViewById(R.id.image_name);
            ivCheckBoxImageView = itemView.findViewById(R.id.iv_check_box);
            animalDetailsArrowImageView = itemView.findViewById(R.id.animal_details_arrow);
        }
    }


    public void updateDisplayedPets(ArrayList<ExoticPet> searchedPets) {
        exoticPets = searchedPets;
    }
}
