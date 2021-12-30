package com.example.exoticpets;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    public ArrayList<ExoticPet> selectedPetIdsToDeleteArrayList = new ArrayList<>();
    public ArrayList<ExoticPet> feedPet = new ArrayList<>();
    private ExoticPetDao exoticPetDao;

    public static Executor executor;
    private AppDatabase db;
    private Context mContext;
    private View deletePetView;
    private View changePetPictureView;
    private File   cameraPicture1;
    boolean changePicture = false;
    boolean isEnable = false;
    boolean isSelectAll = false;
    private ArrayList<String> exoticPetsFedDates = new ArrayList<>();




    public RecyclerViewAdapter(Context context, ArrayList<ExoticPet> exoticPets, View changePetPictureView, File cameraPicture1, View deletePetView) {
        this.mContext = context;
        this.exoticPets = exoticPets;
        this.changePetPictureView = changePetPictureView;
        this.cameraPicture1 = cameraPicture1;
        this.deletePetView = deletePetView;
    }

    //This method is responsible for inflating the view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //create new thread
        executor = Executors.newSingleThreadExecutor();
        //Database
        db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "pet_database")
                .fallbackToDestructiveMigration()
                .build();
        exoticPetDao = db.exoticPetDAO();
        return holder;
    }

    //This method gets called everytime a new item gets added to the list in RecycleView
    //Treat this as a for loop method
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {


        if (exoticPets.get(position).getWhenPetWasLastFed() != null) {
            holder.fedDateTextView.setVisibility(View.VISIBLE);
            holder.timeFedTextView.setVisibility(View.VISIBLE);
            holder.fedDateTextView.setText(exoticPets.get(position).getWhenPetWasLastFed());
            holder.timeFedTextView.setText(exoticPets.get(position).getTimePetWasLastFed());
        } else {
            holder.fedDateTextView.setVisibility(View.GONE);
            holder.timeFedTextView.setVisibility(View.GONE);
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
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        alertDialog.dismiss();
                        if(changePetPictureView.getParent() != null) {
                            ((ViewGroup)changePetPictureView.getParent()).removeView(changePetPictureView);
                        }
                    }
                });

                changePetPictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            Picasso.get().load(cameraPicture1).into(holder.pet_ImageView);

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

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setView(deletePetView);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    Button okButton = deletePetView.findViewById(R.id.ok_delete_pet);
                                    Button cancelButton = deletePetView.findViewById(R.id.cancel_delete_pet_alertDialog);
                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            for (ExoticPet exoticPet : selectedPetIdsToDeleteArrayList) {

                                                //Remove selected item from array list
                                                exoticPets.remove(exoticPet);
                                                executor.execute(() -> {
                                                    exoticPetDao.delete(exoticPet);
                                                });
                                            }
                                            alertDialog.dismiss();
                                            if(deletePetView.getParent() != null) {
                                                ((ViewGroup)deletePetView.getParent()).removeView(deletePetView);
                                            }
                                            notifyDataSetChanged();
                                            //Check condition
                                            //Finish action mode
                                            actionMode.finish();
                                        }
                                    });
                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                            if(deletePetView.getParent() != null) {
                                                ((ViewGroup)deletePetView.getParent()).removeView(deletePetView);
                                            }
                                            actionMode.finish();
                                        }
                                    });
                                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            alertDialog.dismiss();
                                            if(deletePetView.getParent() != null) {
                                                ((ViewGroup)deletePetView.getParent()).removeView(deletePetView);
                                            }
                                        }
                                    });
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

//                                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy h:mm aa");
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
                                    String formattedDate = df.format(c.getTime());
                                    Calendar c1 = Calendar.getInstance();
                                    SimpleDateFormat tf = new SimpleDateFormat("h:mm aa");
                                    String formattedTime = tf.format(c1.getTime());
                                        for (ExoticPet exoticPet : feedPet) {
                                        exoticPet.setWhenPetWasLastFed(formattedDate);
                                        exoticPet.setTimePetWasLastFed(formattedTime);
                                        executor.execute(() -> {
                                            exoticPetDao.updatePet(exoticPet);
                                        });
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
        TextView timeFedTextView;
        ImageView ivCheckBoxImageView;
        ImageView animalDetailsArrowImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            addPet = itemView.findViewById(R.id.add_pet_json);
            pet_ImageView = itemView.findViewById(R.id.pet_image_recycleview);
            petName = itemView.findViewById(R.id.image_name);
            ivCheckBoxImageView = itemView.findViewById(R.id.iv_check_box);
            animalDetailsArrowImageView = itemView.findViewById(R.id.animal_details_arrow);
            fedDateTextView = itemView.findViewById(R.id.fed_date);
            timeFedTextView = itemView.findViewById(R.id.timefed_textview);
        }
    }

    public void changeCameraPicture(File cameraPicture){
        this.cameraPicture1 = cameraPicture;
    }


    public void updateDisplayedPets(ArrayList<ExoticPet> searchedPets) {
        exoticPets = searchedPets;
    }

}
