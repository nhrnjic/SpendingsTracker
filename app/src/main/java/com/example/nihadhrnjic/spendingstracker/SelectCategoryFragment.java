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

import com.example.nihadhrnjic.spendingstracker.models.Category;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nihadhrnjic on 6/23/17.
 */

public class SelectCategoryFragment extends DialogFragment {

    public static final String CHOOSE_CATEGORY_EXTRA = "choose_category_extra";

    private Realm mRealmInstance;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mRealmInstance = Realm.getDefaultInstance();
        final String[] items = getAllCategories();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose category")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(CHOOSE_CATEGORY_EXTRA, items[which]);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                });

        return builder.create();

    }

    private String[] getAllCategories(){
        RealmResults<Category> categories = mRealmInstance.where(Category.class).findAll();
        String[] categoriesList = new String[categories.size()];

        ArrayList<String> categoriesArrayList = new ArrayList<>();
        for (Category item: categories){
            categoriesArrayList.add(item.Name);
        }

        return categoriesArrayList.toArray(categoriesList);
    }
}
