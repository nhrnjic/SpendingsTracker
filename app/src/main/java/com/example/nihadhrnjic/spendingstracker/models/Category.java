package com.example.nihadhrnjic.spendingstracker.models;

import io.realm.RealmObject;

/**
 * Created by nihadhrnjic on 6/22/17.
 */

public class Category extends RealmObject {

    public String Name;

    public Category() {
        Name = "";
    }
}
