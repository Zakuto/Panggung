package com.example.panggung.data.source.remote;

import androidx.annotation.Nullable;

import com.example.panggung.data.Constants;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.model.MovieResponse;
import com.example.panggung.data.model.Review;
import com.example.panggung.data.model.ReviewResponse;
import com.example.panggung.data.model.Video;
import com.example.panggung.data.model.VideoResponse;
import com.example.panggung.data.source.DataSource;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource implements DataSource {

    private static final String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/";

    @Nullable
    private static RemoteDataSource INSTANCE = null;
    private TheMovieDbApi mTheMovieDbApi;

    // Prevent direct instantiation.
    private RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mTheMovieDbApi = retrofit.create(TheMovieDbApi.class);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RemoteDataSource} instance
     */
    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance()} to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Movie>> getPopularMovies() {
        return mTheMovieDbApi.getPopularMovies(Constants.TMDB_API_KEY)
                .map(MovieResponse::getMovies);
    }

    @Override
    public Observable<List<Movie>> getTopRatedMovies() {
        return mTheMovieDbApi.getTopRatedMovies(Constants.TMDB_API_KEY)
                .map(MovieResponse::getMovies);
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Movie> getFavoriteMovieById(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        return mTheMovieDbApi.getMovieReviews(movieId, Constants.TMDB_API_KEY)
                .map(ReviewResponse::getReviews);
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        return mTheMovieDbApi.getMovieVideos(movieId, Constants.TMDB_API_KEY)
                .map(VideoResponse::getVideos);
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Movie movie) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        // Not used yet
        return null;
    }
}
