package com.project.movian.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.movian.MovieAdapter;
import com.project.movian.MovieDetailActivity;
import com.project.movian.api.MovieRepository;
import com.project.movian.api.OnGetGenresCallback;
import com.project.movian.api.OnGetMoviesCallback;
import com.project.movian.R;
import com.project.movian.api.OnMoviesClickCallback;
import com.project.movian.model.Genre;
import com.project.movian.model.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Tutorial voor viewpager fragments gevolgd: https://www.codingdemos.com/android-tablayout-example-viewpager/
 */
public class CinemaFragment extends Fragment {

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MovieRepository movieRepo;
    private List<Genre> movieGenres;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private String sortBy = MovieRepository.UPCOMING;


    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };
    public CinemaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cinema, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        movieRepo = MovieRepository.getInstance();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.new_movies);
        //mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupOnScrollListener();

        getGenres();
        return view;

    }
    private void getGenres() {
        movieRepo.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }
            @Override
            public void onError() {
                showError();
                }
            });
        }
    private void getMovies(int page) {
        isFetchingMovies = true;
        movieRepo.getMovies(page,sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (mAdapter == null) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mAdapter = new MovieAdapter(movies, movieGenres, callback);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    if (page == 1) {
                        mAdapter.clearMovies();
                    }
                    mAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }
            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void showError() {
        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
    }
    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}



