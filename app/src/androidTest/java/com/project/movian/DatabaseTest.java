package com.project.movian;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.project.movian.database.DBRepository;
import com.project.movian.model.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private DBRepository dbRepo;
    private Movie movie;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        dbRepo = new DBRepository(context);
        if (dbRepo.findById(550) != null){
            dbRepo.deleteMovie(dbRepo.findById(550));
        }
    }

    @Test
    public void testDatabase() throws Exception {
        movie = new Movie();
        movie.setId(550);
        movie.setTitle("Fight Club");

        dbRepo.insertMovie(movie); //insert a favorite movie
        TimeUnit.SECONDS.sleep(5); //wait a few seconds, async insert

        Movie movie2 = dbRepo.findById(550);


        assertEquals(movie.getId(), movie2.getId());
        assertEquals(movie.getTitle(), movie2.getTitle());
    }
}
