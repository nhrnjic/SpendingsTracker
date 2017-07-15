package com.example.nihadhrnjic.spendingstracker.models;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by nihadhrnjic on 6/26/17.
 */

public class SpendingsGoal extends RealmObject {

    public double Amount;
    public Date Date;

    public SpendingsGoal(){
        Amount = 0;
        Date = new Date();
    }

    public DateTime getDate(){
        return new DateTime(Date);
    }
}
