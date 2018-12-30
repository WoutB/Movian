package com.project.movian.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.movian.fragment.CinemaFragment;
import com.project.movian.fragment.FavoritesFragment;
import com.project.movian.fragment.TopRatedFragment;

/**
 * Tutorial voor viewpager fragments gevolgd: https://www.codingdemos.com/android-tablayout-example-viewpager/
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CinemaFragment();
            case 1:
                return new TopRatedFragment();
            case 2:
                return new FavoritesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }


}