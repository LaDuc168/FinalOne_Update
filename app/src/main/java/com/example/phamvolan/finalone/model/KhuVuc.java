package com.example.phamvolan.finalone.model;

public class KhuVuc {
    private String ma;
    private String ten;

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public KhuVuc(String ma, String ten) {

        this.ma = ma;
        this.ten = ten;
    }
}
