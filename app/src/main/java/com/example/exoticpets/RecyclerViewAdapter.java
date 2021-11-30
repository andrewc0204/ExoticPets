package com.example.exoticpets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.bumptech.glide.Glide;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    public ArrayList<ExoticPet> selectedPetIdsToDeleteArrayList = new ArrayList<>();
    public ArrayList<ExoticPet> feedPet = new ArrayList<>();
    private Context mContext;
    private View changePetPictureView;
    private File cameraPicture;
    boolean changePicture = false;
    boolean isEnable = false;
    boolean isSelectAll = false;
    private ArrayList<String> exoticPetsFedDates = new ArrayList<>();




    public RecyclerViewAdapter(Context context, ArrayList<ExoticPet> exoticPets, View changePetPictureView, File cameraPicture) {
        this.mContext = context;
        this.exoticPets = exoticPets;
        this.changePetPictureView = changePetPictureView;
        this.cameraPicture = cameraPicture;
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


        if (exoticPets.get(position).getWhenPetWasLastFed() != null) {
            holder.fedDateTextView.setVisibility(View.VISIBLE);
            holder.fedDateTextView.setText(exoticPets.get(position).getWhenPetWasLastFed());
        } else {
            holder.fedDateTextView.setVisibility(View.GONE);
        }


        Glide.with(mContext)
                .asBitmap()
                .load(exoticPets.get(position).getPetImage())
                .into(holder.pet_ImageView);


        holder.petName.setText(exoticPets.get(position).getPetName());

        holder.pet_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatButton changePetPictureButton = changePetPictureView.findViewById(R.id.change_picture_button);
                Button closeBtn1 = changePetPictureView.findViewById(R.id.close_btn1);
                CircleImageView changePetPictureImageView = changePetPictureView.findViewById(R.id.change_pet_picture);

                changePicture = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(changePetPictureView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Glide.with(mContext)
                        .asBitmap()
                        .load(exoticPets.get(position).getPetImage())
                        .into(changePetPictureImageView);

                closeBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                changePetPictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExoticPet exoticPet = new ExoticPet(null,null,null,null);
                        exoticPet.setPetImage(String.valueOf(cameraPicture));
                        alertDialog.dismiss();
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
                                    if (selectedPetIdsToDeleteArrayList.size() == exoticPets.size() || feedPet.size() == exoticPets.size()) {
                                        //When all item selected
                                        //Set isSelectAll false
                                        isSelectAll = false;
                                        //Clear select array list
                                        feedPet.clear();
                                        selectedPetIdsToDeleteArrayList.clear();
                                        notifyDataSetChanged();
                                        break;
                                    } else {
                                        //When all item unselected
                                        //Set isSelectAll true
                                        isSelectAll = true;
                                        //Clear select array list
                                        selectedPetIdsToDeleteArrayList.clear();
                                        //Add all value in select array list
                                        feedPet.addAll(exoticPets);
                                        selectedPetIdsToDeleteArrayList.addAll(exoticPets);
                                        notifyDataSetChanged();
                                        break;
                                    }


                                case R.id.feed_pet:

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy HH:mm aa");
                                    String formattedDate = df.format(c.getTime());
                                    for (ExoticPet exoticPet : feedPet) {
                                        exoticPet.setWhenPetWasLastFed(formattedDate);
                                    }
                                    //Check
                                    actionMode.finish();

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
                            feedPet.clear();
                            selectedPetIdsToDeleteArrayList.clear();
                            //Notify adapter
                            notifyDataSetChanged();
                        }
                    };
                    //Start action mode
                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                } else {
                    //When action mode is already enable
                    //Call method
                    ClickItem(holder);
                }


//                Intent intent = new Intent(mContext, AnimalDetails.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("exotic_pet", exoticPets.get(position));
//                bundle.putSerializable("pet_picture", exoticPets.get(position).getPetImage());
//                bundle.putSerializable("pet_name", exoticPets.get(position).getPetName());
//
//
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return true;
            }
        });
        holder.addPet.setOnClickListener(new View.OnClickListener() {
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
            holder.addPet.playAnimation();
            holder.addPet.setVisibility(View.VISIBLE);
        } else {
            //When all value unselected
            //Hide all check box image
            holder.animalDetailsArrowImageView.setVisibility(View.VISIBLE);
            holder.addPet.setVisibility(View.GONE);
        }

    }


    private void ClickItem(ViewHolder holder) {
        //Get the selected item value
        ExoticPet pets = exoticPets.get(holder.getAbsoluteAdapterPosition());
        //Check condition
        if (holder.addPet.getVisibility() == View.GONE) {
            //When item not selected
            //Visible check box image
            //holder.toolbar.setVisibility(View.GONE);
            holder.animalDetailsArrowImageView.setVisibility(View.GONE);
            holder.addPet.playAnimation();
            holder.addPet.setVisibility(View.VISIBLE);
            //Set background color
            feedPet.add(pets);
            selectedPetIdsToDeleteArrayList.add(pets);
        } else {
            //When item selected
            //Hide check box image
            holder.animalDetailsArrowImageView.setVisibility(View.VISIBLE);
            holder.addPet.setVisibility(View.GONE);
            //Set background color
            feedPet.remove(pets);
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


        LottieAnimationView addPet;
        CircleImageView pet_ImageView;
        TextView petName;
        TextView fedDateTextView;
        ImageView ivCheckBoxImageView;
        ImageView animalDetailsArrowImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            addPet = itemView.findViewById(R.id.add_pet_json);
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

//    public void updatePetsLastFed(){
//        exoticPetsFedDates = new ArrayList<>();
//        for (ExoticPet exoticPet : exoticPets){
//            if (exoticPet.getWhenPetWasLastFed() != null){
//                exoticPetsFedDates.add(exoticPet.getWhenPetWasLastFed());
//            }
//        }
//
////        System.out.println("");
////        Collections.sort(exoticPets, byDate);
////        System.out.println("");
//    }

//    static final Comparator<ExoticPet> byDate = new Comparator<ExoticPet>() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
//
//        public int compare(ExoticPet ord1, ExoticPet ord2) {
//            Date d1 = null;
//            Date d2 = null;
//            try {
//                d1 = (Date) sdf.parse(ord1.getWhenPetWasLastFed());
//                d2 = (Date) sdf.parse(ord2.getWhenPetWasLastFed());
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//
//            //return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
//            return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
//        }
//    };
}
