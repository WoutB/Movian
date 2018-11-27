package com.project.movian.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.project.movian.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Tutorial voor viewpager fragments gevolgd: https://www.codingdemos.com/android-tablayout-example-viewpager/
 */
public class CinemaFragment extends Fragment {

    @BindView(R.id.loadingBar)
    private ProgressBar mProgressBar;

    private String popularMovies;
    private String topRatedMovies;

    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopTopRatedList;

    public CinemaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cinema, container, false);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            popularMovies = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c2c572ea4e936f1e562f5d7b5b82909a";
            topRatedMovies = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=c2c572ea4e936f1e562f5d7b5b82909a";

            mPopularList = new ArrayList<>();
            mTopTopRatedList = new ArrayList<>();

            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}



