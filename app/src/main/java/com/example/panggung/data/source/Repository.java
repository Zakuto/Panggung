package com.example.panggung.data.source;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.panggung.data.model.Movie;
import com.example.panggung.data.model.Review;
import com.example.panggung.data.model.Video;

import java.util.List;

import io.reactivex.Observable;

public class Repository implements DataSource {

    @Nullable
    private static volatile Repository INSTANCE = null;

    @NonNull
    private final DataSource mRemoteDataSource;

    @NonNull
    private final DataSource mLocalDataSource;

    // Prevent direct instantiation.
    private Repository(@NonNull DataSource remoteDataSource,
                       @NonNull DataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @param localDataSource  the device storage data source
     * @return the {@link Repository} instance
     */
    public static Repository getInstance(@NonNull DataSource remoteDataSource,
                                         @NonNull DataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(DataSource, DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Movie>> getPopularMovies() {
        return mRemoteDataSource.getPopularMovies();
    }

    @Override
    public Observable<List<Movie>> getTopRatedMovies() {
        return mRemoteDataSource.getTopRatedMovies();
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        return mLocalDataSource.getFavoriteMovies();
    }

    @Override
    public Observable<Movie> getFavoriteMovieById(int movieId) {
        return mLocalDataSource.getFavoriteMovieById(movieId);
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        return mRemoteDataSource.getMovieReviews(movieId);
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        return mRemoteDataSource.getMovieVideos(movieId);
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Movie movie) {
        return mLocalDataSource.saveMovieAsFavorite(movie);
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        return mLocalDataSource.deleteMovieFromFavorites(movieId);
    }
}
