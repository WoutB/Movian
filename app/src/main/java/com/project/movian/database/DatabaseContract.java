package com.project.movian.database;

import android.arch.persistence.room.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.project.movian.model.Movie;

/**
 * Werkcollege 4
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class DatabaseContract extends RoomDatabase {

    public abstract MovieDao movieDao();
    static DatabaseContract getDatabase(final Context context)
    {
        return Room.databaseBuilder(context.getApplicationContext(),
                DatabaseContract.class,
                "movian_database")
                .allowMainThreadQueries()
                .build();
    }
}
