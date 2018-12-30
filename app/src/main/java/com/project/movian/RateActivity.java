package com.project.movian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.project.movian.fragment.RateFragment;
import com.project.movian.fragment.ShowRatingFragment;

/**
 * Werkcollege 3
 * */
public class RateActivity extends AppCompatActivity implements RateFragment.RatingInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void ratingBarChanged(int r) {
        ShowRatingFragment showRatingFragment = (ShowRatingFragment)getSupportFragmentManager().findFragmentById(R.id.fmt_text);
        showRatingFragment.ratingChanged(r);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
