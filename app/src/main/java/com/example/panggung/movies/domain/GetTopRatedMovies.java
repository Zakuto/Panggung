package com.example.panggung.movies.domain;

import com.example.panggung.UseCase;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.source.DataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetTopRatedMovies extends UseCase<List<Movie>, Void> {
    private final DataSource mRepository;

    public GetTopRatedMovies(DataSource repository,
                             Scheduler threadExecutor,
                             Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<List<Movie>> buildUseCaseObservable(Void unused) {
        return mRepository.getTopRatedMovies();
    }
}
