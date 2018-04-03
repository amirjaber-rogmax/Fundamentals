package com.rogmax.amirjaber.fundamentals;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rogmax.amirjaber.fundamentals.adapters.TabPageAdapter;
import com.rogmax.amirjaber.fundamentals.fragments.Tab1;
import com.rogmax.amirjaber.fundamentals.fragments.Tab2;
import com.rogmax.amirjaber.fundamentals.fragments.Tab3;

public class TabActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener, Tab3.OnFragmentInteractionListener{

    private static final String TAG = "TabActivity";

    private TabPageAdapter tabPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        tabPageAdapter = new TabPageAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        TabPageAdapter adapter = new TabPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1(), "Tab 1");
        adapter.addFragment(new Tab2(), "Tab 2");
        adapter.addFragment(new Tab3(), "Tab 3");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
