package com.project.movian.adapters;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.movian.R;
import com.project.movian.api.OnMoviesClickCallback;
import com.project.movian.database.DBRepository;
import com.project.movian.model.Genre;
import com.project.movian.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Tutorial voor ophalen en weergeven van films http://imakeanapp.com/make-a-movies-app-using-tmdb-api-part-1-introduction/
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private List<Movie> movies;
    private List<Genre> allGenres;
    private OnMoviesClickCallback callback;
    private DBRepository dbRepo;

    public MovieAdapter(List<Movie> movies, List<Genre> allGenres, OnMoviesClickCallback callback, Application application) {
        this.movies = movies;
        this.allGenres = allGenres;
        this.callback = callback;
        dbRepo = new DBRepository(application);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }
    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        ImageView heart;
        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);
            heart = itemView.findViewById(R.id.ic_heart);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
        }

        public void bind(final Movie movie) {
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
            if(isFav()){
                heart.setImageResource(R.drawable.ic_heart_yellow_full);
            } else {
                heart.setImageResource(R.drawable.ic_heart_yellow);
            };
            heart.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (!isFav()){
                        dbRepo.insertMovie(movie);
                        heart.setImageResource(R.drawable.ic_heart_yellow_full);
                    } else {
                        dbRepo.deleteMovie(movie);
                        heart.setImageResource(R.drawable.ic_heart_yellow);
                    }
                }
            });
        }

        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
        private boolean isFav(){
            if (dbRepo.isFavorite(movie.getId()) == movie.getId()){
                return true;
            } else {
                return false;
            }
        }
    }
}