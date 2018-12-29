package com.project.movian;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.movian.api.MovieRepository;
import com.project.movian.api.OnGetGenresCallback;
import com.project.movian.api.OnGetMovieCallback;
import com.project.movian.api.OnGetTrailersCallback;
import com.project.movian.database.DBRepository;
import com.project.movian.fragment.FavoritesFragment;
import com.project.movian.model.Genre;
import com.project.movian.model.Movie;
import com.project.movian.model.Trailer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private Movie mMovie;
    private Toolbar toolbar;
    private ImageView movieBackdrop;
    private ImageView moviePoster;
    private TextView movieShortInfo;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieTrailerName;
    private TextView movieRating;
    private TextView trailersLabel;
    private TextView movieLanguage;
    private TextView movieBudget;
    private TextView movieRevenue;
    private TextView movieHomepage;
    private LinearLayout movieTrailers;
    private MovieRepository moviesRepository;
    private int movieId;
    private FloatingActionButton mFab;
    private DBRepository dbRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        toolbar = findViewById(R.id.toolbar);
        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);
        mFab = (FloatingActionButton) findViewById(R.id.addToFavorites);
        moviesRepository = MovieRepository.getInstance();
        dbRepo = new DBRepository(getApplication());

        setupToolbar();
        initUI();
        getMovie();
        favoritesHandler();
    }

    private void favoritesHandler(){
        //check if movie is in favorites // yes? show delete button
        if (dbRepo.isFavorite(movieId) == movieId){
            mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else {
            mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (dbRepo.isFavorite(movieId) != movieId){
                    dbRepo.insertMovie(mMovie);
                    Toast.makeText(MovieDetailActivity.this, getResources().getString(R.string.success_add), Toast.LENGTH_SHORT).show();
                    mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                } else {
                    dbRepo.deleteMovie(mMovie);
                    Toast.makeText(MovieDetailActivity.this, getResources().getString(R.string.success_delete), Toast.LENGTH_SHORT).show();
                    mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieShortInfo = findViewById(R.id.movieShortInfo);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieTrailers = findViewById(R.id.movieTrailers);
        moviePoster = findViewById(R.id.item_movie_poster);
        trailersLabel = findViewById(R.id.trailersLabel);
        movieRating = findViewById(R.id.movieMDBRating);
        movieLanguage = findViewById(R.id.movieLanguage);
        movieBudget = findViewById(R.id.movieBudget);
        movieRevenue = findViewById(R.id.movieRevenue);
        movieHomepage = findViewById(R.id.movieHomepage);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                mMovie = movie;
                String year = movie.getReleaseDate().substring(0, 4)+ " \u2022 ";
                int hours = movie.getRuntime() / 60;
                int minutes = movie.getRuntime() % 60;
                String duration = hours + " hrs " + minutes + " mins";
                movieShortInfo.setText(year + duration );
                movieTitle.setText(movie.getTitle());
                movieOverview.setText(movie.getOverview());
                movieRating.setText(Float.toString(movie.getRating()));
                Locale loc = new Locale("",movie.getOriginal_language());
                movieLanguage.setText(loc.getDisplayCountry());
                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                movieBudget.setText(formatter.format(movie.getBudget()));
                movieRevenue.setText(formatter.format(movie.getRevenue()));
                movieHomepage.setText(movie.getHomepage());
                getTrailers(movie);
                getGenres(movie);
                if (!isFinishing()) {
                    Glide.with(MovieDetailActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                    Glide.with(MovieDetailActivity.this)
                            .load(IMAGE_BASE_URL + movie.getPosterPath())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(moviePoster);
                }
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void getTrailers(Movie movie) {
        moviesRepository.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                movieTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                        }
                    });
                    Glide.with(MovieDetailActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    movieTrailerName = parent.findViewById(R.id.movieTrailerName);
                    movieTrailerName.setText(trailer.getName());
                    movieTrailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showError() {
        Toast.makeText(MovieDetailActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
    }

}