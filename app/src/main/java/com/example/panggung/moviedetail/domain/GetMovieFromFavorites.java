package com.example.panggung.moviedetail.domain;

import com.example.panggung.UseCase;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.source.DataSource;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetMovieFromFavorites extends UseCase<Movie, GetMovieFromFavorites.Params> {

    private final DataSource mRepository;

    public GetMovieFromFavorites(DataSource repository,
                                 Scheduler threadExecutor,
                                 Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Movie> buildUseCaseObservable(Params params) {
        return mRepository.getFavoriteMovieById(params.movieId);
    }

    public static final class Params {

        private final int movieId;

        private Params(int movieId) {
            this.movieId = movieId;
        }

        public static Params forMovie(int movieId) {
            return new Params(movieId);
        }
    }
}
