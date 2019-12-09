package com.example.panggung.data.source;

import java.util.List;

import io.reactivex.Observable;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.model.Review;
import com.example.panggung.data.model.Video;

public interface DataSource {

    Observable<List<Movie>> getPopularMovies();

    Observable<List<Movie>> getTopRatedMovies();

    Observable<List<Movie>> getFavoriteMovies();

    Observable<Movie> getFavoriteMovieById(int movieId);

    Observable<List<Review>> getMovieReviews(int movieId);

    Observable<List<Video>> getMovieVideos(int movieId);

    Observable<Boolean> saveMovieAsFavorite(Movie movie);

    Observable<Boolean> deleteMovieFromFavorites(int movieId);
}
