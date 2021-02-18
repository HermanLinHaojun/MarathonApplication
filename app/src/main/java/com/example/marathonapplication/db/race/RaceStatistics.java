package com.example.marathonapplication.db.race;

import java.util.List;

public class RaceStatistics {
    private RaceInfo mRaceInfo;
    private List<RaceDetail> mRaceDetailList;

    public RaceInfo getRaceInfo() {
        return mRaceInfo;
    }

    public void setRaceInfo(RaceInfo raceInfo) {
        mRaceInfo = raceInfo;
    }

    public List<RaceDetail> getRaceDetailList() {
        return mRaceDetailList;
    }

    public void setRaceDetailList(List<RaceDetail> raceDetailList) {
        mRaceDetailList = raceDetailList;
    }
}
