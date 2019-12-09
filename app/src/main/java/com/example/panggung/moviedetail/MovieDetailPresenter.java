package com.example.panggung.moviedetail;

import androidx.annotation.NonNull;

import com.example.panggung.base.BaseObserver;
import com.example.panggung.data.Constants;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.model.Review;
import com.example.panggung.data.model.Video;
import com.example.panggung.data.source.DataSource;
import com.example.panggung.moviedetail.domain.DeleteMovieFromFavorites;
import com.example.panggung.moviedetail.domain.GetMovieFromFavorites;
import com.example.panggung.moviedetail.domain.GetMovieReviews;
import com.example.panggung.moviedetail.domain.GetMovieVideos;
import com.example.panggung.moviedetail.domain.SaveMovieAsFavorite;
import com.example.panggung.util.schedulers.BaseSchedulerProvider;

import java.util.List;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    @NonNull
    private MovieDetailContract.View mMovieDetailView;

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final Movie mMovie;
    private String mVideoKey;

    private final GetMovieReviews mGetMovieReviews;
    private final GetMovieVideos mGetMovieVideos;
    private final SaveMovieAsFavorite mSaveMovieAsFavorite;
    private final DeleteMovieFromFavorites mDeleteMovieFromFavorites;
    private final GetMovieFromFavorites mGetMovieFromFavorites;

    public MovieDetailPresenter(@NonNull MovieDetailContract.View movieDetailView,
                                @NonNull DataSource repository,
                                @NonNull BaseSchedulerProvider schedulerProvider,
                                @NonNull Movie movie) {
        mMovieDetailView = movieDetailView;
        mRepository = repository;
        mSchedulerProvider = schedulerProvider;
        mMovie = movie;

        mMovieDetailView.setPresenter(this);

        mGetMovieReviews = new GetMovieReviews(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetMovieVideos = new GetMovieVideos(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mSaveMovieAsFavorite = new SaveMovieAsFavorite(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mDeleteMovieFromFavorites = new DeleteMovieFromFavorites(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetMovieFromFavorites = new GetMovieFromFavorites(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());

    }

    @Override
    public void subscribe() {
        mMovieDetailView.showLoading();
        getMovie();
        getMovieReviews();
        getMovieVideos();
        getMovieFromFavorites();
    }

    @Override
    public void unsubscribe() {
        mGetMovieReviews.dispose();
        mGetMovieVideos.dispose();
        mSaveMovieAsFavorite.dispose();
        mDeleteMovieFromFavorites.dispose();
        mGetMovieFromFavorites.dispose();
    }

    @Override
    public void saveOrDeleteMovieAsFavorite() {
        if (mMovie.isFavorite()) {
            deleteMovieFromFavorites();
        } else {
            saveMovieAsFavorite();
        }
    }

    @Override
    public void shareMovie() {
        mMovieDetailView.shareMovie(mMovie.getTitle(), mVideoKey);
    }

    private void getMovie() {
        mMovieDetailView.showMovie(mMovie);
    }

    private void getMovieReviews() {
        mGetMovieReviews.execute(new MovieReviewsObserver(),
                GetMovieReviews.Params.forMovie(mMovie.getId()));
    }

    private void getMovieVideos() {
        mGetMovieVideos.execute(new MovieVideosObserver(),
                GetMovieVideos.Params.forMovie(mMovie.getId()));
    }

    private void saveMovieAsFavorite() {
        mSaveMovieAsFavorite.execute(new SaveFavoriteMovieObserver(),
                SaveMovieAsFavorite.Params.forMovie(mMovie));
    }

    private void deleteMovieFromFavorites() {
        mDeleteMovieFromFavorites.execute(new DeleteFavoriteMovieObserver(),
                DeleteMovieFromFavorites.Params.forMovie(mMovie.getId()));
    }

    private void getMovieFromFavorites() {
        mGetMovieFromFavorites.execute(new GetFavoriteMovieObserver(),
                GetMovieFromFavorites.Params.forMovie(mMovie.getId()));
    }

    private final class MovieReviewsObserver extends BaseObserver<List<Review>> {

        @Override
        public void onNext(List<Review> reviews) {
            mMovieDetailView.showReviews(reviews);
        }

        @Override
        public void onComplete() {
            mMovieDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class MovieVideosObserver extends BaseObserver<List<Video>> {

        @Override
        public void onNext(List<Video> videos) {
            mMovieDetailView.showVideos(videos);
            mVideoKey = videos.get(0).getKey();
        }

        @Override
        public void onComplete() {
            mMovieDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class SaveFavoriteMovieObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean isSaved) {
            if (isSaved) {
                mMovie.setFavorite(true);
                mMovieDetailView.updateSavedMovie();
                mMovieDetailView.showMessage(Constants.SAVED_AS_FAVORITE);
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class DeleteFavoriteMovieObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean isDeleted) {
            if (isDeleted) {
                mMovie.setFavorite(false);
                mMovieDetailView.updateDeletedMovie();
                mMovieDetailView.showMessage(Constants.DELETED_FROM_FAVORITES);
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class GetFavoriteMovieObserver extends BaseObserver<Movie> {

        @Override
        public void onNext(Movie movie) {
            if (movie.isFavorite()) {
                mMovie.setFavorite(true);
                mMovieDetailView.updateSavedMovie();
            } else {
                mMovie.setFavorite(false);
                mMovieDetailView.updateDeletedMovie();
            }

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}
