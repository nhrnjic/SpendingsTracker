package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.Category;
import com.example.nihadhrnjic.spendingstracker.models.SpendingsItem;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 6/21/17.
 */

public class AddSpendingFragment extends Fragment {

    public static final String DIALOG_NEW_CATEGORY = "dialog_new_category";
    public static final String DIALOG_CHOOSE_CATEGORY = "dialog_choose_category";
    public static final String DIALOG_CHOOSE_DATE = "dialog_choose_date";

    public static final int CHOOSE_CATEGORY_CODE = 0;
    public static final int CHOOSE_DATE_CODE = 1;

    private Realm mRealmInstance;

    private EditText mItemName;
    private EditText mItemDescription;
    private EditText mItemPrice;
    private Button mItemDate;
    private Button mAddSpendings;
    private TextView mCancel;
    private TextInputLayout mSpendingsNameLabel;
    private TextInputLayout mSpendingsDescLabel;
    private TextInputLayout mSpendingsAmountLabel;

    private SpendingsItem mSpendingsItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpendingsItem = new SpendingsItem();
        mRealmInstance = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_spending, container, false);

        mItemName = (EditText) view.findViewById(R.id.item_name_id);
        mItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSpendingsItem.Name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mItemDescription = (EditText) view.findViewById(R.id.item_desc_id);
        mItemDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSpendingsItem.Description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mItemDate = (Button) view.findViewById(R.id.item_date_id);
        mItemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(AddSpendingFragment.this, CHOOSE_DATE_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_CHOOSE_DATE);
            }
        });

        mItemPrice = (EditText) view.findViewById(R.id.item_price_id);
        mItemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    mSpendingsItem.Amount = Double.parseDouble(s.toString());
                }catch (NumberFormatException e){
                    mSpendingsItem.Amount = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAddSpendings = (Button) view.findViewById(R.id.add_spendings_id);
        mAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean canAdd = true;

                if(mSpendingsItem.Name.isEmpty()){
                    mSpendingsNameLabel.setError("This field is required.");
                    canAdd = false;
                }

                if(mSpendingsItem.Description.isEmpty()){
                    mSpendingsDescLabel.setError("This field is required.");
                    canAdd = false;
                }

                if(mSpendingsItem.Amount <= 0){
                    mSpendingsAmountLabel.setError("This field is required.");
                }

                if(canAdd){
                    mRealmInstance.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            SpendingsItem item = realm.copyToRealm(mSpendingsItem);
                            float currentAmount = LocalPreferences.getFloat(getActivity(), "money_amount_key");
                            LocalPreferences.saveFloat(getActivity(), "money_amount_key", currentAmount - (float) mSpendingsItem.Amount);
                        }
                    });

                    getActivity().finish();
                }
            }
        });

        mCancel = (TextView) view.findViewById(R.id.add_spending_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mSpendingsNameLabel = (TextInputLayout) view.findViewById(R.id.item_name_layout);
        mSpendingsDescLabel = (TextInputLayout) view.findViewById(R.id.item_desc_layout);
        mSpendingsAmountLabel = (TextInputLayout) view.findViewById(R.id.item_price_layout);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == CHOOSE_DATE_CODE){
            DateTime date = (DateTime) data.getSerializableExtra(DatePickerFragment.NEW_DATE_EXTRA);
            mItemDate.setText(date.toString("dd/MM/yyyy"));
            mSpendingsItem.Date = date.toDate();
        }
    }

    private Category getCategoryByName(String name){
        Category category = mRealmInstance.where(Category.class).equalTo("Name", name).findFirst();
        return category;
    }
}
