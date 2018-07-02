package com.example.phamvolan.finalone.model;

public class TramSelect {
    private String maTS;
    private String matram;
    private String tentram;

    public String getMaTS() {
        return maTS;
    }

    public void setMaTS(String maTS) {
        this.maTS = maTS;
    }

    public String getMatram() {
        return matram;
    }

    public void setMatram(String matram) {
        this.matram = matram;
    }

    public String getTentram() {
        return tentram;
    }

    public void setTentram(String tentram) {
        this.tentram = tentram;
    }

    public TramSelect(String maTS, String matram, String tentram) {

        this.maTS = maTS;
        this.matram = matram;
        this.tentram = tentram;
    }
}
