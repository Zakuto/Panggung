package com.example.panggung.movies;

import com.example.panggung.base.BasePresenter;
import com.example.panggung.base.BaseView;
import com.example.panggung.data.model.Movie;


import java.util.List;

public interface MoviesContract {
    interface View extends BaseView<Presenter>{
        void showMovies(List<Movie> movies, int nav);

        void showEmptyView(int nav);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter{
        void getPopularMovies();

        void getTopRatedMovies();

        void getFavoriteMovies();
    }
}
