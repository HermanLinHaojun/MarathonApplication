package com.example.marathonapplication.db.race;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.marathonapplication.MyApplication;
import com.example.marathonapplication.R;
import com.example.marathonapplication.db.DbHelper;
import com.example.marathonapplication.db.TableContract;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.COLUMN_CREATOR;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.COLUMN_PARTICIPANT_ID;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.COLUMN_RACE_COST_TIME;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.COLUMN_RACE_ID;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.COLUMN_RACE_STATUS;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.COLUMN_STEP_NUM;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry.TABLE_NAME;
import static com.example.marathonapplication.db.TableContract.RaceDetailEntry._ID;


public class RaceDetailDao {
    private DbHelper mDbHelper;
    private RaceDetailDao(Context ctx){
        mDbHelper = new DbHelper(ctx);
    }

    private static RaceDetailDao instance;
    public static synchronized RaceDetailDao getInstance(Context ctx){
        if(instance == null){
            instance = new RaceDetailDao(ctx);
        }
        return  instance;
    }

    public RaceDetail addRaceDetail(RaceDetail raceDetail){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values= new ContentValues();
        values.put(COLUMN_RACE_ID, raceDetail.getRaceId());
        values.put(COLUMN_CREATOR, raceDetail.getCreatorId());
        values.put(COLUMN_PARTICIPANT_ID, raceDetail.getParticipantId());
        values.put(COLUMN_STEP_NUM, raceDetail.getStepNum());
        values.put(COLUMN_RACE_STATUS, raceDetail.getRaceStatus());


        long id = db.insert(TABLE_NAME, null, values);
        raceDetail.setId(String.valueOf(id));
        return raceDetail;
    }

    public void updateRaceDetailStatus(RaceDetail raceDetail){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(COLUMN_RACE_STATUS, raceDetail.getRaceStatus());
        db.update(TABLE_NAME,values, "_ID = ?",new String[]{raceDetail.getId()});
    }

    public void updateRaceDetail(RaceDetail raceDetail){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(COLUMN_CREATOR, raceDetail.getCreatorId());
        values.put(COLUMN_PARTICIPANT_ID, raceDetail.getParticipantId());
        values.put(COLUMN_STEP_NUM, raceDetail.getStepNum());
        values.put(COLUMN_RACE_STATUS, raceDetail.getRaceStatus());
        values.put(COLUMN_RACE_COST_TIME, raceDetail.getRaceCostTime());
        db.update(TABLE_NAME,values, "_ID = ?",new String[]{raceDetail.getId()});
    }





    public List<RaceDetail> listRaceDetail(String ownerId){

        List<RaceDetail> racedetails = new ArrayList<RaceDetail>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where participant_id = ? order by _id desc;",new String[]{ownerId});
        RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(MyApplication.getAppContext());
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceId = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_ID));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String participantId = cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID));
            int stepNum = cursor.getInt(cursor.getColumnIndex(COLUMN_STEP_NUM));
            int raceStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_RACE_STATUS));
            long mCostTime = cursor.getLong(cursor.getColumnIndex(COLUMN_RACE_COST_TIME));
            RaceDetail bean = new RaceDetail(mId, raceId,creatorId,participantId,stepNum,raceStatus);
            bean.setRaceCostTime(mCostTime);
            RaceInfo raceInfo = raceInfoDao.getRaceInfoById(raceId);
            bean.setRaceInfo(raceInfo);
            racedetails.add(bean);
        }

        cursor.close();
        return racedetails;
    }


    public List<RaceDetail> listRaceDetailByCreatorId(String createId){

        List<RaceDetail> racedetails = new ArrayList<RaceDetail>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TableContract.RaceDetailEntry.TABLE_NAME + " where creator_id = ? order by _id desc;",new String[]{createId});
        RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(MyApplication.getAppContext());
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceId = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_ID));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String participantId = cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID));
            int stepNum = cursor.getInt(cursor.getColumnIndex(COLUMN_STEP_NUM));
            int raceStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_RACE_STATUS));
            long mCostTime = cursor.getLong(cursor.getColumnIndex(COLUMN_RACE_COST_TIME));
            RaceDetail bean = new RaceDetail(mId, raceId,creatorId,participantId,stepNum,raceStatus);
            bean.setRaceCostTime(mCostTime);
            RaceInfo raceInfo = raceInfoDao.getRaceInfoById(raceId);
            bean.setRaceInfo(raceInfo);
            racedetails.add(bean);
        }

        cursor.close();
        return racedetails;
    }
    public List<RaceDetail> listRaceDetailByCreatorIdAndRaceId(String createId,String theRaceId){

        List<RaceDetail> racedetails = new ArrayList<RaceDetail>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TableContract.RaceDetailEntry.TABLE_NAME + " where creator_id = ? and race_id = ? order by _id desc;",new String[]{createId,theRaceId});
        RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(MyApplication.getAppContext());
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceId = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_ID));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String participantId = cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID));
            int stepNum = cursor.getInt(cursor.getColumnIndex(COLUMN_STEP_NUM));
            int raceStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_RACE_STATUS));
            long mCostTime = cursor.getLong(cursor.getColumnIndex(COLUMN_RACE_COST_TIME));
            RaceDetail bean = new RaceDetail(mId, raceId,creatorId,participantId,stepNum,raceStatus);
            bean.setRaceCostTime(mCostTime);
            RaceInfo raceInfo = raceInfoDao.getRaceInfoById(raceId);
            bean.setRaceInfo(raceInfo);
            racedetails.add(bean);
        }

        cursor.close();
        return racedetails;
    }

    public RaceDetail getRaceDetailByRaceIdAndUserId(String searchRaceDetailId,String searchUserId) {
        List<RaceDetail> racedetails = new ArrayList<RaceDetail>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where _id = ? and participant_id = ? order by _id desc;",new String[]{searchRaceDetailId,searchUserId});
        RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(MyApplication.getAppContext());
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceId = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_ID));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String participantId = cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID));
            int stepNum = cursor.getInt(cursor.getColumnIndex(COLUMN_STEP_NUM));
            int raceStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_RACE_STATUS));
            long mCostTime = cursor.getLong(cursor.getColumnIndex(COLUMN_RACE_COST_TIME));
            RaceDetail bean = new RaceDetail(mId, raceId,creatorId,participantId,stepNum,raceStatus);
            bean.setRaceCostTime(mCostTime);
            RaceInfo raceInfo = raceInfoDao.getRaceInfoById(raceId);
            bean.setRaceInfo(raceInfo);
            racedetails.add(bean);

        }

        cursor.close();
        if(racedetails.size()>0){
            return racedetails.get(0);
        }
        return null;

    }
}
