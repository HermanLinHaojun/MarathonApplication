package com.example.marathonapplication.db.race;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RaceDetail {

    private String mId;
    private String mRaceId;
    private String mCreatorId;
    private String mParticipantId;
    private int mStepNum;
    private int mRaceStatus;
    private long mRaceCostTime;
    private RaceInfo mRaceInfo;


    public RaceDetail(String id, String raceId, String creatorId, String participantId, int stepNum, int raceStatus) {
        mId = id;
        mRaceId = raceId;
        mCreatorId = creatorId;
        mParticipantId = participantId;
        mStepNum = stepNum;
        mRaceStatus = raceStatus;
    }

    public RaceDetail() {

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getRaceId() {
        return mRaceId;
    }

    public void setRaceId(String raceId) {
        mRaceId = raceId;
    }

    public String getCreatorId() {
        return mCreatorId;
    }

    public void setCreatorId(String creatorId) {
        mCreatorId = creatorId;
    }

    public String getParticipantId() {
        return mParticipantId;
    }

    public void setParticipantId(String participantId) {
        mParticipantId = participantId;
    }

    public int getStepNum() {
        return mStepNum;
    }

    public void setStepNum(int stepNum) {
        mStepNum = stepNum;
    }

    public int getRaceStatus() {
        return mRaceStatus;
    }

    public void setRaceStatus(int raceStatus) {
        mRaceStatus = raceStatus;
    }

    public long getRaceCostTime() {
        return mRaceCostTime;
    }

    public void setRaceCostTime(long raceCostTime) {
        mRaceCostTime = raceCostTime;
    }


    public RaceInfo getRaceInfo() {
        return mRaceInfo;
    }

    public void setRaceInfo(RaceInfo raceInfo) {
        mRaceInfo = raceInfo;
    }
}
