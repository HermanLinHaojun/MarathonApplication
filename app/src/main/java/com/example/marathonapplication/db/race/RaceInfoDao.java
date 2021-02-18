package com.example.marathonapplication.db.race;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.marathonapplication.MyApplication;
import com.example.marathonapplication.R;
import com.example.marathonapplication.db.DbHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.marathonapplication.db.TableContract.RaceInfoEntry.COLUMN_CREATOR;
import static com.example.marathonapplication.db.TableContract.RaceInfoEntry.COLUMN_END_DATE;
import static com.example.marathonapplication.db.TableContract.RaceInfoEntry.COLUMN_LINE_POINTS;
import static com.example.marathonapplication.db.TableContract.RaceInfoEntry.COLUMN_RACE_NAME;
import static com.example.marathonapplication.db.TableContract.RaceInfoEntry.COLUMN_START_DATE;
import static com.example.marathonapplication.db.TableContract.RaceInfoEntry.TABLE_NAME;
import static com.example.marathonapplication.db.TableContract.RaceInfoEntry._ID;


public class RaceInfoDao {
    private DbHelper mDbHelper;
    private RaceInfoDao(Context ctx){
        mDbHelper = new DbHelper(ctx);
    }

    private static RaceInfoDao instance;
    public static synchronized RaceInfoDao getInstance(Context ctx){
        if(instance == null){
            instance = new RaceInfoDao(ctx);
        }
        return  instance;
    }

    public void addRaceInfo(RaceInfo raceInfo){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(COLUMN_RACE_NAME, raceInfo.getRaceName());
        values.put(COLUMN_CREATOR, raceInfo.getCreatorId());
        values.put(COLUMN_LINE_POINTS, new Gson().toJson(raceInfo.getLinePoints()));
        values.put(COLUMN_START_DATE, raceInfo.getStartDate());
        values.put(COLUMN_END_DATE, raceInfo.getEndDate());


        db.insert(TABLE_NAME, null, values);
    }
    public List<RaceInfo> listRaceInfo(){

        List<RaceInfo> raceInfos = new ArrayList<RaceInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by _id desc;",new String[]{});
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceName = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_NAME));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String linePoints = cursor.getString(cursor.getColumnIndex(COLUMN_LINE_POINTS));
            String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
            List<LatLng> latLngs = new Gson().fromJson(linePoints, new TypeToken<List<LatLng>>() {}.getType());

            RaceInfo bean = new RaceInfo(mId, raceName,creatorId,latLngs,startDate,endDate);
            //封装的对象添加到集合中
            raceInfos.add(bean);

        }

        cursor.close();
        return raceInfos;
    }
    public List<RaceInfo> listRaceInfo(String ownerId){

        List<RaceInfo> raceInfos = new ArrayList<RaceInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where creator_id = ? order by _id desc;",new String[]{ownerId});
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceName = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_NAME));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String linePoints = cursor.getString(cursor.getColumnIndex(COLUMN_LINE_POINTS));
            String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
            List<LatLng> latLngs = new Gson().fromJson(linePoints, new TypeToken<List<LatLng>>() {}.getType());

            RaceInfo bean = new RaceInfo(mId, raceName,creatorId,latLngs,startDate,endDate);
            //封装的对象添加到集合中
            raceInfos.add(bean);

        }

        cursor.close();
        return raceInfos;
    }


    public RaceStatistics getRaceStatistics(String mRaceId){
        RaceStatistics raceStatistics = new RaceStatistics();
        RaceInfo mRaceInfo = getRaceInfoById(mRaceId);
        RaceDetailDao raceDetailDao =  RaceDetailDao.getInstance(MyApplication.getAppContext());
        List<RaceDetail> raceDetails = raceDetailDao.listRaceDetailByCreatorIdAndRaceId(mRaceInfo.getCreatorId(),mRaceInfo.getId());
        raceStatistics.setRaceInfo(mRaceInfo);
        raceStatistics.setRaceDetailList(raceDetails);
        return raceStatistics;
    }

    public RaceInfo getRaceInfoById(String raceId) {
        List<RaceInfo> raceInfos = new ArrayList<RaceInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where _id = ? order by _id desc;",new String[]{raceId});
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String raceName = cursor.getString(cursor.getColumnIndex(COLUMN_RACE_NAME));
            String creatorId = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
            String linePoints = cursor.getString(cursor.getColumnIndex(COLUMN_LINE_POINTS));
            String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
            List<LatLng> latLngs = new Gson().fromJson(linePoints, new TypeToken<List<LatLng>>() {}.getType());

            RaceInfo bean = new RaceInfo(mId, raceName,creatorId,latLngs,startDate,endDate);
            //封装的对象添加到集合中
            raceInfos.add(bean);

        }

        cursor.close();
        return raceInfos.get(0);
    }
}
