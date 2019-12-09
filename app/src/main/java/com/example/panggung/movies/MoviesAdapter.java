package com.example.panggung.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.panggung.R;
import com.example.panggung.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final MovieClickListener mOnClickListener;

    private List<Movie> mMovies;
    private Context mContext;

    public MoviesAdapter(Context context, MovieClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mContext.getString(R.string.tmdb_image_url, mMovies.get(position).getPosterPath()))
                .placeholder(mContext.getDrawable(R.mipmap.ic_launcher_foreground))
                .error(mContext.getDrawable(R.mipmap.empty_view))
                .into(holder.mImgPoster);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public void setMoviesData(List<Movie> moviesData) {
        mMovies = moviesData;
        notifyDataSetChanged();
    }

    public List<Movie> getList() {
        return mMovies;
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_poster)
        ImageView mImgPoster;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onMovieClick(mMovies.get(getAdapterPosition()));
        }
    }

    public interface MovieClickListener {
        void onMovieClick(Movie movie);
    }
}
