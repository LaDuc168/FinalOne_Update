package com.example.phamvolan.finalone.model;

import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_Custom {
    private String dateBegin;
    private String dateEnd;

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date_Custom(String dateBegin, String dateEnd) {

        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    public boolean ComparDate(String dateTemp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = null;
        Date end = null;
        Date temp = null;
        try {
            begin = simpleDateFormat.parse(dateBegin);
            end = simpleDateFormat.parse(dateEnd);
            temp = simpleDateFormat.parse(dateTemp);
        } catch (ParseException e) {
        }

        if ((begin.compareTo(temp) < 0 || begin.compareTo(temp) == 0) && (
                end.compareTo(temp) > 0 || end.compareTo(temp) == 0

        )) {
            return true;
        }
        return false;
    }
}
