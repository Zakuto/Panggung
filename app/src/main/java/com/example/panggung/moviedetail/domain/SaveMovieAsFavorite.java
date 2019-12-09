package com.example.panggung.moviedetail.domain;

import com.example.panggung.UseCase;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.source.DataSource;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class SaveMovieAsFavorite extends UseCase<Boolean, SaveMovieAsFavorite.Params> {

    private final DataSource mRepository;

    public SaveMovieAsFavorite(DataSource repository,
                               Scheduler threadExecutor,
                               Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(Params params) {
        return mRepository.saveMovieAsFavorite(params.movie);
    }

    public static final class Params {

        private final Movie movie;

        private Params(Movie movie) {
            this.movie = movie;
        }

        public static Params forMovie(Movie movie) {
            return new Params(movie);
        }
    }
}
