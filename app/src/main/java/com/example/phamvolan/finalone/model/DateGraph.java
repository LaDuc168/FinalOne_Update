package com.example.phamvolan.finalone.model;

public class DateGraph {
    private String date;
    private int gioBD;
    private int gioKT;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGioBD() {
        return gioBD;
    }

    public void setGioBD(int gioBD) {
        this.gioBD = gioBD;
    }

    public int getGioKT() {
        return gioKT;
    }

    public void setGioKT(int gioKT) {
        this.gioKT = gioKT;
    }

    public DateGraph(String date, int gioBD, int gioKT) {

        this.date = date;
        this.gioBD = gioBD;
        this.gioKT = gioKT;
    }
}
