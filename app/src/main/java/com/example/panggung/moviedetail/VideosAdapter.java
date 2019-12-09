package com.example.panggung.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panggung.R;
import com.example.panggung.data.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private static final String TAG = VideosAdapter.class.getSimpleName();

    private final VideoClickListener mOnClickListener;

    private List<Video> mVideos;
    private Context mContext;

    public VideosAdapter(Context context, VideoClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextVideo.setText(mVideos.get(position).getName());
        Picasso.with(mContext)
                .load(mContext.getString(R.string.youtube_thumbnail_url, mVideos.get(position).getKey()))
                .into(holder.mImgThumbnail);
    }

    @Override
    public int getItemCount() {
        if (null == mVideos) return 0;
        return mVideos.size();
    }

    public void setVideosData(List<Video> videosData) {
        mVideos = videosData;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_thumbnail)
        ImageView mImgThumbnail;

        @BindView(R.id.text_video)
        TextView mTextVideo;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onVideoClick(mVideos.get(getAdapterPosition()));
        }
    }

    public interface VideoClickListener {
        void onVideoClick(Video video);
    }
}
