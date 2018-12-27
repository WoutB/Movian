package com.project.movian;

import android.support.annotation.NonNull;
import android.util.Log;

import com.project.movian.api.MovieResponse;
import com.project.movian.api.OnGetGenresCallback;
import com.project.movian.api.OnGetMovieCallback;
import com.project.movian.api.OnGetMoviesCallback;
import com.project.movian.api.OnGetTrailersCallback;
import com.project.movian.api.TheMovieDatabaseAPI;
import com.project.movian.api.TrailerResponse;
import com.project.movian.model.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static MovieRepository repository;

    private TheMovieDatabaseAPI api;

    private MovieRepository(TheMovieDatabaseAPI api) {
        this.api = api;
    }

    public static MovieRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MovieRepository(retrofit.create(TheMovieDatabaseAPI.class));
        }

        return repository;
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MovieResponse> call = new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies("c2c572ea4e936f1e562f5d7b5b82909a", LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getNowPlayingMovies("c2c572ea4e936f1e562f5d7b5b82909a", LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }
    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres("c2c572ea4e936f1e562f5d7b5b82909a", LANGUAGE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, "c2c572ea4e936f1e562f5d7b5b82909a", LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, "c2c572ea4e936f1e562f5d7b5b82909a", LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
