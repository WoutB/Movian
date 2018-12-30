package com.project.movian.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.movian.R;

/**
 * Werkcollege 3
 * */
public class ShowRatingFragment extends Fragment {
    private TextView txtStarRating;

    public ShowRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_rating, container, false);
        txtStarRating = view.findViewById(R.id.textRating);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String rating = sharedPref.getString("rating_app", "0");
        ratingChanged(Integer.parseInt(rating));
        return view;
    }
    public void ratingChanged(int waarde){
        txtStarRating.setText(getString(R.string.user_rating, waarde));
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getResources().getString(R.string.rating_app), waarde);
        editor.commit();
        //do something with rating

    }

}
