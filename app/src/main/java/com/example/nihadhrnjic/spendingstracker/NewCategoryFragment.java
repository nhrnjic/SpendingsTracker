package com.example.nihadhrnjic.spendingstracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nihadhrnjic.spendingstracker.models.Category;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 6/22/17.
 */

public class NewCategoryFragment extends DialogFragment {

    private Category mCategory;
    private EditText mCategoryName;
    private Realm mRealmInstance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategory = new Category();
        mRealmInstance = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_new_category, null);

        mCategoryName = (EditText) view.findViewById(R.id.dialog_new_category_id);
        mCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCategory.Name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setMessage("Create a new Category")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!mCategory.Name.isEmpty()){

                            if(!doesCategoryExists(mCategory.Name)){
                                addCategory();
                                Log.d("INSERT", "Added new category");
                            }else{
                                Log.d("INSERT", "Didnt insert new category, category exists!");
                            }
                        }else{
                            Log.d("INSERT", "DIDnt insert new category, name empty!");
                        }
                    }
                });


        return builder.create();
    }

    private void addCategory(){
        mRealmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = realm.createObject(Category.class);
                category.Name = mCategory.Name;
            }
        });
    }

    private boolean doesCategoryExists(final String name){

        Category category = mRealmInstance.where(Category.class)
                .equalTo("Name", name)
                .findFirst();

        return category != null;
    }
}
