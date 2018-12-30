package com.project.movian.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.project.movian.R;

/**
 * Werkcollege 3
 * */
public class RateFragment extends Fragment {
    private RatingInterface activityCallback;

    public interface RatingInterface {
        void ratingBarChanged(int r);
    }

    public RateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.fragment_rate, container, false);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                activityCallback.ratingBarChanged((int) v);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RatingInterface){
            activityCallback = (RatingInterface) context;
        } else {
            throw new ClassCastException("Mainactivity does not implement ratingInterface");
        }
    }

    private void ratingBarChanged(int f) {
        activityCallback.ratingBarChanged(f);
    }


}
