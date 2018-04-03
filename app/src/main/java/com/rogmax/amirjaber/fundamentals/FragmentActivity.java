package com.rogmax.amirjaber.fundamentals;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.rogmax.amirjaber.fundamentals.adapters.PagerAdapter;
import com.rogmax.amirjaber.fundamentals.fragments.FragmentOne;
import com.rogmax.amirjaber.fundamentals.fragments.FragmentThree;
import com.rogmax.amirjaber.fundamentals.fragments.FragmentTwo;

public class FragmentActivity extends AppCompatActivity {

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        init();

        setupViewPager(viewPager);

        viewPager.setCurrentItem(0);

    }

    private void init() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne(), "Fragment 1");
        adapter.addFragment(new FragmentTwo(), "Fragment 2");
        adapter.addFragment(new FragmentThree(), "Fragment 3");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }

}
