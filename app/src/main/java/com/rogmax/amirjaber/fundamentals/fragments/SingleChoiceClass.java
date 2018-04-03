package com.rogmax.amirjaber.fundamentals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by Amir Jaber on 3/7/2018.
 */

public class SingleChoiceClass extends DialogFragment {

    final CharSequence[] items = {"Easy", "Medium", "Hard"};
    String selection;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Difficulty Level").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        selection = (String) items[which];

                        break;
                    case 1:
                        selection = (String) items[which];

                        break;
                    case 2:
                        selection = (String) items[which];

                        break;
                    default:
                        break;
                }
            }
        }).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Your Difficulty level is: " + selection, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}
