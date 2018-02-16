package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.MonthExtra;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

/**
 * Created by nihadhrnjic on 6/25/17.
 */

public class MonthPicker extends DialogFragment {

    private TextView YearText;
    private ImageView PrevYear;
    private ImageView NextYear;
    private TextView mCancel;
    private int year = DateTime.now().getYear();
    private String[] months = new String[] { "January", "February", "March", "April", "May",
            "June", "July", "August", "September","October", "November", "December" };

    public static final String MONTH_INDEX_EXTRA = "month_name_extra";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.month_picker, null);

        mCancel = (TextView) view.findViewById(R.id.cancel_month_picker);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        YearText = (TextView) view.findViewById(R.id.year_view);
        YearText.setText(year +"");

        PrevYear = (ImageView) view.findViewById(R.id.prev_year);
        NextYear = (ImageView) view.findViewById(R.id.next_year);

        PrevYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year -= 1;
                YearText.setText(year + "");
            }
        });

        NextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year += 1;
                YearText.setText(year + "");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        for (final String month: months) {
            TextView monthBtn = (TextView) view.findViewById(getResources().getIdentifier(month, "id", getContext().getPackageName()));
            monthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int monthIndex = getMonthIndex(month);
                    MonthExtra monthExtra = new MonthExtra(monthIndex + 1, year);
                    Intent intent = new Intent();
                    intent.putExtra(MONTH_INDEX_EXTRA, monthExtra);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                }
            });
        }

//        builder.setTitle("Choose month")
//                .setItems(months, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent();
//                        intent.putExtra(MONTH_INDEX_EXTRA, which + 1);
//                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
//                    }
//                }).setNegativeButton("Cancel", null);

        builder.setView(view);

        return builder.create();
    }

    private int getMonthIndex(final String month){
        for(int i = 0; i < 12; i++){
            if(months[i] == month){
                return i;
            }
        }

        return -1;
    }
}
