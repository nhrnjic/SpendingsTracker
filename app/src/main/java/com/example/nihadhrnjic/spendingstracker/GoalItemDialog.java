package com.example.nihadhrnjic.spendingstracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.SpendingsGoal;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 7/6/17.
 */

public class GoalItemDialog extends DialogFragment {

    private EditText mGoalAmount;
    private Button mSaveGoal;
    private TextView mExit;
    private TextInputLayout mGoalLabel;
    private SpendingsGoal mGoal;
    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoal = new SpendingsGoal();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_set_goal, null);
        mGoalAmount = (EditText) view.findViewById(R.id.goal_edit_text_id);
        mGoalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGoal.Amount = Double.parseDouble(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGoalLabel = (TextInputLayout) view.findViewById(R.id.goal_layout);

        mSaveGoal = (Button) view.findViewById(R.id.save_goal);
        mSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mGoal.Amount <= 0){
                    mGoalLabel.setError("This field is required.");
                    return;
                }

                addGoal();
                dismiss();
            }
        });

        mExit = (TextView) view.findViewById(R.id.cancel_goal);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    public void addGoal(){

        final Date monthBegin = new DateTime().withDayOfMonth(1).withTime(0,0,0,0).toDate();
        final Date monthEnd = new DateTime().plusMonths(1).minusDays(1).toDate();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                SpendingsGoal goal = realm.where(SpendingsGoal.class)
                        .between("Date", monthBegin, monthEnd)
                        .findFirst();

                if(goal == null){
                    realm.copyToRealm(mGoal);
                }else{
                    goal.Amount = mGoal.Amount;
                }

            }
        });
    }
}
