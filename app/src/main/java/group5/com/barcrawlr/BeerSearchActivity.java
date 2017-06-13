package group5.com.barcrawlr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.NetworkUtils;

/**
 * Created by georgecrary on 6/4/17.
 */

public class BeerSearchActivity extends AppCompatActivity
        implements BeerSearchAdapter.OnBeerItemClickListener{

    Button mButtonSearch;
    EditText mEditTextSearch;
    RecyclerView mSearchResultsRV;
    ProgressBar mLoadingIndicatorPB;
    TextView mLoadingErrorMessageTV;
    BeerSearchAdapter mBeerSearchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_search_activity);

        mButtonSearch = (Button) findViewById(R.id.btn_search);
        mEditTextSearch = (EditText) findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView) findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView) findViewById(R.id.rv_search_results);
        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mBeerSearchAdapter = new BeerSearchAdapter(this);
        mSearchResultsRV.setAdapter(mBeerSearchAdapter);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBrewerySearch(mEditTextSearch.getText().toString());
                //TODO implement *_* in search
                //search database for value
            }
        });
    }

    private void doBrewerySearch(String searchQuery)
    {
        StringBuilder searchQueryBuilder = new StringBuilder(searchQuery);
        searchQueryBuilder.insert(0, '*');
        searchQueryBuilder.append('*');
        String beerSearchURL = BreweryDBUtils.buildBeerSearchURL(searchQueryBuilder.toString());
        Log.d("MainActivity", "got search url: " + beerSearchURL);
        new BrewerySearchTask().execute(beerSearchURL);
    }

    @Override
    public void onBeerItemClick(BreweryDBUtils.beerDetail beerDetail) {
        Intent intent = new Intent(this, BeerDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM, beerDetail);
        startActivity(intent);
    }

    public class BrewerySearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String beerSearchURL = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(beerSearchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
            if (s != null) {
                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                mSearchResultsRV.setVisibility(View.VISIBLE);
                ArrayList<BreweryDBUtils.beerDetail> searchResultsList = BreweryDBUtils.parseBeerSearchJSON(s);
                mBeerSearchAdapter.updateSearchResults(searchResultsList);
            } else {
                mSearchResultsRV.setVisibility(View.INVISIBLE);
                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
            }
        }
    }
}
