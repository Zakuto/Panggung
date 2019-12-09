package com.example.panggung.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.panggung.R;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panggung.data.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{
    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    private List<Review> mReviews;
    private Context mContext;

    public ReviewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextContent.setText(mReviews.get(position).getContent());
        holder.mTextAuthor.setText(
                mContext.getString(R.string.author, mReviews.get(position).getAuthor()));
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    public void setReviewsData(List<Review> reviewsData) {
        mReviews = reviewsData;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_content)
        TextView mTextContent;
        @BindView(R.id.text_author)
        TextView mTextAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
