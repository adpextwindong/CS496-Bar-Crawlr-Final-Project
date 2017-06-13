package group5.com.barcrawlr;

/**
 * Created by aidan on 6/6/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;

public class BarSearchAdapter extends RecyclerView.Adapter<BarSearchAdapter.BarItemViewHolder> {

    private ArrayList<BreweryDBUtils.barDetail> mSearchResultsList;
    private OnBarItemClickListener mBarItemClickListener;

    public interface OnBarItemClickListener {
        void onBarItemClick(BreweryDBUtils.barDetail barDetail);
    }

    public BarSearchAdapter(OnBarItemClickListener clickListener) {
        mBarItemClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<BreweryDBUtils.barDetail> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public BarItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bar_search_item, parent, false);
        return new BarItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BarItemViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mSearchResultsList != null) {
            return mSearchResultsList.size();
        } else {
            return 0;
        }
    }

    class BarItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;
        private TextView mEstablishedTV;

        public BarItemViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            mEstablishedTV = (TextView) itemView.findViewById(R.id.tv_search_result_established);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println(mSearchResultTV.getText());
            BreweryDBUtils.barDetail barDetail = mSearchResultsList.get(getAdapterPosition());
            mBarItemClickListener.onBarItemClick(barDetail);
        }

        public void bind(BreweryDBUtils.barDetail searchResult) {
            mSearchResultTV.setText(searchResult.barName);
            mEstablishedTV.setText(searchResult.established);
        }
    }

}

