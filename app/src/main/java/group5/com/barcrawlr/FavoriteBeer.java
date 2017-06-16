package group5.com.barcrawlr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.FavoritesContract;
import group5.com.barcrawlr.utils.FavoritesDBHelper;

public class FavoriteBeer extends AppCompatActivity implements BeerSearchAdapter.OnBeerItemClickListener {

    private RecyclerView mSavedSearchResultsRV;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_beer);

        FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        ArrayList<BreweryDBUtils.beerDetail> favoriteBeersList = getAllSavedBeers();
        BeerSearchAdapter adapter = new BeerSearchAdapter(this);
        adapter.updateSearchResults(favoriteBeersList);

        mSavedSearchResultsRV = (RecyclerView)findViewById(R.id.rv_saved_beer_search_results);
        mSavedSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedSearchResultsRV.setHasFixedSize(true);
        mSavedSearchResultsRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    @Override
    public void onBeerItemClick(BreweryDBUtils.beerDetail beerDetail) {
        Intent intent = new Intent(this, BeerDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM, beerDetail);
        startActivity(intent);
    }

    public ArrayList<BreweryDBUtils.beerDetail> getAllSavedBeers() {
        Cursor cursor = mDB.query(
                FavoritesContract.FavoriteBeers.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoritesContract.FavoriteBeers.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<BreweryDBUtils.beerDetail> searchResultsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BreweryDBUtils.beerDetail result = new BreweryDBUtils.beerDetail();
            result.beerName = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBeers.COLUMN_BEER_NAME)
            );

            result.abv = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBeers.COLUMN_ABV)
            );

            result.description = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBeers.COLUMN_DESCRIPTION)
            );

            result.style = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBeers.COLUMN_STYLE)
            );

            result.imageUrl = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBeers.COLUMN_IMAGE_URL)
            );

            searchResultsList.add(result);
        }
        cursor.close();

        return searchResultsList;
    }
}
