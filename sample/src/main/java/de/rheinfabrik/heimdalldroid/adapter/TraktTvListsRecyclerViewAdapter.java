package de.rheinfabrik.heimdalldroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import de.rheinfabrik.heimdalldroid.R;
import de.rheinfabrik.heimdalldroid.adapter.viewholder.TraktTvListViewHolder;
import de.rheinfabrik.heimdalldroid.network.models.TraktTvList;

/**
 * Simple adapter for showing a list of liked lists.
 */
public class TraktTvListsRecyclerViewAdapter extends RecyclerView.Adapter<TraktTvListViewHolder> {

    // Members

    private final List<TraktTvList> mTraktTvLists;

    // Constructor

    public TraktTvListsRecyclerViewAdapter(List<TraktTvList> traktTvLists) {
        super();

        mTraktTvLists = traktTvLists;
    }

    // RecyclerView.Adapter

    @Override
    public TraktTvListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_trakt_tv_list, parent, false);
        return new TraktTvListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TraktTvListViewHolder holder, int position) {
        holder.bind(mTraktTvLists.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTraktTvLists != null) {
            return mTraktTvLists.size();
        }

        return 0;
    }
}
