package com.example.marathonapplication.square;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marathonapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class SquareActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    MarkerOptions markerStart;
    MarkerOptions markerEnd;
    String mode = "";
    Button startBtn,endBtn,centerBtn;
    SupportMapFragment mapFragment;
    List<MarkerOptions> markerList = new ArrayList<>();
    TextView lengthTv;
    private SensorManager mSensroMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSensroMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lengthTv = findViewById(R.id.lengthTv);
//        LoginUserDao loginUserDao = LoginUserDao.getInstance(this);
//        loginUserDao.addLoginUser("stu1","Lim","lim@gmail.com","street 1","12345678");
//        loginUserDao.addLoginUser("stu2","Lim2","lim2@gmail.com","street 2","12345678");
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

        if(latLngs.size()>2) {
            double length = 0;
            float[] results = new float[1];
            for(int index=0;index<latLngs.size()-1;index++){
                Location.distanceBetween(latLngs.get(index).latitude,latLngs.get(index).longitude,latLngs.get(index+1).latitude,latLngs.get(index+1).longitude,results);
                length += results[0];
            }
            lengthTv.setText(length + "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensroMgr.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        int suitable = 0;
        List<Sensor> sensorList = mSensroMgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                suitable += 1;
            } else if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                suitable += 10;
            } else if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                suitable = suitable;
            }

        }
        mSensroMgr.registerListener(this,
                mSensroMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
//        if (suitable/10>0 && suitable%10>0) {
//            mSensroMgr.registerListener(this,
//                    mSensroMgr.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
//                    SensorManager.SENSOR_DELAY_NORMAL);
//            mSensroMgr.registerListener(this,
//                    mSensroMgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        } else {
        lengthTv.setText("当前设备不支持计步器，请检查是否存在步行检测传感器和计步器传感器");
//        }
    }

    private int mStepDetector = 0;
    private int mStepCounter = 0;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //设定一个精度范围
        double range = 1;
        float[] value = sensorEvent.values;

        //计算当前的模
        curValue = magnitude(value[0], value[1], value[2]);

        //向上加速的状态
        if (motiveState == true) {
            if (curValue >= lstValue) lstValue = curValue;
            else {
                //检测到一次峰值
                if (Math.abs(curValue - lstValue) > range) {
                    oriValue = curValue;
                    motiveState = false;
                }
            }
        }
        //向下加速的状态
        if (motiveState == false) {
            if (curValue <= lstValue) lstValue = curValue;
            else {
                if (Math.abs(curValue - lstValue) > range) {
                    //检测到一次峰值
                    oriValue = curValue;
                    if (processState == true) {
                        mStepCounter++;  //步数 + 1
                        if (processState == true) {
                            String desc = String.format("设备检测到您当前走了%d步，总计数为%d步",
                                    mStepDetector, mStepCounter);
                            lengthTv.setText(desc);
                        }
                    }
                    motiveState = true;
                }
            }
        }



    }
    private int step = 0;   //步数
    private double oriValue = 0;  //原始值
    private double lstValue = 0;  //上次的值
    private double curValue = 0;  //当前值
    private boolean motiveState = true;   //是否处于运动状态
    private boolean processState = true;   //标记当前是否已经在计步
    //向量求模
    public double magnitude(float x, float y, float z) {
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}