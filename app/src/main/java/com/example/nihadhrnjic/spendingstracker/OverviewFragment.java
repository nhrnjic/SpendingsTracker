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

import com.example.nihadhrnjic.spendingstracker.models.OverviewModel;
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

    private OverviewModel mOverview;
    private Realm mRealmInstance;
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

        mOverview = new OverviewModel();
        mRealmInstance = Realm.getDefaultInstance();
        mTargetMonth = DateTime.now().getMonthOfYear();

        setTitle();
        mOverview.setmSpendingThisDay(getTodayTotal());
        mOverview.setmSpendingGoal(getSpendingGoal());
        mOverview.setmTotalSpendings(getTotalForMonth());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        mTotal = (TextView) view.findViewById(R.id.total_amount_id);
        mTotalToday = (TextView) view.findViewById(R.id.total_today_id);
        mSpendingGoal = (TextView) view.findViewById(R.id.spending_goal_id);
        mCanSpendMonth = (TextView) view.findViewById(R.id.can_spend_month_id);
        mCanSpendToday = (TextView) view.findViewById(R.id.can_spend_today_id);

        mChangeMonthButton = (Button) view.findViewById(R.id.change_month_id);

        mChangeMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker monthPicker = new MonthPicker();
                monthPicker.setTargetFragment(OverviewFragment.this, CHOOSE_MONTH_CODE);
                monthPicker.show(getFragmentManager(), CHOOSE_MONTH_FRAGMENT);
            }
        });

        setTotalToday();
        setSpendingGoal();
        setTotalForMonth();
        setCanSpend();

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
            mOverview.setmMonthName(getMonthName(mTargetMonth));


            setTitle();
        }
    }


    private double getTotalForMonth(){

        DateTime monthBegin = new DateTime().withMonthOfYear(mTargetMonth).withDayOfMonth(1);
        DateTime monthEnd = monthBegin.plusMonths(1).minusDays(1);

        double amount = mRealmInstance.where(SpendingsItem.class)
                .between("Date", monthBegin.toDate(), monthEnd.toDate())
                .sum("Amount").doubleValue();

        return amount;
    }

    private void setTotalForMonth(){
        mTotal.setText("Total this month: "+ mOverview.getmTotalSpendings() + " KM");
    }

    private void setTotalToday(){
        mTotalToday.setText("Total for "+ new DateTime().toString("E, dd")+ ": "+ mOverview.getmSpendingThisDay()+ " KM");
    }

    private String getMonthName(int index){
        final String[] months = new String[] { "","January", "February", "March", "April", "May",
                "June", "July", "August", "September","October", "November", "December" };

        return months[index];
    }

    private void setTitle(){
        getActivity().setTitle("Overview for : "+getMonthName(mTargetMonth));
    }

    private double getTodayTotal(){

        DateTime today = new DateTime().withTime(0,0,0,0);
        DateTime tomorow = new DateTime().withTime(23,59,59,59);

        double totalToday = mRealmInstance.where(SpendingsItem.class)
                .between("Date", today.toDate(), tomorow.toDate())
                .sum("Amount").doubleValue();

        return totalToday;
    }

    private double getSpendingGoal(){
        DateTime monthBegin = new DateTime().withMonthOfYear(mTargetMonth).withDayOfMonth(1);
        DateTime monthEnd = monthBegin.plusMonths(1).minusDays(1);

        SpendingsGoal goal = mRealmInstance.where(SpendingsGoal.class)
                .between("Date", monthBegin.toDate(), monthEnd.toDate()).findFirst();

        return goal.Amount;
    }

    private void setSpendingGoal(){
        mSpendingGoal.setText("Goal for "+new DateTime().toString("MMMM")+": "+mOverview.getmSpendingGoal() + " KM");
    }

    private double getCanSpendMonth(){
        return mOverview.getmSpendingGoal() - mOverview.getmTotalSpendings();
    }

    private double getCanSpendToday(){

        DateTime today = new DateTime();
        int daysLeftThisMonth = today.dayOfMonth().getMaximumValue() - today.dayOfMonth().get();

        Log.d("MOnth", daysLeftThisMonth  + "");

        double dailyAverage = getCanSpendMonth() / (daysLeftThisMonth + 1);
        return dailyAverage - mOverview.getmSpendingThisDay();

    }

    private void setCanSpend(){
        mCanSpendMonth.setText("Left to spend this month: "+ getCanSpendMonth() + " KM");
        String canSpendTodayRounded = String.format("Left to spend this day: %.2f KM", getCanSpendToday());
        mCanSpendToday.setText(canSpendTodayRounded);
    }

    public void updateUI(){
        setTitle();
        mOverview.setmSpendingThisDay(getTodayTotal());
        mOverview.setmSpendingGoal(getSpendingGoal());
        mOverview.setmTotalSpendings(getTotalForMonth());

        setTotalToday();
        setSpendingGoal();
        setTotalForMonth();
        setCanSpend();
    }
}
