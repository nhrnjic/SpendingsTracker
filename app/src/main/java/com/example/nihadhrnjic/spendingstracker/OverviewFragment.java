package com.example.nihadhrnjic.spendingstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.Overview;
import com.example.nihadhrnjic.spendingstracker.models.SpendingsGoal;
import com.example.nihadhrnjic.spendingstracker.models.SpendingsItem;

import org.joda.time.DateTime;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 6/21/17.
 */

public class OverviewFragment extends Fragment {

    private static final String CHOOSE_MONTH_FRAGMENT = "choose_month_fragment";
    private static final int CHOOSE_MONTH_CODE = 0;

    private Overview mOverview;
    private final Realm mRealmInstance = Realm.getDefaultInstance();
    private int mTargetMonth;

    private TextView mTotal;
    private TextView mTotalToday;
    private TextView mSpendingGoal;
    private TextView mCanSpendMonth;
    private TextView mCanSpendToday;
    private Button mChangeMonthButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTargetMonth = DateTime.now().getMonthOfYear();
        mOverview = new Overview(mTargetMonth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        bindUIWidgets(view);

        mChangeMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker monthPicker = new MonthPicker();
                monthPicker.setTargetFragment(OverviewFragment.this, CHOOSE_MONTH_CODE);
                monthPicker.show(getFragmentManager(), CHOOSE_MONTH_FRAGMENT);
            }
        });

        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_overview, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == CHOOSE_MONTH_CODE){
            mTargetMonth = data.getIntExtra(MonthPicker.MONTH_INDEX_EXTRA, mTargetMonth);
            setTitle();
        }
    }

    private void setTotalForMonth(){
        mTotal.setText(getString(R.string.month_total, mOverview.getTargetMonthTotal()+""));
    }

    private void setTotalToday(){
        mTotalToday.setText(getString(R.string.today_total, mOverview.getDailyTotal()+""));
    }

    private void setTitle(){
        getActivity().setTitle(getString(R.string.overview_title, new DateTime(2017, mTargetMonth, 1, 0, 0).toString("MMMM")));
    }

    private void setSpendingGoal(){
        double goal = mOverview.getTargetMonthGoal();
        if(goal <= 0){
            mSpendingGoal.setText(getString(R.string.goal_not_set_general, new DateTime().toString("MMMM")));
        }else{
            mSpendingGoal.setText(getString(R.string.current_month_goal, new DateTime().toString("MMMM"), +mOverview.getTargetMonthGoal() + ""));
        }
    }

    private void setCanSpend(){

        if(mOverview.getTargetMonthGoal() <= 0){
            mCanSpendMonth.setText(getString(R.string.set_goal_warning));
            mCanSpendToday.setText(getString(R.string.set_goal_warning));
        }else {
            String canSpendTodayRounded = String.format("%.2f", mOverview.getLeftToSpendToday());
            mCanSpendToday.setText(getString(R.string.left_to_spend_today, canSpendTodayRounded));
            mCanSpendMonth.setText(getString(R.string.left_to_spend_month, mOverview.getLeftToSpendThisMonth() + ""));
        }
    }

    public void updateUI(){
        setTitle();
        setTotalToday();
        setSpendingGoal();
        setTotalForMonth();
        setCanSpend();
    }

    private void bindUIWidgets(View view){
        mTotal = (TextView) view.findViewById(R.id.total_amount_id);
        mTotalToday = (TextView) view.findViewById(R.id.total_today_id);
        mSpendingGoal = (TextView) view.findViewById(R.id.spending_goal_id);
        mCanSpendMonth = (TextView) view.findViewById(R.id.can_spend_month_id);
        mCanSpendToday = (TextView) view.findViewById(R.id.can_spend_today_id);
        mChangeMonthButton = (Button) view.findViewById(R.id.change_month_id);
        mChangeMonthButton = (Button) view.findViewById(R.id.change_month_id);
    }
}
