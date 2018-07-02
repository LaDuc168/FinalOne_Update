package com.example.phamvolan.finalone.model;

/**
 * Created by LaVanDuc on 5/17/2018.
 */

public class NguoiDung {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NguoiDung(String username, String password) {

        this.username = username;
        this.password = password;
    }
}
