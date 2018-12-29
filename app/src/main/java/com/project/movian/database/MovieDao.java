package com.project.movian.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.project.movian.model.Movie;

import java.util.List;

//Werkcollege 4 gevolgd van Mobile Apps: Android
@Dao
public interface MovieDao {
    @Insert
    long insert(Movie movie);
    @Insert
    void insert(Movie... movies);

    // Default SQLite behavior on insert or update = ABORT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(Movie... movies);

    // returns number of updated rows
    @Update
    int update(Movie... movies);
    // Default SQLite behavior on insert of update = ABORT
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAndReplace(Movie movie1, Movie movie2);

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


}
