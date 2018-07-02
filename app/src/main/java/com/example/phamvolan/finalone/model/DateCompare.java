package com.example.phamvolan.finalone.model;

public class DateCompare {

    private String date;
    private int gio;

    public DateCompare(String date, int gio) {
        this.date = date;
        this.gio = gio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGio() {
        return gio;
    }

    public void setGio(int gio) {
        this.gio = gio;
    }

}
