package com.example.marathonapplication.square.utils;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class DateUtils {
    public static void showDatePickDialog(Context context, DatePickerDialog.OnDateSetListener listener, String curDate) {
        Calendar calendar = Calendar.getInstance();
        int year = 0,month = 0,day = 0;
        if(curDate == null) {
            try {
                int[] result = dealTheDate(curDate);
                year = result[0];
                month = result[1];
                day = result[2];
            } catch (Exception e) {
                e.printStackTrace();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,DatePickerDialog.THEME_HOLO_LIGHT,listener, year,month , day);
        datePickerDialog.show();
    }

    public static int[] dealTheDate(String curDate){
        int[] dateInt = new int[3];
        String[] date = curDate.split("-");
        dateInt[0] = Integer.parseInt(date[0]);
        dateInt[1] = Integer.parseInt(date[1]);
        dateInt[2] = Integer.parseInt(date[2]);
        return dateInt;
    }
}
