package com.project.movian.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.movian.MovieDetailActivity;
import com.project.movian.R;
import com.project.movian.adapters.MovieAdapter;
import com.project.movian.adapters.MovieCardAdapter;
import com.project.movian.api.MovieRepository;
import com.project.movian.api.OnGetMoviesCallback;
import com.project.movian.api.OnMoviesClickCallback;
import com.project.movian.database.DBRepository;
import com.project.movian.database.MovieDao;
import com.project.movian.model.Genre;
import com.project.movian.model.Movie;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Tutorial voor viewpager fragments gevolgd: https://www.codingdemos.com/android-tablayout-example-viewpager/
 */
public class FavoritesFragment extends Fragment {

    private MovieCardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView noFavorites;
    private List<Movie> movies;
    private DBRepository dbRepo;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    public FavoritesFragment() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        noFavorites = view.findViewById(R.id.noFavorites);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fav_movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        dbRepo = new DBRepository(this.getActivity().getApplication());
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mAdapter.clearMovies();
                        getMovies();
                        mSwipeRefreshLayout.setRefreshing(false);;
                    }
                }
        );
        getMovies();
        return view;
    }

    private void getMovies() {
        movies = dbRepo.getAllFavoriteMovies();

        if (!movies.isEmpty()) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mAdapter = new MovieCardAdapter(movies, callback);
                mRecyclerView.setAdapter(mAdapter);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            noFavorites.setVisibility(View.VISIBLE);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
