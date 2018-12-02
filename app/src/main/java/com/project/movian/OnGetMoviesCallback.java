package com.project.movian;

import com.project.movian.model.Movie;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(List<Movie> movies);

    void onError();
}
