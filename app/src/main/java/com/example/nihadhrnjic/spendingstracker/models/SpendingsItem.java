package com.example.nihadhrnjic.spendingstracker.models;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by nihadhrnjic on 6/22/17.
 */

public class SpendingsItem extends RealmObject {

    @PrimaryKey
    @Required
    public String Id;

    public String Name;

    public String Description;

    public java.util.Date Date;

    public double Amount;

    public Category Category;

    public SpendingsItem() {
        Id = UUID.randomUUID().toString();
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
