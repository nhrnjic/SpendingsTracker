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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nihadhrnjic.spendingstracker.models.Overview;

public class ActivityMain extends AppCompatActivity{

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
                    fragment.setTitle();
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
        tabLayout.getTabAt(0).setIcon(R.drawable.overview_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.spendings_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.goals_icon);

        mAddButton = (FloatingActionButton) findViewById(R.id.fab);
        handleFabButton(mViewPager.getCurrentItem());

        String warning = warningText();

        if(warning != null){
            Toast.makeText(this, warning, Toast.LENGTH_LONG).show();
        }
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

    private String warningText(){
        Overview overview = new Overview();

        double percentage =  (overview.getTargetMonthTotal() / overview.getTargetMonthGoal()) * 100;

        if(overview.getTargetMonthTotal() > overview.getTargetMonthGoal()){
            return "Be careful! You've exceeded your monthly goal.";
        }else if(overview.getTargetMonthTotal() == overview.getTargetMonthGoal()){
            return "Be careful! You've reached your monthly goal.";
        }else if(percentage > 85 && percentage < 100){
            return "Be careful! You are getting closer to your monthly goal.";
        }else{
            return null;
        }
    }
}
