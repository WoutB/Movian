package com.project.movian.database;

import android.content.Context;
import android.os.AsyncTask;

import com.project.movian.model.Movie;

import java.util.List;

public class DBRepository {
    private MovieDao mMovieDao;

    public DBRepository(Context application) {
        DatabaseContract db = DatabaseContract.getDatabase(application);
        mMovieDao = db.movieDao();
    }
    public List<Movie> getAllFavoriteMovies(){
        return mMovieDao.getAllFavoriteMovies();
    }
    public int isFavorite(int id){ return mMovieDao.isFavorite(id); }
    public Movie findById(int id){ return mMovieDao.findById(id); }

    public void insertMovie(Movie movie) {
        new insertMovieAsync(mMovieDao).execute(movie);
    }

    //NOT USED (Problem with return statements)
    private static class getFavoritesAsync extends AsyncTask<Void, Void, List<Movie>> {
        private MovieDao movieDao;

        getFavoritesAsync(MovieDao movieDao) { this.movieDao = movieDao; }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return movieDao.getAllFavoriteMovies();
        }
    }

    private static class insertMovieAsync extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        insertMovieAsync(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies);
            return null;
        }
    }
    public void deleteMovie(Movie movie) {
        new deleteMovieAsync(mMovieDao).execute(movie);
    }
    private static class deleteMovieAsync extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        deleteMovieAsync(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies);
            return null;
        }
    }
}
