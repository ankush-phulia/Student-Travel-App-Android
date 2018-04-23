package com.col740.group9.studenttravelapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.col740.group9.studenttravelapp.R;

import static android.view.Gravity.BOTTOM;

public class CreateTravelFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_create_travel, null))
                .setPositiveButton("Journey", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // create new journey
                    }
                })
                .setNegativeButton("Trip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // create new trip
                    }
                });
        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        return dialog;
    }

    public void onResume() {
        getDialog().getWindow()
                .setLayout(
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels / 2
        );
        getDialog().getWindow().setGravity(BOTTOM);
        super.onResume();
    }

}
