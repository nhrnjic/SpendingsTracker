package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by nihadhrnjic on 7/21/17.
 */

public class MoneyDialog extends android.support.v4.app.DialogFragment {

    public static final String MONTH_INDEX_EXTRA = "month_name_extra";
    private static final String ADD_MONEY_ARGS = "add_money_args";

    private EditText mMoneyInput;
    private TextView mMoneyTitle;
    private Button mSave;
    private TextView mCancel;
    private String mMoney = "";
    private boolean mAddMoney;
    private TextInputLayout mMoneyLabel;

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

        mMoneyTitle = (TextView) view.findViewById(R.id.money_title);
        mMoneyLabel = (TextInputLayout) view.findViewById(R.id.money_label);

        float moneyTotal = LocalPreferences.getFloat(getActivity(), getString(R.string.pref_money));
        String title = "Add to balance";

        if(!mAddMoney){
            title = "Subtract from balance";
        }

        mMoneyTitle.setText(title);

        mCancel = (TextView) view.findViewById(R.id.cancel_money);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSave = (Button) view.findViewById(R.id.save_balance);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mMoney.isEmpty()){
                    mMoneyLabel.setError("This field is required.");
                    return;
                }

                updateMoney();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    private void updateMoney(){
        float currentMoneyTotal = LocalPreferences.getFloat(getActivity(), getString(R.string.pref_money));
        if(mAddMoney){
            LocalPreferences.saveFloat(getActivity(), getString(R.string.pref_money), currentMoneyTotal + Float.valueOf(mMoney));
        }else{
            LocalPreferences.saveFloat(getActivity(), getString(R.string.pref_money), currentMoneyTotal - Float.valueOf(mMoney));
        }
    }
}
