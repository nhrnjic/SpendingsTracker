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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.MonthExtra;
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
    private static final String ADD_MONEY_DIALOG = "add_money_dialog";

    private static final int CHOOSE_MONTH_CODE = 0;
    private static final int UPDATE_MONEY_CODE = 1;

    private Overview mOverview;
    private int mTargetMonth;

    private TextView mTotal;
    private TextView mTotalToday;
    private TextView mSpendingGoal;
    private TextView mCanSpendMonth;
    private TextView mCanSpendToday;
    private TextView mTotalMoney;
    private TextView mDaysLeftInMonth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverview = new Overview();
        setHasOptionsMenu(true);
        setTitle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        bindUIWidgets(view);
        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_money_id:
                openMoneyDialog(true);
                return true;
            case R.id.remove_money_id:
                openMoneyDialog(false);
                return true;
            case R.id.change_month_id:
                changeOverviewMonth();
                return true;
            default: return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == CHOOSE_MONTH_CODE){
            MonthExtra monthExtra = (MonthExtra) data.getSerializableExtra(MonthPicker.MONTH_INDEX_EXTRA);
            mOverview.setMonth(monthExtra.getMonth());
            mOverview.setYear(monthExtra.getYear());
            updateUI();
            setTitle();
        }

        if(requestCode == UPDATE_MONEY_CODE){
            updateUI();
        }
    }

    private void setTotalForMonth(){
        String targetMonthTotalRounded = String.format("%.2f", mOverview.getTargetMonthTotal());
        mTotal.setText(targetMonthTotalRounded);
    }

    private void setTotalToday(){
        String totalTodayRounded = String.format("%.2f", mOverview.getDailyTotal());
        mTotalToday.setText(totalTodayRounded);
    }

    public void setTitle(){
        getActivity().setTitle(getString(R.string.overview_title, mOverview.getLabel()));
    }

    private void setSpendingGoal(){
        double goal = mOverview.getTargetMonthGoal();
        if(goal <= 0){
            mSpendingGoal.setText(getString(R.string.goal_not_set_general, mOverview.getCurrentMonthName()));
        }else{
            mSpendingGoal.setText(getString(R.string.current_month_goal, mOverview.getCurrentMonthName(), +mOverview.getTargetMonthGoal() + ""));
        }
    }

    private void setTotalMoney(){
        String totalMoneyRounded = String.format("%.2f", mOverview.getTotalMoney(getActivity()));
        mTotalMoney.setText(totalMoneyRounded + " KM");
    }

    private void setCanSpend(){

        if(mOverview.getTargetMonthGoal() <= 0){
            mCanSpendMonth.setText(getString(R.string.set_goal_warning));
            mCanSpendToday.setText(getString(R.string.set_goal_warning));
        }else {
            String canSpendTodayRounded = String.format("%.2f", mOverview.getLeftToSpendToday());
            String canSpendThisMonthRounded = String.format("%.2f", mOverview.getLeftToSpendThisMonth());

            mCanSpendToday.setText(canSpendTodayRounded);
            mCanSpendMonth.setText(canSpendThisMonthRounded);
        }
    }

    public void updateUI(){
        setTotalToday();
        //setSpendingGoal();
        setTotalForMonth();
        setCanSpend();
        setTotalMoney();
        //mDaysLeftInMonth.setText(getString(R.string.days_left, mOverview.daysLeftInMonth() + ""));
    }

    private void bindUIWidgets(View view){
        mTotal = (TextView) view.findViewById(R.id.total_amount_id);
        mTotalToday = (TextView) view.findViewById(R.id.total_today_id);
        //mSpendingGoal = (TextView) view.findViewById(R.id.spending_goal_id);
        mCanSpendMonth = (TextView) view.findViewById(R.id.can_spend_month_id);
        mCanSpendToday = (TextView) view.findViewById(R.id.can_spend_today_id);
        mTotalMoney = (TextView) view.findViewById(R.id.total_money_id);
        //mDaysLeftInMonth = (TextView) view.findViewById(R.id.days_left_in_month_id);
    }

    private void changeOverviewMonth(){
        MonthPicker monthPicker = new MonthPicker();
        monthPicker.setTargetFragment(this, CHOOSE_MONTH_CODE);
        monthPicker.show(getFragmentManager(), CHOOSE_MONTH_FRAGMENT);
    }

    private void openMoneyDialog(boolean addMoney){
        MoneyDialog moneyDialog = MoneyDialog.newInstance(addMoney);
        moneyDialog.setTargetFragment(this, UPDATE_MONEY_CODE);
        moneyDialog.show(getFragmentManager(), ADD_MONEY_DIALOG);
    }
}
