package com.example.panggung.data.source.remote;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.panggung.data.model.MovieResponse;
import com.example.panggung.data.model.ReviewResponse;
import com.example.panggung.data.model.VideoResponse;

public interface TheMovieDbApi {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Observable<ReviewResponse> getMovieReviews(@Path("movie_id") int movieId,
                                                @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Observable<VideoResponse> getMovieVideos(@Path("movie_id") int movieId,
                                              @Query("api_key") String apiKey);
}
