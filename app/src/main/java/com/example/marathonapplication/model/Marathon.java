package com.example.marathonapplication.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;
import java.util.Date;

public class Marathon {
    LatLng startLatLng;
    LatLng endLatLng;
    Date startDate;
    Date endDate;
    Timestamp totalTimeStamp;

    public LatLng getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(LatLng startLatLng) {
        this.startLatLng = startLatLng;
    }

    public LatLng getEndLatLng() {
        return endLatLng;
    }

    public void setEndLatLng(LatLng endLatLng) {
        this.endLatLng = endLatLng;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Timestamp getTotalTimeStamp() {
        return totalTimeStamp;
    }

    public void setTotalTimeStamp(Timestamp totalTimeStamp) {
        this.totalTimeStamp = totalTimeStamp;
    }
}
