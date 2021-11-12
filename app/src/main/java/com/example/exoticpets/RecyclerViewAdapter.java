package com.example.exoticpets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    public ArrayList<ExoticPet> selectedPetIdsToDeleteArrayList = new ArrayList<>();
    public ArrayList<ExoticPet> feedPet = new ArrayList<>();
    private Context mContext;
    private Toolbar searchPetToolbar;
    boolean isEnable = false;
    boolean isSelectAll = false;


    public RecyclerViewAdapter(Context context, ArrayList<ExoticPet> exoticPets, Toolbar searchPetToolbar) {
        this.mContext = context;
        this.exoticPets = exoticPets;
        this.searchPetToolbar = searchPetToolbar;


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

//                currentItem.setSelected(!currentItem.isSelected());
//                Log.d(TAG, "onOptionsItemSelected: " + currentItem.getPetName() + " " + position);
//
//                holder.ivCheckBoxImageView.setVisibility(currentItem.isSelected() ? View.VISIBLE : View.GONE);
//                holder.animalDetailsArrowImageView.setVisibility(currentItem.isSelected() ? View.GONE : View.VISIBLE);
//
//                holder.ivCheckBoxImageView.setVisibility(View.VISIBLE);
//                searchPet.setVisibility(View.GONE);


                if (!isEnable) {
                    //When action mode is not enable
                    //Initialize action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            //Initialize menu inflater
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            //Inflate menu
                            menuInflater.inflate(R.menu.selectallmenu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            //When action mode is prepare, set isEnable true
                            isEnable = true;
                            ClickItem(holder);
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            //When click on action mode item, get item id
                            int id = menuItem.getItemId();
                            //When click on delete, user for loop
                            switch (id) {
                                case R.id.menu_delete:
                                    for (ExoticPet exoticPet : selectedPetIdsToDeleteArrayList) {
                                        //Remove selected item from array list
                                        exoticPets.remove(exoticPet);

                                    }
                                    //Check condition
                                    //Finish action mode
                                    actionMode.finish();
                                    break;
                                case R.id.menu_select_all:
                                    //When click on select all, check condition
                                    if (selectedPetIdsToDeleteArrayList.size() == exoticPets.size()) {
                                        //When all item selected
                                        //Set isSelectAll false
                                        isSelectAll = false;
                                        //Clear select array list
                                        selectedPetIdsToDeleteArrayList.clear();
                                    } else {
                                        //When all item unselected
                                        //Set isSelectAll true
                                        isSelectAll = true;
                                        //Clear select array list
                                        selectedPetIdsToDeleteArrayList.clear();
                                        //Add all value in select array list
                                        selectedPetIdsToDeleteArrayList.addAll(exoticPets);
                                    }
                                case R.id.feed_pet:
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
                                    String formattedDate = df.format(c.getTime());

                                    holder.fedDateTextView.setVisibility(View.VISIBLE);
                                    holder.fedDateTextView.setText(formattedDate);

                                    Log.d("TAG", "You fed me");
                                    //Notify adapter
                                    notifyDataSetChanged();
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            //When action mode is destroy, set isEnable false
                            isEnable = false;
                            //Set isSelectAll false
                            isSelectAll = false;
                            //Clear select array list
                            selectedPetIdsToDeleteArrayList.clear();
                            //Notify adapter
                            notifyDataSetChanged();
                        }
                    };
                    //Start action mode
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                } else {
                    //When action mode is already enable
                    //Call method
                    ClickItem(holder);
                }


                //If pet is selected
//                if (exoticPets.get(position).isSelected) {
//                    exoticPets.get(position).setSelected(false);
//                    holder.animalDetailsArrowImageView.setVisibility(View.VISIBLE);
//                    holder.ivCheckBoxImageView.setVisibility(View.GONE);
//                    selectedPetIdsToDeleteArrayList.remove(exoticPets.get(position));
//
//                } else {
//
//                    //If pet is not selected
//                    exoticPets.get(position).setSelected(true);
//                    holder.animalDetailsArrowImageView.setVisibility(View.GONE);
//                    holder.ivCheckBoxImageView.setVisibility(View.VISIBLE);
//                    selectedPetIdsToDeleteArrayList.add(exoticPets.get(position));
//                }

                return true;
            }
        });
        holder.ivCheckBoxImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    //When action mode is enable
                    //Call method
                    ClickItem(holder);
                } else {
                    //When action mode is not enable
                    //Display toast
                    Log.d("TAG", "You clicked me");
                }
            }
        });
        holder.animalDetailsArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    //When action mode is enable
                    //Call method
                    ClickItem(holder);
                } else {
                    //When action mode is not enable
                    //Display toast
                    Log.d("TAG", "You clicked me");
                }
            }
        });
        //Check condition
        if (isSelectAll) {
            //When all value selected
            //Visible all check box image
            holder.animalDetailsArrowImageView.setVisibility(View.GONE);
            holder.ivCheckBoxImageView.setVisibility(View.VISIBLE);
        } else {
            //When all value unselected
            //Hide all check box image
            holder.animalDetailsArrowImageView.setVisibility(View.VISIBLE);
            holder.ivCheckBoxImageView.setVisibility(View.GONE);
        }

    }


    private void ClickItem(ViewHolder holder) {
        //Get the selected item value
        ExoticPet pets = exoticPets.get(holder.getAbsoluteAdapterPosition());
        //Check condition
        if (holder.ivCheckBoxImageView.getVisibility() == View.GONE) {
            //When item not selected
            //Visible check box image
            //holder.toolbar.setVisibility(View.GONE);
            holder.animalDetailsArrowImageView.setVisibility(View.GONE);
            holder.ivCheckBoxImageView.setVisibility(View.VISIBLE);
            //Set background color
            selectedPetIdsToDeleteArrayList.add(pets);
        } else {
            //When item selected
            //Hide check box image
            holder.animalDetailsArrowImageView.setVisibility(View.VISIBLE);
            holder.ivCheckBoxImageView.setVisibility(View.GONE);
            //Set background color
            selectedPetIdsToDeleteArrayList.remove(pets);
        }
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
        TextView fedDateTextView;
        ImageView ivCheckBoxImageView;
        ImageView animalDetailsArrowImageView;


        public ViewHolder(View itemView) {
            super(itemView);

            pet_ImageView = itemView.findViewById(R.id.pet_image);
            petName = itemView.findViewById(R.id.image_name);
            ivCheckBoxImageView = itemView.findViewById(R.id.iv_check_box);
            animalDetailsArrowImageView = itemView.findViewById(R.id.animal_details_arrow);
            fedDateTextView = itemView.findViewById(R.id.fed_date);

        }
    }


    public void updateDisplayedPets(ArrayList<ExoticPet> searchedPets) {
        exoticPets = searchedPets;
    }
}
