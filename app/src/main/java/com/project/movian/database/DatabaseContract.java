package com.project.movian.database;

import android.arch.persistence.room.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.project.movian.model.Movie;

@Database(entities = {Movie.class}, version = 1)
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
