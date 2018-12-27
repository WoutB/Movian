package com.project.movian.api;

import com.project.movian.model.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
