package com.example.nihadhrnjic.spendingstracker;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ActivityMain extends AppCompatActivity implements GoalItemDialog.OnGoalDialogFinishListener {

    public interface OnMonthGoalUpdated{
        public void onUpdate();
    }

    public static final String GOAL_DIALOG = "goal_dialog";

   // private OnMonthGoalUpdated mGoalUpdatedListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                handleFabButton(position);

                if(position == 0){
                    OverviewFragment fragment = (OverviewFragment) mSectionsPagerAdapter
                            .instantiateItem(mViewPager, position);
                    fragment.updateUI();
                }

                if(position == 1){
                    SpendingsListFragment fragment = (SpendingsListFragment) mSectionsPagerAdapter
                            .instantiateItem(mViewPager, position);
                    fragment.setTitle();
                }

                if(position == 2){
                    GoalsFragment fragment = (GoalsFragment) mSectionsPagerAdapter
                            .instantiateItem(mViewPager, position);
                    fragment.setTitle();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mAddButton = (FloatingActionButton) findViewById(R.id.fab);
        handleFabButton(mViewPager.getCurrentItem());
    }

    @Override
    public void onComplete() {
       // mGoalUpdatedListener.onUpdate();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new OverviewFragment();
                case 1:
                    return new SpendingsListFragment();
                case 2:
                    return new GoalsFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Spendings";
                case 2:
                    return "Goals";
            }
            return null;
        }
    }

    private void handleFabButton(int position){
        switch (position) {
            case 0:
                mAddButton.setVisibility(View.GONE);
                break;
            case 1:
                mAddButton.setVisibility(View.VISIBLE);
                mAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityMain.this, AddSpendingActivity.class));
                    }
                });
                break;
            case 2:
                mAddButton.setVisibility(View.VISIBLE);
                mAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoalItemDialog goalItemDialog = new GoalItemDialog();
                        goalItemDialog.show(getFragmentManager(), GOAL_DIALOG);
                    }
                });
        }
    }
}
