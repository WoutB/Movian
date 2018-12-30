package com.project.movian.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.project.movian.model.Movie;

import java.util.List;

/**
 * Werkcollege 4
 */
@Dao
public interface MovieDao {
    @Insert
    long insert(Movie movie);
    @Insert
    void insert(Movie... movies);

    // returns number of deleted rows
    @Delete
    int delete(Movie... movies);

    @Query("SELECT * FROM favorite_movie")
    List<Movie> getAllFavoriteMovies();

    @Query("DELETE FROM favorite_movie")
    void deleteAll();

    @Query("DELETE FROM favorite_movie WHERE id = :id")
    void delete(int id);
    @Query("SELECT id FROM favorite_movie WHERE id = :id")
    int isFavorite(int id);
    @Query("SELECT * FROM favorite_movie WHERE id LIKE :id LIMIT 1")
    Movie findById(int id);

}
