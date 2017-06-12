package group5.com.barcrawlr;

/**
 * Created by aidan on 6/6/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BarSearchAdapter extends RecyclerView.Adapter<BarSearchAdapter.BarItemViewHolder> {

    private ArrayList<String> mBarItems;

    @Override
    public BarItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BarItemViewHolder holder, int position) {
        holder.bind(mBarItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BarItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public BarItemViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(String barItem) {

        }

        @Override
        public void onClick(View v) {
        }
    }

}

