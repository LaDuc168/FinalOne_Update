package com.example.phamvolan.finalone.model;

/**
 * Created by LaVanDuc on 6/13/2018.
 */

public class DangKiNguoiDung {
    private String ten;
    private String email;
    private String pass;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public DangKiNguoiDung(String ten, String email, String pass) {

        this.ten = ten;
        this.email = email;
        this.pass = pass;
    }
}
