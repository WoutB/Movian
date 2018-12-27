package com.project.movian;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.movian.api.MovieRepository;
import com.project.movian.api.OnGetGenresCallback;
import com.project.movian.api.OnGetMoviesCallback;
import com.project.movian.api.OnMoviesClickCallback;
import com.project.movian.api.OnSearchMovieCallback;
import com.project.movian.model.Genre;
import com.project.movian.model.Movie;

import java.util.List;

//https://developer.android.com/training/search/setup#java
public class SearchActivity extends AppCompatActivity {
    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MovieRepository movieRepo;
    private List<Genre> movieGenres;
    private TextView resultQuery;
    private TextView noResults;
    private String query;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private Toolbar mToolbar;
    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(SearchActivity.this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();

        mProgressBar = (ProgressBar) findViewById(R.id.loadingBar);
        movieRepo = MovieRepository.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.search_results);
        resultQuery = findViewById(R.id.resultQuery);
        noResults = findViewById(R.id.noResults);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupOnScrollListener();

        handleIntent(getIntent());
    }
    private void setupToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.search_results));
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            this.query = query;
            resultQuery.setText(query);
            getGenres(query);
        }
    }
    private void getGenres(String q) {
        movieRepo.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                searchMovie(currentPage, getQuery());
            }
            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void searchMovie(int page, String query) {
        isFetchingMovies = true;
        movieRepo.searchMovie(page,query, new OnSearchMovieCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (movies.isEmpty()){
                    noResults.setVisibility(View.VISIBLE);
                    noResults.setText(getResources().getString(R.string.no_search_results));
                    mProgressBar.setVisibility(View.GONE);
                } else {
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
            }}
            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void showError() {
        Toast.makeText(this, "Nothing found", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String getQuery(){ return this.query; }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        searchMovie(currentPage + 1, getQuery());
                    }
                }
            }
        });
    }

}
