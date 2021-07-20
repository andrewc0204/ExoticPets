package com.example.exoticpets;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class  CustomSpinnerAdapter extends ArrayAdapter<String> {


    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }


    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }

    //Inheritance = Inheritening all methods, variables, etc from parent classes
    //Abstraction = 

}
