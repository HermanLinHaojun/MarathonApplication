package com.example.marathonapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.marathonapplication.personal.MyRaceFragment;
import com.example.marathonapplication.square.ItemFragment;
import com.example.marathonapplication.square.RaceInitActivity;
import com.example.marathonapplication.statistics.MyCreateRaceFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mFragments;


    private LinearLayout mTab1;
    private LinearLayout mTab2;
    private LinearLayout mTab3;

    //four Tab ImageButton
    private ImageButton mImg1;
    private ImageButton mImg2;
    private ImageButton mImg3;

    Button addRaceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        initViews();//Initialize the controller
        initEvents();//Initialize the event
        initDatas();// initialize the data
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        mFragments.add(new MyRaceFragment());
        mFragments.add(new ItemFragment());
        mFragments.add(new MyCreateRaceFragment());

        // Initialize the adapter
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }
        };
        // Don't forget to set the ViewPager adapter
        mViewPager.setAdapter(mAdapter);
        // Set the ViewPager toggle listener
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            // page scrolling events
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // The page selects the event
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            // page scrolling state change event
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents() {
        // Set four Tab click events
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        addRaceBtn.setOnClickListener(this);
    }

    // Initialize the controller
    private void initViews() {
        mViewPager = findViewById(R.id.id_viewpager);

        mTab1 =  findViewById(R.id.id_tab1);
        mTab2 =  findViewById(R.id.id_tab2);
        mTab3 =  findViewById(R.id.id_tab3);

        mImg1 =  findViewById(R.id.id_tab_img1);
        mImg2 =  findViewById(R.id.id_tab_img2);
        mImg3 =  findViewById(R.id.id_tab_img3);
        addRaceBtn = findViewById(R.id.add_race_btn);
    }

    @Override
    public void onClick(View v) {
        // Set the four imageButtons to gray
        resetImgs();

        // Switch to different pages based on the Tab clicked and set the corresponding ImageButton to green
        switch (v.getId()) {
            case R.id.id_tab1:
                selectTab(0);
                break;
            case R.id.id_tab2:
                selectTab(1);
                break;
            case R.id.id_tab3:
                selectTab(2);
                break;
            case R.id.add_race_btn:
                Intent intent = new Intent(MainActivity.this, RaceInitActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void selectTab(int i) {
        // Set ImageButton to green based on the Tab you clicked on
        switch (i) {
            case 0:
                mImg1.setImageResource(R.drawable.person_center_32_32_click);
                break;
            case 1:
                mImg2.setImageResource(R.drawable.square_32_32_click);
                break;
            case 2:
                mImg3.setImageResource(R.drawable.deploy_32_32_click);
                break;
        }
        // Sets the page for the current Tab
        mViewPager.setCurrentItem(i);
    }


    private void resetImgs() {
        mImg1.setImageResource(R.drawable.person_center_32_32);
        mImg2.setImageResource(R.drawable.square_32_32);
        mImg3.setImageResource(R.drawable.deploy_32_32);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_add){
                Intent intent = new Intent(MainActivity.this, RaceInitActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}