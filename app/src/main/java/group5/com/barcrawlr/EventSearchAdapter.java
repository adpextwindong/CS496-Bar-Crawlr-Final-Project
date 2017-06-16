package group5.com.barcrawlr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;

/**
 * Created by RheaMae on 6/15/2017.
 */

public class EventSearchAdapter extends RecyclerView.Adapter<EventSearchAdapter.EventItemViewHolder> {

    private ArrayList<BreweryDBUtils.eventDetail> mSearchResultsList;
    private EventSearchAdapter.OnEventItemClickListener mEventItemClickListener;

    public interface OnEventItemClickListener {
        void onEventItemClick(BreweryDBUtils.eventDetail eventDetail);
    }

    public EventSearchAdapter(EventSearchAdapter.OnEventItemClickListener clickListener) {
        mEventItemClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<BreweryDBUtils.eventDetail> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public EventSearchAdapter.EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_search_item, parent, false);
        return new EventSearchAdapter.EventItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventSearchAdapter.EventItemViewHolder holder, int position) {
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

    class EventItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;
        private TextView mDateTV;
        private ImageView mIV;

        public EventItemViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            mDateTV = (TextView) itemView.findViewById(R.id.tv_search_result_date);
            mIV = (ImageView)itemView.findViewById(R.id.iv_search_result);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println(mSearchResultTV.getText());
            BreweryDBUtils.eventDetail eventDetail = mSearchResultsList.get(getAdapterPosition());
            mEventItemClickListener.onEventItemClick(eventDetail);
        }

        public void bind(BreweryDBUtils.eventDetail searchResult) {
            mSearchResultTV.setText(searchResult.eventName);
            mDateTV.setText(searchResult.date);

            if(searchResult.imageUrl != null)
                mIV.setImageURI(android.net.Uri.parse(searchResult.imageUrl));
        }
    }
}
