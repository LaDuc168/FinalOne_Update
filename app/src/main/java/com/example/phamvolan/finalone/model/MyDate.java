package com.example.phamvolan.finalone.model;

/**
 * Created by LaVanDuc on 6/14/2018.
 */

public class MyDate {
    private int date;
    private int month;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public MyDate(int date, int month) {

        this.date = date;
        this.month = month;
    }
}
