package com.software.glide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private PictPagerAdapter mAdapter;
    private List<Integer> mResd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResd.add(R.drawable.icon_1);
        mResd.add(R.drawable.icon_2);
        mResd.add(R.drawable.icon_3);
        mResd.add(R.drawable.icon_4);
        mResd.add(R.drawable.icon_5);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mAdapter = new PictPagerAdapter(this,mResd);
        mViewPager.setAdapter(mAdapter);

    }




}
