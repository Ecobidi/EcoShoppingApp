package com.example.obidi.eco_shopping_mall.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import com.example.obidi.eco_shopping_mall.R;

public class ProgressDialog extends DialogFragment {
    public static final String TAG = ProgressDialog.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ProgressBar progressBar = (ProgressBar) inflater.inflate(R.layout.loading_indicator, null);
        return new AlertDialog.Builder(getContext())
                .setView(progressBar)
                .setCancelable(false)
                .show();
    }
}
