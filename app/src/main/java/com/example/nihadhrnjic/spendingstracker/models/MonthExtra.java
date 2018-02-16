package com.example.nihadhrnjic.spendingstracker.models;

import java.io.Serializable;

/**
 * Created by nihadhrnjic on 2/16/18.
 */

public class MonthExtra implements Serializable {
    private int month;
    private int year;

    public MonthExtra(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
