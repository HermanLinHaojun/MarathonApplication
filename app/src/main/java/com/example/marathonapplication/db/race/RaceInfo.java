package com.example.marathonapplication.db.race;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RaceInfo {
    private String mId;
    private String mRaceName;
    private String mCreatorId;
    List<LatLng> mLinePoints;
    private String mStartDate;
    private String mEndDate;

    public RaceInfo() {
    }

    public RaceInfo(String id, String raceName, String creatorId, List<LatLng> linePoints, String startDate, String endDate) {
        mId = id;
        mRaceName = raceName;
        mCreatorId = creatorId;
        mLinePoints = linePoints;
        mStartDate = startDate;
        mEndDate = endDate;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getRaceName() {
        return mRaceName;
    }

    public void setRaceName(String raceName) {
        mRaceName = raceName;
    }

    public String getCreatorId() {
        return mCreatorId;
    }

    public void setCreatorId(String creatorId) {
        mCreatorId = creatorId;
    }

    public List<LatLng> getLinePoints() {
        return mLinePoints;
    }

    public void setLinePoints(List<LatLng> linePoints) {
        mLinePoints = linePoints;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }
}
