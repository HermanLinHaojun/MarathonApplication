package com.example.marathonapplication.square;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.marathonapplication.MainActivity;
import com.example.marathonapplication.R;
import com.example.marathonapplication.db.race.RaceInfo;
import com.example.marathonapplication.db.race.RaceInfoDao;
import com.example.marathonapplication.utils.DealUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.marathonapplication.square.utils.DateUtils.showDatePickDialog;

public class RaceInitActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    MarkerOptions markerStart;
    MarkerOptions markerEnd;
    String mode = "";
    Button startBtn,endBtn,centerBtn,confirmBtn,cancelBtn;
    SupportMapFragment mapFragment;
    List<MarkerOptions> markerList = new ArrayList<>();
    TextView lengthTv;

    EditText marathonNameEditText;
    EditText startDateEditText;
    EditText endDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_init);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lengthTv = findViewById(R.id.length_tv);
        
        initView();

    }


    void initView(){
        marathonNameEditText = findViewById(R.id.marathon_name_edit);

        startDateEditText = findViewById(R.id.start_date_edit);
        endDateEditText = findViewById(R.id.end_date_edit);
        startDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDatePickDialog(view.getContext(),new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            startDateEditText.setText(year + "-" + (month + 1) + "-" + day);
                        }
                    },startDateEditText.getText().toString());
                }
            }
        });
        endDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDatePickDialog(view.getContext(),new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            endDateEditText.setText(year + "-" + (month + 1) + "-" + day);
                        }
                    },endDateEditText.getText().toString());
                }
            }
        });
        //date button init and the event
//        startDateBtn = findViewById(R.id.start_date_btn);
//        endDateBtn = findViewById(R.id.end_date_btn);
//        startDateBtn.setOnClickListener(this);
//        endDateBtn.setOnClickListener(this);

        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = "s";
            }
        });
        endBtn = findViewById(R.id.end_btn);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = "e";
            }
        });

        centerBtn =  findViewById(R.id.center_btn);
        centerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = "c";
            }
        });
        confirmBtn = findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(this);
        cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                switch (mode){
                    case "s":
                        markerStart = new MarkerOptions().position(latLng).title("startPoint");
                        redrawTheLoadLine();
                        break;
                    case "e":
                        markerEnd = new MarkerOptions().position(latLng).title("endPoint");
                        redrawTheLoadLine();
                        break;
                    case "c":
                        markerList.add(new MarkerOptions().position(latLng).title("center"));
                        redrawTheLoadLine();
                        break;
                    default:
                        break;
                }
            }
        });

        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    void redrawTheLoadLine(){
        mMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true);
        if(markerStart != null) {
            polylineOptions.add(markerStart.getPosition());
            mMap.addMarker(markerStart);
        }
        if(markerList.size()>0){
            for(MarkerOptions marker : markerList){
                mMap.addMarker(marker);
                polylineOptions.add(marker.getPosition());
            }
        }

        if(markerEnd != null ) {
            mMap.addMarker(markerEnd);
            polylineOptions.add(markerEnd.getPosition());
        }

        mMap.addPolyline(polylineOptions);

        List<LatLng> latLngs = polylineOptions.getPoints();

        if(latLngs.size() >= 2) {
            float length = 0;
            float[] results = new float[1];
            for(int index=0;index<latLngs.size()-1;index++){
                Location.distanceBetween(latLngs.get(index).latitude,latLngs.get(index).longitude,latLngs.get(index+1).latitude,latLngs.get(index+1).longitude,results);
                length += results[0];
            }
            lengthTv.setText((DealUtils.float2(length)));
        }
    }









    String checkInfo(){
        StringBuilder tip = new StringBuilder();
        if(marathonNameEditText.getText() == null || marathonNameEditText.getText().toString().equals("")){
            tip.append("you should input the marathonName\r\n");
        }
        if(startDateEditText.getText() == null || startDateEditText.getText().toString().equals("")){
            tip.append("you should input the startDate\r\n");
        }
        if(endDateEditText.getText() == null || endDateEditText.getText().toString().equals("")){
            tip.append("you should input the endDate\r\n");
        }
        if(markerStart == null){
            tip.append("you should set the start point\r\n");
        }
        if(markerEnd == null){
            tip.append("you should set the end point\r\n");
        }


        return tip.toString();
    }

    /**
     * pack the data
     * @return
     */
    RaceInfo packTheData() {
        RaceInfo raceInfo = new RaceInfo();
        raceInfo.setRaceName(marathonNameEditText.getText().toString());
        List<LatLng> linePoints = new ArrayList<>();
        linePoints.add(markerStart.getPosition());
        for(MarkerOptions markerOptions: markerList){
            linePoints.add(markerOptions.getPosition());
        }
        linePoints.add(markerEnd.getPosition());
        raceInfo.setLinePoints(linePoints);
        raceInfo.setStartDate(startDateEditText.getText().toString());
        raceInfo.setEndDate(endDateEditText.getText().toString());
        raceInfo.setCreatorId("1");
        return raceInfo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm_btn:
                String tip = checkInfo();
                if(tip.equals("")){
                    RaceInfo raceInfo = packTheData();
                    RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(view.getContext());
                    raceInfoDao.addRaceInfo(raceInfo);
                    Intent intent = new Intent(RaceInitActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    new AlertDialog.Builder(view.getContext()).setTitle("TIP").setMessage(tip).create().show();
                }
                break;
            case R.id.cancel_btn:
                finish();
            default:
                break;


        }
    }


}