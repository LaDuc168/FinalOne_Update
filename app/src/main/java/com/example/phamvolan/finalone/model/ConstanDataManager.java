package com.example.phamvolan.finalone.model;

import java.util.ArrayList;

public class ConstanDataManager {
    public static  int SELECT_ITEM_DIALOG=0;
    public static final int SELECT_THEM_KHU_VUC=0;
    public static final int SELECT_THEM_TRAM=1;
    public static final String VALUE_GRAPH="GRAPH";
    public static final String VALUE_TABLE="TABLE";
    public static final String VARIABLE_TRANSLATE="VALUE_ACTIVITY";
    public static final String CONSTAN_TRAM="Đại Học Quốc Tế Miền Đông";

    public static boolean CheckTram(ArrayList<String> arr){
        for(String item:arr){
            if(item.equals(ConstanDataManager.CONSTAN_TRAM)){
                return true;
            }
        }
        return false;
    }

}
