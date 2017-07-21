package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nihadhrnjic on 7/21/17.
 */

public class MoneyDialog extends android.support.v4.app.DialogFragment {

    public static final String MONTH_INDEX_EXTRA = "month_name_extra";
    private static final String ADD_MONEY_ARGS = "add_money_args";

    private EditText mMoneyInput;
    private String mMoney = "";
    private boolean mAddMoney;

    public static MoneyDialog newInstance(boolean addMoney){
        MoneyDialog dialog = new MoneyDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ADD_MONEY_ARGS, addMoney);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mAddMoney = getArguments().getBoolean(ADD_MONEY_ARGS, true);


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_money, null);
        mMoneyInput = (EditText) view.findViewById(R.id.add_money_id);
        mMoneyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMoney = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        float moneyTotal = LocalPreferences.getFloat(getActivity(), "money_amount_key");
        String formatedMoneyTotal = String.format("%.2f", moneyTotal);
        String title = "Add Money ("+formatedMoneyTotal+" KM)";

        if(!mAddMoney){
            title = "Remove Money ("+formatedMoneyTotal+" KM)";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setView(view)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateMoney();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }

    private void updateMoney(){
        float currentMoneyTotal = LocalPreferences.getFloat(getActivity(), "money_amount_key");
        if(mAddMoney){
            LocalPreferences.saveFloat(getActivity(), "money_amount_key", currentMoneyTotal + Float.valueOf(mMoney));
        }else{
            LocalPreferences.saveFloat(getActivity(), "money_amount_key", currentMoneyTotal - Float.valueOf(mMoney));
        }
    }
}
