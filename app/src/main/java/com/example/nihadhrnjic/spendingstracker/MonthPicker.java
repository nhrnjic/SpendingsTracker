package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by nihadhrnjic on 6/25/17.
 */

public class MonthPicker extends DialogFragment {

    public static final String MONTH_INDEX_EXTRA = "month_name_extra";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] months = new String[] { "January", "February", "March", "April", "May",
        "June", "July", "August", "September","October", "November", "December" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose month")
                .setItems(months, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(MONTH_INDEX_EXTRA, which + 1);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                }).setNegativeButton("Cancel", null);

        return builder.create();
    }
}
