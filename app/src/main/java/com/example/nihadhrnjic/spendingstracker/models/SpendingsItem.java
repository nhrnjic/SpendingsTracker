package com.example.nihadhrnjic.spendingstracker.models;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by nihadhrnjic on 6/22/17.
 */

public class SpendingsItem extends RealmObject {

    public String Name;

    public String Description;

    public java.util.Date Date;

    public double Amount;

    public Category Category;

    public SpendingsItem() {
        Name = "";
        Description = "";
        Date = new Date();
        Amount = 0;
        Category = new Category();
    }

    public DateTime getDate(){
        return new DateTime(Date);
    }
}
