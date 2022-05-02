package com.kurbcreations.exoticpets.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.kurbcreations.exoticpets.MainActivity;
import com.kurbcreations.exoticpets.database.AppDatabase;
import com.kurbcreations.exoticpets.models.ExoticPet;
import com.kurbcreations.exoticpets.database.ExoticPetDao;
import com.kurbcreations.exoticpets.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executor;

import java.util.concurrent.Executors;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ActionMode mActionMode;


    //ArrayLists
    public ArrayList<ExoticPet> exoticPets;
    public ArrayList<ExoticPet> selectedPetIdsArrayList = new ArrayList<>();
    public ArrayList<ExoticPet> feedPet = new ArrayList<>();
    public ArrayList<ExoticPet> quickFeedPets = new ArrayList<>();


    //Room
    private ExoticPetDao exoticPetDao;
    public static Executor executor;
    private AppDatabase db;

    //Context
    private Context mContext;
    MainActivity mainActivity;

    //Views
    private View deletePetView;
    private View changePetPictureView;
    private View changePetNameView;

    //EasyImage
    private File cameraPicture1;

    //Buttons
    private FloatingActionButton addPetButton;

    //TextViews
    private TextView instructionTextView;

    //Booleans
    public boolean changePicture = false;
    public boolean isCardViewClicked = false;
    boolean isSelectAll = false;


    public RecyclerViewAdapter(Context context, MainActivity mainActivity, ArrayList<ExoticPet> exoticPets, View changePetPictureView, File cameraPicture1,
                               View deletePetView, FloatingActionButton addPetButton, View changePetNameView, TextView instructionTextView) {
        this.mContext = context;
        this.exoticPets = exoticPets;
        this.changePetPictureView = changePetPictureView;
        this.cameraPicture1 = cameraPicture1;
        this.deletePetView = deletePetView;
        this.addPetButton = addPetButton;
        this.changePetNameView = changePetNameView;
        this.mainActivity = mainActivity;
        this.instructionTextView = instructionTextView;

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
                .build();
        exoticPetDao = db.exoticPetDAO();

        return holder;
    }

    //This method gets called everytime a new item gets added to the list in RecycleView
    //Treat this as a for loop method
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        //Sets the Feed date visibility
        if (exoticPets.get(position).getDatePetWasLastFed() != null) {
            holder.fedDateTextView.setVisibility(View.VISIBLE);
            holder.fedDateTextView.setText(exoticPets.get(position).getDatePetWasLastFed());
        } else {
            holder.fedDateTextView.setVisibility(View.GONE);
        }

        if (exoticPets.get(position).getTimePetWasLastFed() != null) {
            holder.timeFedTextView.setVisibility(View.VISIBLE);
            holder.timeFedTextView.setText(exoticPets.get(position).getTimePetWasLastFed());
        } else {
            holder.timeFedTextView.setVisibility(View.GONE);
        }


        //Sets the pet picture
        if (exoticPets.get(position).getCameraPicture() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(exoticPets.get(position).getCameraPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.pet_ImageView);
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(exoticPets.get(position).getPetImage())
                    .into(holder.pet_ImageView);
        }

        //Sets the instruction view visiblity
        if (exoticPets.isEmpty()) {
            instructionTextView.setVisibility(View.VISIBLE);
        } else {
            instructionTextView.setVisibility(View.GONE);
        }

        holder.petNameTextView.setText(exoticPets.get(position).getPetName());


        //Changes Pet Name
        holder.petNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCardViewClicked) {
                    holder.petNameTextView.setClickable(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(changePetNameView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Button okChangePetNameButton = changePetNameView.findViewById(R.id.ok_change_name_button);
                    Button cancelNameChangeButton = changePetNameView.findViewById(R.id.cancel_change_petname_alertDialog);
                    EditText changePetNameEditText = changePetNameView.findViewById(R.id.edittext_pet_name);

                    okChangePetNameButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (changePetNameEditText.getText().toString().isEmpty()) {
                                changePetNameEditText.setError("Please type pet name");
                                changePetNameEditText.requestFocus();

                                //Checks to see if user picked a pet type
                            } else {
                                for (ExoticPet exoticPet : exoticPets) {
                                    exoticPets.get(position).setPetName(changePetNameEditText.getText().toString());
                                    holder.petNameTextView.setText(changePetNameEditText.getText().toString());
                                    executor.execute(() -> {
                                        exoticPetDao.updateName(exoticPet.getPetName(), exoticPet.getSecondId());
                                    });
                                }
                                alertDialog.dismiss();
                            }
                        }
                    });

                    cancelNameChangeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            if (changePetNameView.getParent() != null) {
                                ((ViewGroup) changePetNameView.getParent()).removeView(changePetNameView);
                            }
                        }
                    });

                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            alertDialog.dismiss();
                            if (changePetNameView.getParent() != null) {
                                ((ViewGroup) changePetNameView.getParent()).removeView(changePetNameView);
                            }
                        }
                    });
                }
            }
        });

        //Quick Feed function
        holder.quickfeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCardViewClicked) {
                    holder.quickfeedButton.setClickable(false);
                    holder.fedDateTextView.setVisibility(View.VISIBLE);
                    holder.timeFedTextView.setVisibility(View.VISIBLE);
                    ExoticPet quickFeedPet = exoticPets.get(holder.getAbsoluteAdapterPosition());
                    quickFeedPets.add(quickFeedPet);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
                    String formattedDate = df.format(c.getTime());
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat tf = new SimpleDateFormat("h:mm aa");
                    String formattedTime = tf.format(c1.getTime());
                    for (ExoticPet exoticPet : quickFeedPets) {
                        exoticPet.setDatePetWasLastFed(formattedDate);
                        exoticPet.setTimePetWasLastFed(formattedTime);
                        holder.fedDateTextView.setText(exoticPets.get(position).getDatePetWasLastFed());
                        holder.timeFedTextView.setText(exoticPets.get(position).getTimePetWasLastFed());
                        quickFeedPets.remove(quickFeedPet);
                        executor.execute(() -> {
                            exoticPetDao.updateDateFed(exoticPet.getDatePetWasLastFed(), exoticPet.getSecondId());
                            exoticPetDao.updateTimeFed(exoticPet.getTimePetWasLastFed(), exoticPet.getSecondId());
                        });
                    }
                }
            }
        });

        holder.lastFedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCardViewClicked) {
                    holder.lastFedButton.setClickable(false);
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {

                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, day);
                            SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
                            String formattedDate = df.format(calendar.getTime());

                            for (ExoticPet exoticPet : exoticPets) {
                                exoticPets.get(position).setDatePetWasLastFed(formattedDate);
                                holder.fedDateTextView.setText(exoticPets.get(position).getDatePetWasLastFed());
                                notifyDataSetChanged();
                                executor.execute(() -> {
                                    exoticPetDao.updateDateFed(exoticPet.getDatePetWasLastFed(), exoticPet.getSecondId());
                                });
                            }
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        holder.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCardViewClicked) {
                    holder.timeButton.setClickable(false);
                    Calendar currentTime = Calendar.getInstance();
                    int min = currentTime.get(Calendar.MINUTE);
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                            String choosedHour = "";
                            String choosedMinute = "";
                            String choosedTimeZone = "";

                            if (selectedHour > 12) {
                                choosedTimeZone = "PM";
                                selectedHour = selectedHour - 12;
                                if (selectedHour < 10) {
                                    choosedHour = "" + selectedHour;
                                } else {
                                    choosedHour = "" + selectedHour;
                                }
                            } else {
                                choosedTimeZone = "AM";
                                if (selectedHour < 10) {
                                    choosedHour = "" + selectedHour;
                                } else {
                                    choosedHour = "" + selectedHour;
                                }
                            }
                            if (selectedMinute < 10) {
                                choosedMinute = "0" + selectedMinute;
                            } else {
                                choosedMinute = "" + selectedMinute;
                            }

                            String time = choosedHour + ":" + choosedMinute + " " + choosedTimeZone;

                            for (ExoticPet exoticPet : exoticPets) {
                                exoticPets.get(position).setTimePetWasLastFed(time);
                                holder.timeFedTextView.setText(exoticPets.get(position).getTimePetWasLastFed());
                                notifyDataSetChanged();
                                executor.execute(() -> {
                                    exoticPetDao.updateTimeFed(exoticPet.getTimePetWasLastFed(), exoticPet.getSecondId());
                                });
                            }
                        }
                    }, hour, min, false);
                    timePickerDialog.show();
                }
            }
        });

        //Changes the pet picture
        holder.pet_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCardViewClicked) {
                    holder.pet_ImageView.setClickable(false);
                    AppCompatButton changePetPictureButton = changePetPictureView.findViewById(R.id.change_picture_button);
                    Button closeBtn1 = changePetPictureView.findViewById(R.id.close_btn1);
                    ImageView changePetPictureImageView = changePetPictureView.findViewById(R.id.change_pet_picture);

                    changePicture = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(changePetPictureView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    if (exoticPets.get(position).getCameraPicture() != null) {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(exoticPets.get(position).getCameraPicture())
                                .apply(RequestOptions.circleCropTransform())
                                .into(changePetPictureImageView);
                    } else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(exoticPets.get(position).getPetImage())
                                .into(changePetPictureImageView);
                    }

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
                            if (changePetPictureView.getParent() != null) {
                                ((ViewGroup) changePetPictureView.getParent()).removeView(changePetPictureView);
                            }
                        }
                    });

                    changePetPictureButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cameraPicture1 != null) {
                                for (ExoticPet exoticPet : exoticPets) {
                                    Glide.with(mContext)
                                            .load(cameraPicture1)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(holder.pet_ImageView);


                                    exoticPets.get(position).setCameraPicture(String.valueOf(cameraPicture1));
                                    executor.execute(() -> {
                                        exoticPetDao.updatePetPicture(exoticPet.getCameraPicture(), exoticPet.getSecondId());
                                    });
                                    alertDialog.dismiss();
                                }
                            } else {
                                Snackbar.make(v, "Please take a picture", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
//                            Toast.makeText(mContext, "Please take a picture", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        //Enables user to click multiple of pets
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addPetButton.setVisibility(View.GONE);
                if (!isCardViewClicked) {

                    isCardViewClicked = true;
                    //When action mode is not enable
                    //Initialize action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            //Initialize menu inflater
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            //Inflate menu
                            menuInflater.inflate(R.menu.selectallmenu, menu);
                           mActionMode = actionMode;
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            ClickItem(holder);

                            if (selectedPetIdsArrayList.isEmpty()){
                                actionMode.finish();
                            }
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                            //When click on action mode item, get item id
                            int id = menuItem.getItemId();
                            //When click on delete, user for loop
                            switch (id) {
                                case R.id.menu_delete:
                                    TextView deletePetTextView = deletePetView.findViewById(R.id.title_textview);
                                    Button okButton = deletePetView.findViewById(R.id.ok_delete_pet);
                                    Button cancelButton = deletePetView.findViewById(R.id.cancel_delete_pet_alertDialog);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setView(deletePetView);
                                    AlertDialog alertDialog = builder.create();

                                    if (selectedPetIdsArrayList.isEmpty()) {
                                        Snackbar.make(view, "Please click on a pet", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    } else if (selectedPetIdsArrayList.size() > 1) {
                                        alertDialog.show();
                                        deletePetTextView.setText("Are you sure you want to delete these pets?");
                                    } else {
                                        alertDialog.show();
                                        deletePetTextView.setText("Are you sure you want to delete this pet?");
                                    }

                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            for (ExoticPet exoticPet : selectedPetIdsArrayList) {
                                                //Remove selected item from array list
                                                exoticPets.remove(exoticPet);
                                                executor.execute(() -> {
                                                    exoticPetDao.deleteBySecondID(exoticPet.getSecondId());
                                                });
                                                notifyDataSetChanged();
                                            }


                                            if (deletePetView.getParent() != null) {
                                                ((ViewGroup) deletePetView.getParent()).removeView(deletePetView);
                                            }

                                            if (exoticPets.isEmpty()) {
                                                instructionTextView.setVisibility(View.VISIBLE);
                                            } else {
                                                instructionTextView.setVisibility(View.GONE);
                                            }
                                            notifyDataSetChanged();
                                            //Check condition
                                            //Finish action mode
                                            actionMode.finish();
                                            alertDialog.dismiss();
                                        }
                                    });

                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                            if (deletePetView.getParent() != null) {
                                                ((ViewGroup) deletePetView.getParent()).removeView(deletePetView);
                                            }
                                            actionMode.finish();
                                        }
                                    });

                                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            alertDialog.dismiss();
                                            if (deletePetView.getParent() != null) {
                                                ((ViewGroup) deletePetView.getParent()).removeView(deletePetView);
                                            }
                                        }
                                    });
                                    break;

                                case R.id.menu_select_all:
                                    //When click on select all, check condition
                                    if (selectedPetIdsArrayList.size() == exoticPets.size() || feedPet.size() == exoticPets.size()) {
                                        //When all item selected
                                        //Set isSelectAll false
                                        isSelectAll = false;
                                        //Clear select array list
                                        feedPet.clear();
                                        selectedPetIdsArrayList.clear();
                                        notifyDataSetChanged();
                                        break;
                                    } else {
                                        //When all item unselected
                                        //Set isSelectAll true
                                        isSelectAll = true;
                                        //Clear select array list
                                        selectedPetIdsArrayList.clear();
                                        feedPet.clear();
                                        //Add all value in select array list
                                        feedPet.addAll(exoticPets);
                                        selectedPetIdsArrayList.addAll(exoticPets);
                                        notifyDataSetChanged();
                                        break;
                                    }

                                case R.id.feed_pet:

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
                                    String formattedDate = df.format(c.getTime());
                                    Calendar c1 = Calendar.getInstance();
                                    SimpleDateFormat tf = new SimpleDateFormat("h:mm aa");
                                    String formattedTime = tf.format(c1.getTime());
                                    for (ExoticPet exoticPet : feedPet) {
                                        exoticPet.setDatePetWasLastFed(formattedDate);
                                        exoticPet.setTimePetWasLastFed(formattedTime);
                                        executor.execute(() -> {
                                            exoticPetDao.updateDateFed(exoticPet.getDatePetWasLastFed(), exoticPet.getSecondId());
                                            exoticPetDao.updateTimeFed(exoticPet.getTimePetWasLastFed(), exoticPet.getSecondId());
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
                            isCardViewClicked = false;
                            //Set isSelectAll false
                            isSelectAll = false;
                            //Clear select array list
                            feedPet.clear();
                            selectedPetIdsArrayList.clear();
                            mActionMode = null;

                            addPetButton.setVisibility(View.VISIBLE);
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


            }
        });

//        holder.addPetAnimImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isEnable) {
//                    //When action mode is enable
//                    //Call method
//                    ClickItem(holder);
//                } else {
//                    //When action mode is not enable
//                    //Display toast
//                    Log.d("TAG", "You clicked me");
//                }
//            }
//        });
//        holder.checkBoxMainImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isEnable) {
//                    //When action mode is enable
//                    //Call method
//                    ClickItem(holder);
//                } else {
//                    //When action mode is not enable
//                    //Display toast
//                    Log.d("TAG", "You clicked me");
//                }
//            }
//        });


        //Check condition
        if (isSelectAll) {
            //When all value selected
            //Visible all check box image
            holder.checkBoxMainImageView.setVisibility(View.GONE);
            holder.addPetAnimImageView.playAnimation();
            holder.addPetAnimImageView.setVisibility(View.VISIBLE);
        } else {
            //When all value unselected
            //Hide all check box image
            holder.checkBoxMainImageView.setVisibility(View.VISIBLE);
            holder.addPetAnimImageView.setVisibility(View.GONE);
        }
    }


    private void ClickItem(ViewHolder holder) {

        //Get the selected item value
        ExoticPet pets = exoticPets.get(holder.getAbsoluteAdapterPosition());
        //Check condition
        if (holder.addPetAnimImageView.getVisibility() == View.GONE) {
            //When item not selected
            //Visible check box image
            holder.checkBoxMainImageView.setVisibility(View.GONE);
            holder.addPetAnimImageView.playAnimation();
            holder.addPetAnimImageView.setVisibility(View.VISIBLE);

            feedPet.add(pets);
            selectedPetIdsArrayList.add(pets);
        } else {
            //When item selected
            //Hide check box image
            holder.checkBoxMainImageView.setVisibility(View.VISIBLE);
            holder.addPetAnimImageView.setVisibility(View.GONE);
            feedPet.remove(pets);
            selectedPetIdsArrayList.remove(pets);
            if (mActionMode != null && selectedPetIdsArrayList.isEmpty()){
                mActionMode.finish();
            }
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

        LottieAnimationView addPetAnimImageView;
        ImageView pet_ImageView;
        TextView petNameTextView;
        TextView fedDateTextView;
        TextView timeFedTextView;
        ImageView checkBoxMainImageView;
        ImageView checkBoxSecondaryImageView;
        ImageView animalDetailsArrowImageView;
        Button quickfeedButton;
        Button timeButton;
        Button lastFedButton;


        public ViewHolder(View itemView) {
            super(itemView);

            pet_ImageView = itemView.findViewById(R.id.pet_picture_imageview);
            addPetAnimImageView = itemView.findViewById(R.id.add_pet_json);
            animalDetailsArrowImageView = itemView.findViewById(R.id.animal_details_arrow);
            checkBoxMainImageView = itemView.findViewById(R.id.iv_check_box_main);
            checkBoxSecondaryImageView = itemView.findViewById(R.id.iv_check_box_secondary);
            petNameTextView = itemView.findViewById(R.id.pet_name_textview);
            fedDateTextView = itemView.findViewById(R.id.fed_date);
            timeFedTextView = itemView.findViewById(R.id.timefed_textview);
            quickfeedButton = itemView.findViewById(R.id.quick_feed_button);
            timeButton = itemView.findViewById(R.id.time_button);
            lastFedButton = itemView.findViewById(R.id.last_fed_button);
        }
    }

    public void changeCameraPicture(File cameraPicture) {
        this.cameraPicture1 = cameraPicture;
    }

    public void updateDisplayedPets(ArrayList<ExoticPet> searchedPets) {
        exoticPets = searchedPets;
    }

    public void updatePets(ArrayList<ExoticPet> updatePet) {
        exoticPets = updatePet;
    }

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