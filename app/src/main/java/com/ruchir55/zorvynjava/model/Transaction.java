package com.ruchir55.zorvynjava.model;

import android.view.SurfaceControl;

//import androidx.annotation.RequiresApi;

//@RequiresApi(api = Build.VERSION_CODES.Q)
public class Transaction extends SurfaceControl.Transaction {
    int id;
    String title;
    double amount;
    String type;
    long timestamp;

    public Transaction(int id, String title, double amount, String type, long timestamp) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public long getTimestamp() { return timestamp; }
}
