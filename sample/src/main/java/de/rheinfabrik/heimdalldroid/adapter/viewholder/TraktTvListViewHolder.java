package de.rheinfabrik.heimdalldroid.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import de.rheinfabrik.heimdalldroid.R;
import de.rheinfabrik.heimdalldroid.network.models.TraktTvList;

/**
 * View holder used to display a liked list.
 */
public class TraktTvListViewHolder extends RecyclerView.ViewHolder {

    // Members

    private TextView mTitleTextView;

    private TextView mDescriptionTextView;

    private TextView mLikeCountTextView;

    // Constructor

    public TraktTvListViewHolder(View itemView) {
        super(itemView);
        mTitleTextView = itemView.findViewById(R.id.titleTextView);
        mDescriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        mLikeCountTextView = itemView.findViewById(R.id.likeCountTextView);
    }

    // Public Api

    public void bind(TraktTvList traktTvList) {

        // Set title
        mTitleTextView.setText(traktTvList.name);

        // Set description
        mDescriptionTextView.setText(traktTvList.description);

        // Set like count
        mLikeCountTextView.setText(traktTvList.numberOfLikes + itemView.getContext().getString(R.string.likes_postfix));
    }
}
