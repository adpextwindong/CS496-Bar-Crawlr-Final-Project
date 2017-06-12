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

public class BeerSearchAdapter extends RecyclerView.Adapter<BeerSearchAdapter.BeerItemViewHolder> {

    private ArrayList<BreweryDBUtils.beerDetail> mSearchResultsList;

    public BeerSearchAdapter()
    {

    }

    public void updateSearchResults(ArrayList<BreweryDBUtils.beerDetail> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public BeerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.beer_search_item, parent, false);
        return new BeerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeerItemViewHolder holder, int position) {
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

    class BeerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;

        public BeerItemViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
        }

        @Override
        public void onClick(View v) {
        }

        public void bind(BreweryDBUtils.beerDetail searchResult) {
            mSearchResultTV.setText(searchResult.beerName);
        }
    }

}

