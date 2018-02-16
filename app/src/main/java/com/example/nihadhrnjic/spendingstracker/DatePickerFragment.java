package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nihadhrnjic on 6/24/17.
 */

public class DatePickerFragment extends DialogFragment {

    public static final String NEW_DATE_EXTRA = "new_date_extra";

    private DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);

        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker_id);

        builder.setTitle("Choose a date")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int day = mDatePicker.getDayOfMonth();
                        int month = mDatePicker.getMonth();
                        int year = mDatePicker.getYear();

                        DateTime dateTime = new DateTime(year, month+1, day, 0, 0);

                        Intent intent = new Intent();
                        intent.putExtra(NEW_DATE_EXTRA, dateTime);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }
}
