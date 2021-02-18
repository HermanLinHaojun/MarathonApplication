package com.example.marathonapplication.utils;

import com.example.marathonapplication.model.Config;

import java.text.DecimalFormat;

public class DealUtils {

    public static String float2(float num){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        return decimalFormat.format(num);
    }
}
