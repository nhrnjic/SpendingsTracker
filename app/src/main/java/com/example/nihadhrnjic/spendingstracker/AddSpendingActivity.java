package com.example.nihadhrnjic.spendingstracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nihadhrnjic on 6/25/17.
 */

public class AddSpendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        FragmentManager manager = getSupportFragmentManager();
        Fragment spendingFragment = manager.findFragmentById(R.id.fragment_container);

        if(spendingFragment == null){
            spendingFragment = new AddSpendingFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, spendingFragment)
                    .commit();
        }
    }
}
