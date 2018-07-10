package com.example.phamvolan.finalone.model;

public class DateGraph {
    private String dateBD;
    private String dateKT;

    public String getDateBD() {
        return dateBD;
    }

    public void setDateBD(String dateBD) {
        this.dateBD = dateBD;
    }

    public String getDateKT() {
        return dateKT;
    }

    public void setDateKT(String dateKT) {
        this.dateKT = dateKT;
    }

    public DateGraph(String dateBD, String dateKT) {

        this.dateBD = dateBD;
        this.dateKT = dateKT;
    }
}
