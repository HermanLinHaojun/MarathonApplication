package com.example.marathonapplication.statistics;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marathonapplication.R;
import com.example.marathonapplication.db.race.RaceDetail;
import com.example.marathonapplication.db.race.RaceInfo;
import com.example.marathonapplication.db.race.RaceInfoDao;
import com.example.marathonapplication.db.race.RaceStatistics;
import com.example.marathonapplication.model.Config;
import com.example.marathonapplication.utils.DealUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RaceStatisticsActivity extends AppCompatActivity {

    TextView startDateTv, endDateTv, raceNameTv, totalLengthTv;
    TextView totalRunLengthTv, totalJoinNumTv, totalRunFinishTv;
    RaceStatistics mRaceStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_statistics);
        initView();
    }

    private void initView() {
        RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(this);
        String raceId = getIntent().getStringExtra("raceId");
        mRaceStatistics = raceInfoDao.getRaceStatistics(raceId);
        RaceInfo mRaceInfo = mRaceStatistics.getRaceInfo();
        if (mRaceInfo != null) {
            startDateTv = findViewById(R.id.start_date_tv);
            endDateTv = findViewById(R.id.end_date_tv);
            startDateTv.setText(mRaceInfo.getStartDate());
            endDateTv.setText(mRaceInfo.getEndDate());
            raceNameTv = findViewById(R.id.race_name_tv);
            raceNameTv.setText(mRaceInfo.getRaceName());
            totalLengthTv = findViewById(R.id.length_tv);
            List<LatLng> latLngs = mRaceInfo.getLinePoints();
            if (latLngs.size() >= 2) {
                float length = 0;
                float[] results = new float[1];
                for (int index = 0; index < latLngs.size() - 1; index++) {
                    Location.distanceBetween(latLngs.get(index).latitude, latLngs.get(index).longitude, latLngs.get(index + 1).latitude, latLngs.get(index + 1).longitude, results);
                    length += results[0];
                }
                totalLengthTv.setText(DealUtils.float2(length) + "m");
            }
        }
        totalJoinNumTv = findViewById(R.id.total_join_num_tv);
        totalRunFinishTv = findViewById(R.id.total_run_finish_tv);
        totalRunLengthTv = findViewById(R.id.total_run_length_tv);
        List<RaceDetail> raceDetailList = mRaceStatistics.getRaceDetailList();
        if (raceDetailList.size() > 0) {
            int finishNum = 0;
            float runLength = 0;
            for (RaceDetail raceDetail : raceDetailList) {
                if (raceDetail.getRaceStatus() == 2) {
                    finishNum++;
                }
                runLength += (raceDetail.getStepNum() * Config.STEP_LENGTH);
            }
            totalJoinNumTv.setText("Total Join Num:    " + raceDetailList.size());
            totalRunFinishTv.setText("Total Run Finish:      " + finishNum);
            totalRunLengthTv.setText("Total Run Length:    " + runLength);
        }
    }
}