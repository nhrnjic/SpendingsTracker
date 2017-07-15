package com.example.nihadhrnjic.spendingstracker.models;

import org.joda.time.DateTime;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 6/25/17.
 */

public class Overview {

    private Realm mRealm = Realm.getDefaultInstance();
    private int mTargetMonth;

    public Overview(int month){
        mTargetMonth = month;
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

    private double calculateMonthlyTotal(){
        DateTime monthBegin = new DateTime().withMonthOfYear(mTargetMonth).withDayOfMonth(1);
        DateTime monthEnd = monthBegin.plusMonths(1).minusDays(1);

        double amount = mRealm.where(SpendingsItem.class)
                .between("Date", monthBegin.toDate(), monthEnd.toDate())
                .sum("Amount").doubleValue();

        return amount;
    }

    private double calculateMonthlyGoal(){
        DateTime monthBegin = new DateTime().withMonthOfYear(mTargetMonth).withDayOfMonth(1);
        DateTime monthEnd = monthBegin.plusMonths(1).minusDays(1);

        SpendingsGoal goal = mRealm.where(SpendingsGoal.class)
                .between("Date", monthBegin.toDate(), monthEnd.toDate()).findFirst();

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
        return dailyAverage - getDailyTotal();
    }

}
