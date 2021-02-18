package com.example.marathonapplication.run;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.marathonapplication.R;
import com.example.marathonapplication.db.race.RaceDetail;
import com.example.marathonapplication.db.race.RaceDetailDao;
import com.example.marathonapplication.db.race.RaceInfo;
import com.example.marathonapplication.model.Config;
import com.example.marathonapplication.run.view.CircleProgressView;
import com.example.marathonapplication.square.utils.DateUtils;
import com.example.marathonapplication.utils.DealUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class RunActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, SensorEventListener {


    CircleProgressView mCircleProgressView;
    Chronometer mChronometer;
    long lastCostSeconds = 0;
    TextView stepTv, totalTextView;
    Button startBtn, shareTwitterBtn;
    LinearLayout shootLinear;
    RaceDetailDao mRaceDetailDao;
    RaceDetail mRaceDetail;
    SupportMapFragment mapFragment;
    double mTotalLength;
    long seconds = 0;
    String filePath;
    private GoogleMap mMap;
    private SensorManager mSensroMgr;
    private double oriValue = 0;
    private double lstValue = 0;  //The last time the value of the
    private double curValue = 0;  //The current value
    private boolean motiveState = true;   //Whether or not they are in motion
    private boolean processState = true;   //Mark whether you are currently counting steps
    private int mStepCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSensroMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shootLinear = findViewById(R.id.shoot_linear);
        mRaceDetailDao = RaceDetailDao.getInstance(this);
        mRaceDetail = mRaceDetailDao.getRaceDetailByRaceIdAndUserId(getIntent().getStringExtra("raceDetailId"), Config.local_user_id);
        setTitle(mRaceDetail.getRaceInfo().getRaceName());
        initView();
        initData();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initData() {
        if (mRaceDetail != null) {
            mStepCounter = mRaceDetail.getStepNum();
            lastCostSeconds = mRaceDetail.getRaceCostTime();
            List<LatLng> latLngs = mRaceDetail.getRaceInfo().getLinePoints();
            stepTv.setText(mStepCounter + "");
            if (latLngs.size() >= 2) {
                float length = 0;
                float[] results = new float[1];
                for (int index = 0; index < latLngs.size() - 1; index++) {
                    Location.distanceBetween(latLngs.get(index).latitude, latLngs.get(index).longitude, latLngs.get(index + 1).latitude, latLngs.get(index + 1).longitude, results);
                    length += results[0];
                }
                totalTextView.setText(DealUtils.float2(length));
                mTotalLength = length;
                updateProgress();
            }
            mChronometer.setText(formatSeconds(lastCostSeconds + seconds));
            checkOverTime();
            isFinish();
        }

    }

    void startRunning() {
        startBtn.setText("PAUSE");
        mChronometer.start();
        processState = true;
    }

    void pauseRunning() {
        startBtn.setText("START");
        mChronometer.stop();
        processState = false;
    }

    /**
     * check over time
     */
    void checkOverTime() {

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int[] startDateInt = DateUtils.dealTheDate(mRaceDetail.getRaceInfo().getStartDate());
        int[] endDateInt = DateUtils.dealTheDate(mRaceDetail.getRaceInfo().getEndDate());
        if (startDateInt[0] - mYear > 0 || (startDateInt[0] - mYear == 0 && startDateInt[1] - mMonth > 0) || (startDateInt[0] - mYear == 0 && startDateInt[1] - mMonth == 0 && startDateInt[2] - mDay > 0)) {
            startBtn.setText("NOT TIME");
        }
        if (endDateInt[0] - mYear < 0 || (endDateInt[0] - mYear == 0 && endDateInt[1] - mMonth < 0) || (endDateInt[0] - mYear == 0 && endDateInt[1] - mMonth == 0 && endDateInt[2] - mDay < 0)) {
            startBtn.setText("OVER TIME");
        }
    }

    /**
     * check the step is arrive
     *
     * @return
     */
    boolean isFinish() {
        float percent = (float) (mStepCounter * Config.STEP_LENGTH * 100 / mTotalLength);
        ;
        if (percent > 100) {
            startBtn.setText("FINISH");
            if (mRaceDetail.getRaceStatus() != 2) {
                mRaceDetail.setStepNum(mStepCounter);
                mRaceDetail.setRaceCostTime(lastCostSeconds + seconds);
                mRaceDetail.setRaceStatus(2);
                mRaceDetailDao.updateRaceDetail(mRaceDetail);
            }
            return true;
        }
        return false;
    }

    void updateProgress() {

        float percent = (float) (mStepCounter * Config.STEP_LENGTH * 100 / mTotalLength);
        mCircleProgressView.setValue(Float.valueOf(DealUtils.float2(percent)));
    }

    private void initView() {


        mCircleProgressView = findViewById(R.id.progress_circular);
        mCircleProgressView.setCircleRingStyle(false);

        mChronometer = findViewById(R.id.totalTimeChronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                seconds++;
                chronometer.setText(formatSeconds(lastCostSeconds + seconds));
                if (seconds % 60 == 0) {
                    //1 minute save once
                    mRaceDetail.setStepNum(mStepCounter);
                    mRaceDetail.setRaceCostTime(lastCostSeconds + seconds);
                    mRaceDetailDao.updateRaceDetail(mRaceDetail);
                }
            }
        });
        startBtn = findViewById(R.id.start_btn);
        shareTwitterBtn = findViewById(R.id.share_btn);
        startBtn.setOnClickListener(this);
        shareTwitterBtn.setOnClickListener(this);
        stepTv = findViewById(R.id.step_run_tv);
        totalTextView = findViewById(R.id.total_length_tv);
    }

    private String formatSeconds(long seconds) {
        String hh = seconds / 3600 > 9 ? seconds / 3600 + "" : "0" + seconds / 3600;
        String mm = (seconds % 3600) / 60 > 9 ? (seconds % 3600) / 60 + "" : "0" + (seconds % 3600) / 60;
        String ss = (seconds % 3600) % 60 > 9 ? (seconds % 3600) % 60 + "" : "0" + (seconds % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_btn:
                popShotSrceenDialog();
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//                intent.setData(Uri.parse( "https://parentActivity.com/intent/tweet?text=Check out this link:&url=" + "https://123.com"+"&hashtags=Marathon"));
//                view.getContext().startActivity(intent);
                break;
            case R.id.start_btn:
                if (startBtn.getText().toString().equalsIgnoreCase("START")) {
                    startRunning();
                } else if (startBtn.getText().toString().equalsIgnoreCase("PAUSE")) {
                    pauseRunning();
                } else if (startBtn.getText().toString().equalsIgnoreCase("OVER TIME") || startBtn.getText().toString().equalsIgnoreCase("NOT TIME") || startBtn.getText().toString().equalsIgnoreCase("FINISH")) {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void popShotSrceenDialog() {
        final AlertDialog cutDialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.share_shoot_dialog, null);
        ImageView showImg = (ImageView) dialogView.findViewById(R.id.share_shoot_image);
        dialogView.findViewById(R.id.cancel_shoot_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.share_shoot_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    // Verify if the version is above 7.0
                    // Parameter 1 context, Parameter 2 Provider host address and configuration file to keep consistent
                    uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.marathonapplication.fileprovider", new File(filePath));
                    // Add this clause to temporarily authorize the file represented by the URI to the target
                } else {
                    uri = Uri.fromFile(new File(filePath));
                }
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/png");
                imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(imageIntent, "share"));
            }
        });
        // Gets the current screen size
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        // Generate images of the same size
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // Find the layout of the current page
        View view = shootLinear;
        // Set the cache
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        // Retrieves the image of the current screen from the cache
        temBitmap = view.getDrawingCache();
        // Save the image
        if (temBitmap != null) {
            try {
                File dirpath = getApplicationContext().getExternalFilesDir("image");
                // Image file path to get the system time
                long time = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                java.util.Date date = new java.util.Date(time);
                String str = sdf.format(date);
                filePath = dirpath.getPath() + File.separator + str + "screenshot.png";

                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                temBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Toast.makeText(RunActivity.this, "Failed to save, please check permissions or clean memory", Toast.LENGTH_SHORT).show();
            }
        }

        showImg.setImageBitmap(temBitmap);

        cutDialog.setView(dialogView);
        Window window = cutDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // get the width and height of the screen
        WindowManager.LayoutParams p = window.getAttributes(); /// Gets the current parameter values of the dialog box
        p.height = (int) (d.getHeight() * 0.8); // Set the height to 0.6 of the screen
        p.gravity = Gravity.CENTER;// Set the popup position
        window.setAttributes(p);
//        window.setWindowAnimations(R.style.dialogWindowAnim);
        cutDialog.show();
    }

    //向量求模
    public double magnitude(float x, float y, float z) {
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensroMgr.registerListener(this,
                mSensroMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensroMgr.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Set an accuracy range
        double range = 1;
        float[] value = sensorEvent.values;

        //Calculates the current modulus
        curValue = magnitude(value[0], value[1], value[2]);

        //The state of upward acceleration
        if (motiveState == true) {
            if (curValue >= lstValue) lstValue = curValue;
            else {
                //A peak was detected
                if (Math.abs(curValue - lstValue) > range) {
                    oriValue = curValue;
                    motiveState = false;
                }
            }
        }
        //The state of downward acceleration
        if (motiveState == false) {
            if (curValue <= lstValue) lstValue = curValue;
            else {
                if (Math.abs(curValue - lstValue) > range) {
                    //A peak was detected
                    oriValue = curValue;
                    if (processState == true) {
                        mStepCounter++;  //step + 1
                        stepTv.setText(mStepCounter + "");
                        updateProgress();
                        isFinish();
                    }
                    motiveState = true;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        RaceInfo mRaceInfo = mRaceDetail.getRaceInfo();
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true);
        for (LatLng latLng : mRaceInfo.getLinePoints()) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("point"));
            polylineOptions.add(latLng);
        }
        mMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRaceInfo.getLinePoints().get(0), 16));
    }
}