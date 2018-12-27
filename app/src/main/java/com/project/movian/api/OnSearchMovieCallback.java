package com.project.movian.api;

import com.project.movian.model.Movie;

import java.util.List;

public interface OnSearchMovieCallback {
    void onSuccess(int page, List<Movie> movies);

    void onError();
}
