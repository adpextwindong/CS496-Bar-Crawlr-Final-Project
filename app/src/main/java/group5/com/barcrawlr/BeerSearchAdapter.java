package group5.com.barcrawlr;

/**
 * Created by aidan on 6/6/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;

public class BeerSearchAdapter extends RecyclerView.Adapter<BeerSearchAdapter.BeerItemViewHolder> {

    private ArrayList<BreweryDBUtils.beerDetail> mSearchResultsList;
    private OnBeerItemClickListener mBeerItemClickListener;

    public interface OnBeerItemClickListener {
        void onBeerItemClick(BreweryDBUtils.beerDetail beerDetail);
    }

    public BeerSearchAdapter(OnBeerItemClickListener clickListener) {
        mBeerItemClickListener = clickListener;
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
        private TextView mAbvTV;
        private TextView mStyleTV;
        private ImageView mIV;

        public BeerItemViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            mAbvTV = (TextView) itemView.findViewById(R.id.tv_search_result_abv);
            mStyleTV = (TextView)itemView.findViewById(R.id.tv_search_result_style);
            mIV = (ImageView)itemView.findViewById(R.id.iv_search_result);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println(mSearchResultTV.getText());
            BreweryDBUtils.beerDetail beerDetail = mSearchResultsList.get(getAdapterPosition());
            mBeerItemClickListener.onBeerItemClick(beerDetail);
        }

        public void bind(BreweryDBUtils.beerDetail searchResult) {
            mSearchResultTV.setText(searchResult.beerName);
            mAbvTV.setText(searchResult.abv);
            mStyleTV.setText(searchResult.style);

            if(searchResult.imageUrl != null)
                mIV.setImageURI(android.net.Uri.parse(searchResult.imageUrl));
        }
    }

}

