package com.example.marathonapplication.square;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marathonapplication.R;
import com.example.marathonapplication.db.race.RaceDetail;
import com.example.marathonapplication.db.race.RaceDetailDao;
import com.example.marathonapplication.db.race.RaceInfo;
import com.example.marathonapplication.db.race.RaceInfoDao;
import com.example.marathonapplication.model.Config;
import com.example.marathonapplication.utils.DealUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class RaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    String raceId;
    RaceInfo mRaceInfo;
    RaceDetail mRaceDetail;
    Button joinBtn,cacelBtn;
    RaceDetailDao mRaceDetailDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_detail);

        initView();
    }

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    TextView startDateTv,endDateTv,raceNameTv,totalLengthTv;
    private void initView() {
        raceId = getIntent().getStringExtra("raceId");
        final RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(this);
        mRaceInfo = raceInfoDao.getRaceInfoById(raceId);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        joinBtn = findViewById(R.id.joinBtn);
        mRaceDetailDao = RaceDetailDao.getInstance(this);
        mRaceDetail = mRaceDetailDao.getRaceDetailByRaceIdAndUserId(mRaceInfo.getId(),Config.local_user_id);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if(mRaceDetail == null){

                    RaceDetail raceDetail = new RaceDetail();
                    raceDetail.setCreatorId(mRaceInfo.getCreatorId());
                    raceDetail.setParticipantId(Config.local_user_id);
                    raceDetail.setRaceId(mRaceInfo.getId());
                    raceDetail.setStepNum(0);
                    raceDetail.setRaceStatus(1);
                    mRaceDetail = mRaceDetailDao.addRaceDetail(raceDetail);
                    joinBtn.setText("cancel_the_race");
                }else {
                    if(joinBtn.getText().toString().equalsIgnoreCase("join_the_race")){
                        mRaceDetail.setRaceStatus(1);
                        mRaceDetailDao.updateRaceDetailStatus(mRaceDetail);
                        joinBtn.setText("cancel_the_race");
                    }else{
                        mRaceDetail.setRaceStatus(0);
                        mRaceDetailDao.updateRaceDetailStatus(mRaceDetail);
                        joinBtn.setText("join_the_race");
                    }
                }
            }
        });
        cacelBtn = findViewById(R.id.cancel_btn);
        cacelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true);
        for(LatLng latLng:mRaceInfo.getLinePoints()){
            mMap.addMarker(new MarkerOptions().position(latLng).title("point"));
            polylineOptions.add(latLng);
        }
        mMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRaceInfo.getLinePoints().get(0), 16));
    }
}