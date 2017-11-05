package com.example.nihadhrnjic.spendingstracker.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.nihadhrnjic.spendingstracker.LocalPreferences;
import com.example.nihadhrnjic.spendingstracker.R;

import org.joda.time.DateTime;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 6/25/17.
 */

public class Overview {

    private final Realm mRealm = Realm.getDefaultInstance();
    private int mTargetMonth;

    public Overview(){
        mTargetMonth = DateTime.now().getMonthOfYear();
    }

    public void setMonth(int month){
        mTargetMonth = month;
    }

    public String getCurrentMonthName(){
        return new DateTime(2017, mTargetMonth, 1, 0, 0).toString("MMMM");
    }

    public double getTargetMonthTotal(){
        return calculateMonthlyTotal();
    }

    public double getTargetMonthGoal(){
        return calculateMonthlyGoal();
    }

    public double getLeftToSpendThisMonth(){
        double goal = calculateMonthlyGoal();
        double total = calculateMonthlyTotal();
        return goal - total;
    }

    public double getDailyTotal(){
        return calculateDailyTotal();
    }

    public double getLeftToSpendToday(){
        return calculateLeftToSpendToday();
    }

    public float getTotalMoney(Context context){
        return LocalPreferences.getFloat(context, context.getString(R.string.pref_money));
    }

    public int daysLeftInMonth(){
        DateTime today = new DateTime();
        int daysLeftThisMonth = today.dayOfMonth().getMaximumValue() - today.dayOfMonth().get();
        return daysLeftThisMonth + 1;
    }

    private double calculateMonthlyTotal(){
        DateTime monthBegin = new DateTime().withMonthOfYear(mTargetMonth).withDayOfMonth(1)
                .withTime(0,0,0,0);

        DateTime monthEnd = monthBegin.plusMonths(1).minusDays(1);

        double amount = mRealm.where(SpendingsItem.class)
                .between("Date", monthBegin.toDate(), monthEnd.toDate())
                .sum("Amount").doubleValue();

        return amount;
    }

    private double calculateMonthlyGoal(){
        DateTime monthBegin = new DateTime().withMonthOfYear(mTargetMonth).withDayOfMonth(1)
                .withTime(0,0,0,0);

        DateTime monthEnd = monthBegin.plusMonths(1).minusDays(1);

        SpendingsGoal goal = mRealm.where(SpendingsGoal.class)
                .between("Date", monthBegin.toDate(), monthEnd.toDate()).findFirst();

        if(goal == null){
            return 0;
        }

        return goal.Amount;
    }

    private double calculateDailyTotal(){
        DateTime today = new DateTime().withTime(0,0,0,0);
        DateTime tomorow = new DateTime().withTime(23,59,59,59);

        return mRealm.where(SpendingsItem.class)
                .between("Date", today.toDate(), tomorow.toDate())
                .sum("Amount").doubleValue();
    }

    private double calculateLeftToSpendToday(){
        DateTime today = new DateTime();
        int daysLeftThisMonth = today.dayOfMonth().getMaximumValue() - today.dayOfMonth().get();

        double dailyAverage = getLeftToSpendThisMonth() / (daysLeftThisMonth + 1);
        double result = dailyAverage - getDailyTotal();

        if(result < 0){
            return dailyAverage;
        }else{
            return result;
        }
    }
}
