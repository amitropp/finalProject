package com.friends.stay.keepintouch;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avi on 15/05/2017.
 * This is a class which helps the user to create tabs in his code
 */

public class Tabs {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context;
    private String[] tabNames;
    private Fragment[] tabFragments;
    private View rootView;
    private AppCompatActivity activity;

    /**
     * ctor
     * @param context context
     * @param tabNames name of tabs
     * @param tabFragments an array of classes which extend fragment - these are the tabs
     */
    public Tabs(Context context, String[] tabNames, Fragment[] tabFragments)
    {
        this.context = context;
        this.tabNames = tabNames;
        this.tabFragments = tabFragments;
        rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        activity = (AppCompatActivity) context;
        setTabs();
    }

    /**
     * set tabs and show them on screen
     */
    private void setTabs() {

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    //setup the tabs of the page
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager());

        //loop over tabs and add them to sceen
        for (int i = 0; i < tabNames.length; i++)
        {
            adapter.addFragment(tabFragments[i], tabNames[i]);
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }


}

