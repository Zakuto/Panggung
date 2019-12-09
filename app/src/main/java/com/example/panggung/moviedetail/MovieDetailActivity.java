package com.example.panggung.moviedetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panggung.R;
import com.example.panggung.data.model.Movie;
import com.example.panggung.data.model.Review;
import com.example.panggung.data.model.Video;
import com.example.panggung.data.source.Repository;
import com.example.panggung.data.source.local.LocalDataSource;
import com.example.panggung.data.source.remote.RemoteDataSource;
import com.example.panggung.util.schedulers.SchedulerProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
        VideosAdapter.VideoClickListener {

    public static final String INTENT_EXTRA_MOVIE = "movie";

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;

    @BindView(R.id.progress_loading)
    ProgressBar mProgressLoading;
    @BindView(R.id.img_cover)
    ImageView mImgCover;
    @BindView(R.id.img_poster)
    ImageView mImgPoster;
    @BindView(R.id.fab_add_favorite)
    FloatingActionButton mFabAddFavorite;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.text_release_date)
    TextView mTextReleaseDate;
    @BindView(R.id.text_rating_score)
    TextView mTextRatingScore;
    @BindView(R.id.text_total_votes)
    TextView mTextTotalVotes;
    @BindView(R.id.rating_score)
    RatingBar mRatingScore;
    @BindView(R.id.rv_videos)
    RecyclerView mRvVideos;
    @BindView(R.id.text_synopsis)
    TextView mTextSynopsis;
    @BindView(R.id.text_reviews_title)
    TextView mTextReviewsTitle;
    @BindView(R.id.rv_reviews)
    RecyclerView mRvReviews;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        setUpReviews();
        setUpVideos();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPresenter();
        mMovieDetailPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMovieDetailPresenter.unsubscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                mMovieDetailPresenter.shareMovie();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(@NonNull MovieDetailContract.Presenter presenter) {
        mMovieDetailPresenter = presenter;
    }

    @Override
    public void showMovie(Movie movie) {
        Picasso.with(this)
                .load(getString(R.string.tmdb_backdrop_url, movie.getBackdropPath()))
                .into(mImgCover);
        Picasso.with(this)
                .load(getString(R.string.tmdb_image_url, movie.getPosterPath()))
                .into(mImgPoster);
        mTextTitle.setText(movie.getTitle());
        mTextReleaseDate.setText(movie.getReleaseDate());
        mTextRatingScore.setText(String.valueOf(movie.getVoteAverage()));
        mTextTotalVotes.setText(String.valueOf(movie.getVoteCount()));
        mRatingScore.setRating(movie.getVoteAverage() / 2);
        mTextSynopsis.setText(movie.getOverview());
    }

    @Override
    public void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            mTextReviewsTitle.setVisibility(View.GONE);
            mRvReviews.setVisibility(View.GONE);
        } else {
            mTextReviewsTitle.setVisibility(View.VISIBLE);
            mRvReviews.setVisibility(View.VISIBLE);
            mReviewsAdapter.setReviewsData(reviews);
        }
    }

    @Override
    public void showVideos(List<Video> videos) {
        if (videos.isEmpty()) {
            mRvVideos.setVisibility(View.GONE);
        } else {
            mRvVideos.setVisibility(View.VISIBLE);
            mVideosAdapter.setVideosData(videos);
        }
    }

    @Override
    public void showLoading() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressLoading.setVisibility(View.GONE);
    }

    @Override
    public void updateSavedMovie() {
        mFabAddFavorite.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_favorite_24dp));
    }

    @Override
    public void updateDeletedMovie() {
        mFabAddFavorite.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_favorite_border_24dp));
    }

    @Override
    public void showMessage(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this,
                message,
                Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    public void shareMovie(String movie, String video) {
        String mimeType = "text/plain";
        String title = getString(R.string.share_dialog_title);
        StringBuilder message = new StringBuilder();
        String space = " ";

        message.append(getString(R.string.sharing_text, movie));
        message.append(space);
        if (!video.isEmpty()) {
            message.append(getString(R.string.youtube_url));
            message.append(video);
        }

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message.toString())
                .startChooser();
    }

    @Override
    public void onVideoClick(Video video) {
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.youtube_url) + video.getKey()));
        Intent chooser = Intent.createChooser(playVideoIntent , "Open With");

        if (playVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @OnClick(R.id.fab_add_favorite)
    public void addFavorite(View view) {
        mMovieDetailPresenter.saveOrDeleteMovieAsFavorite();
    }

    private void setUpReviews() {
        mReviewsAdapter = new ReviewsAdapter(this);

        mRvReviews.setHasFixedSize(true);
        mRvReviews.setLayoutManager(new LinearLayoutManager(this));
        mRvReviews.setNestedScrollingEnabled(false);
        mRvReviews.addItemDecoration(new DividerItemDecoration(mRvReviews.getContext(),
                DividerItemDecoration.VERTICAL));
        mRvReviews.setAdapter(mReviewsAdapter);
    }

    private void setUpVideos() {
        mVideosAdapter = new VideosAdapter(this, this);

        mRvVideos.setHasFixedSize(true);
        mRvVideos.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        mRvVideos.setAdapter(mVideosAdapter);
    }

    private void initPresenter() {
        mMovieDetailPresenter = new MovieDetailPresenter(this,
                Repository.getInstance(RemoteDataSource.getInstance(),
                        LocalDataSource.getInstance(getApplicationContext())),
                SchedulerProvider.getInstance(),
                getIntent().getParcelableExtra(INTENT_EXTRA_MOVIE));
    }
}
