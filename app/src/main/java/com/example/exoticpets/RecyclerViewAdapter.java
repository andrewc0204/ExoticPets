package com.example.exoticpets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    private Context mContext;
    private TextView searchPet;
    public ArrayList<ExoticPet> selectedPetIdsToDeleteArrayList = new ArrayList<>();


    public RecyclerViewAdapter(Context context, ArrayList<ExoticPet> exoticPets, TextView searchForPetTextView) {
        this.mContext = context;
        this.exoticPets = exoticPets;
        this.searchPet = searchForPetTextView;


    }

    //This method is responsible for inflating the view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //mainViewModel = ViewModelProviders.of((FragmentActivity)activity).get(MainViewModel.class);
        //Return view
        return holder;
    }

    //This method gets called everytime a new item gets added to the list in RecycleView
    //Treat this as a for loop method
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
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


//        ExoticPet currentItem = exoticPets.get(position);

        holder.petName.setText(exoticPets.get(position).getPetName());



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(mContext,mImageNames.get(position), Toast.LENGTH_SHORT).show();
//
//
//                Intent intent = new Intent(mContext, AnimalDetails.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("exotic_pet", exoticPets.get(position));
//                bundle.putSerializable("pet_picture", exoticPets.get(position).getPetImage());
//                bundle.putSerializable("pet_name", exoticPets.get(position).getPetName());
//
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//            }
//        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                currentItem.setSelected(!currentItem.isSelected());
//                Log.d(TAG, "onOptionsItemSelected: " + currentItem.getPetName() + " " + position);

//                holder.ivCheckBoxImageView.setVisibility(currentItem.isSelected() ? View.VISIBLE : View.GONE);
//                holder.animalDetailsArrowImageView.setVisibility(currentItem.isSelected() ? View.GONE : View.VISIBLE);

                holder.ivCheckBoxImageView.setVisibility(View.VISIBLE);
                searchPet.setVisibility(View.GONE);

                selectedPetIdsToDeleteArrayList.add(exoticPets.get(position));

                return true;
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
        TextView instructionView;
        ImageView ivCheckBoxImageView;
        ImageView animalDetailsArrowImageView;



        public ViewHolder(View itemView) {
            super(itemView);
            instructionView = itemView.findViewById(R.id.instructionView);
            pet_ImageView = itemView.findViewById(R.id.pet_image);
            petName = itemView.findViewById(R.id.image_name);
            ivCheckBoxImageView = itemView.findViewById(R.id.iv_check_box);
            ivCheckBoxImageView.setVisibility(View.GONE);
            animalDetailsArrowImageView = itemView.findViewById(R.id.animal_details_arrow);
        }
    }


    public void updateDisplayedPets(ArrayList<ExoticPet> searchedPets) {
        exoticPets = searchedPets;
    }
}
