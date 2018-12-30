package com.project.movian;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.project.movian.adapters.PageAdapter;
import com.project.movian.fragment.CinemaFragment;
import com.project.movian.fragment.FavoritesFragment;
import com.project.movian.fragment.TopRatedFragment;

import java.util.Locale;

import io.paperdb.Paper;

/**
 * Tutorial voor viewpager fragments gevolgd: https://www.codingdemos.com/android-tablayout-example-viewpager/
 * ***Ik moet wel in de instellingen instant run uitzetten (anders krijg ik een INSTALL_FAILED_INVALID_APK error)
 */
public class MainActivity extends AppCompatActivity
        implements  CinemaFragment.OnFragmentInteractionListener,
                    TopRatedFragment.OnFragmentInteractionListener,
                    FavoritesFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private TabItem tabFavorites;
    private TabItem tabTopRated;
    private TabItem tabCinema;
    public static String mLanguageCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        String language = Paper.book().read(getResources().getString(R.string.language_app));
        if (language == null)
            Paper.book().write(getResources().getString(R.string.language_app), "en");
        updateView(Paper.book().read(getResources().getString(R.string.language_app)).toString());

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
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(getApplicationContext(), lang);
        Resources resources = context.getResources();
        mLanguageCode = lang;

    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));

    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.onActionViewExpanded();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
        }
        return true;
    }
}

