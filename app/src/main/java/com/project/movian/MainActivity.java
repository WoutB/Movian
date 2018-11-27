package com.project.movian;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.movian.fragment.CinemaFragment;
import com.project.movian.fragment.FavoritesFragment;
import com.project.movian.fragment.TopRatedFragment;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements CinemaFragment.OnFragmentInteractionListener,
        TopRatedFragment.OnFragmentInteractionListener,
        FavoritesFragment.OnFragmentInteractionListener {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabFavorites;
    TabItem tabTopRated;
    TabItem tabCinema;

    private ListView mListView;
    private TextView mEmptyView;
    ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tablayout);
        tabCinema = findViewById(R.id.tabCinema);
        tabTopRated = findViewById(R.id.tabTopRated);
        tabFavorites = findViewById(R.id.tabFavorites);

        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

