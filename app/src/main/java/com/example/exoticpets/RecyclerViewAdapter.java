package com.example.exoticpets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";


//    private ArrayList<String> mPetNames = new ArrayList<>();
//    private ArrayList<String> mPetImages = new ArrayList<>();
    private ArrayList<ExoticPet> exoticPets = new ArrayList<>();
    private Context mContext;


    public RecyclerViewAdapter(Context context, ArrayList<ExoticPet> exoticPets) {
        this.mContext = context;
        this.exoticPets = exoticPets;
    }

    //    public RecyclerViewAdapter(Context  context, ArrayList<String> imageNames, ArrayList<String> images) {
//        mPetNames = imageNames;
//        mPetImages = images;
//        mContext = context;
//
//
//
//    }


    //This method is responsible for inflating the view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    //This method gets called everytime a new item gets added to the list in RecycleView
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(exoticPets.get(position).getPetImage())
                .into(holder.image);


        holder.imageName.setText(exoticPets.get(position).getPetName());



        //Position = 0 -> Orchid Mantis

        //mImageNames
        //0 = Orchid Mantis
        //1 = Bold Jumping Spider
        //2 = Bearded Dragon
        //3 = Leopard Gecko

        //mImages
        //0 = Image of Orchid Mantis
        //1 = Bold Jumping Spider
        //2 Image of Bearded Dragon

        //holder 1 [First Row] -> Name:Orchid Mantis , Image: Orchid Mantis Image
        //holder.imageName.setText.(mImageNames.

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(mContext,mImageNames.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, AnimalDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("exotic_pets", exoticPets.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);

//                intent.putExtra("image_url", mPetImages.get(position));
//                intent.putExtra("image_name", mPetNames.get(position));
//                mContext.startActivity(intent);
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
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void updateDisplayedPets(ArrayList<ExoticPet> searchedPets){
        exoticPets = searchedPets;
    }
}
