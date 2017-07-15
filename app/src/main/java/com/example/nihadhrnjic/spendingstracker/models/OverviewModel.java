package com.example.nihadhrnjic.spendingstracker.models;

/**
 * Created by nihadhrnjic on 6/25/17.
 */

public class OverviewModel {

    private double mTotalSpendings;
    private Category mTopCategory;
    private String mMonthName;
    private double mSpendingThisDay;
    private double mSpendingGoal;

    public String getmMonthName() {
        return mMonthName;
    }

    public void setmMonthName(String mMonthName) {
        this.mMonthName = mMonthName;
    }

    public double getmTotalSpendings() {
        return mTotalSpendings;
    }

    public void setmTotalSpendings(double mTotalSpendings) {
        this.mTotalSpendings = mTotalSpendings;
    }

    public Category getmTopCategory() {
        return mTopCategory;
    }

    public void setmTopCategory(Category mTopCategory) {
        this.mTopCategory = mTopCategory;
    }

    public double getmSpendingThisDay() {
        return mSpendingThisDay;
    }

    public void setmSpendingThisDay(double mSpendingThisDay) {
        this.mSpendingThisDay = mSpendingThisDay;
    }

    public double getmSpendingGoal() {
        return mSpendingGoal;
    }

    public void setmSpendingGoal(double mSpendingGoal) {
        this.mSpendingGoal = mSpendingGoal;
    }
}
