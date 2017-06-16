package group5.com.barcrawlr;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.FavoritesContract;
import group5.com.barcrawlr.utils.FavoritesDBHelper;

/**
 * Created by aidan on 6/12/17.
 */

public class BeerDescriptionActivity extends AppCompatActivity {

    TextView mBeerNameTV;
    BreweryDBUtils.beerDetail beerItem;
    ImageView mSearchResultBookmarkIV;
    SQLiteDatabase mDB;

    boolean mIsBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_description);

        mSearchResultBookmarkIV = (ImageView)findViewById(R.id.iv_beer_search_result_bookmark);
        mBeerNameTV = (TextView) findViewById(R.id.tv_beer_name);

        FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM )) {
            beerItem = (BreweryDBUtils.beerDetail)intent.getSerializableExtra(
                    BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM
            );

            mIsBookmarked = checkSearchResultIsInDB();
            updateBookmarkIconState();
        }

        mBeerNameTV.setText(beerItem.beerName);
        mSearchResultBookmarkIV.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mIsBookmarked = !mIsBookmarked;
                updateSearchResultInDB();
                updateBookmarkIconState();
            }
        });
    }

    private void updateSearchResultInDB() {
        if (mIsBookmarked) {
            addSearchResultToDB();
        } else {
            deleteSearchResultFromDB();
        }
    }

    private void deleteSearchResultFromDB() {
        if (beerItem != null) {
            String sqlSelection = FavoritesContract.FavoriteBeers.COLUMN_BEER_NAME+ " = ?";
            String[] sqlSelectionArgs = { beerItem.beerName };
            mDB.delete(FavoritesContract.FavoriteBeers.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }
    }

    private long addSearchResultToDB() {
        if (beerItem != null) {
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoriteBeers.COLUMN_BEER_NAME, beerItem.beerName);
            values.put(FavoritesContract.FavoriteBeers.COLUMN_ABV, beerItem.abv);
            values.put(FavoritesContract.FavoriteBeers.COLUMN_DESCRIPTION, beerItem.description);
            values.put(FavoritesContract.FavoriteBeers.COLUMN_IMAGE_URL, beerItem.imageUrl);
            values.put(FavoritesContract.FavoriteBeers.COLUMN_STYLE, beerItem.style);

            return mDB.insert(FavoritesContract.FavoriteBeers.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }

    private void updateBookmarkIconState() {
        if (mIsBookmarked) {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_black_48dp);
        } else {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_border_black_48dp);
        }
    }

    private boolean checkSearchResultIsInDB() {
        boolean isInDB = false;
        if (beerItem != null) {
            String sqlSelection = FavoritesContract.FavoriteBeers.COLUMN_BEER_NAME + " = ?";
            String[] sqlSelectionArgs = { beerItem.beerName };
            Cursor cursor = mDB.query(
                    FavoritesContract.FavoriteBeers.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isInDB = cursor.getCount() > 0;
            cursor.close();
        }
        return isInDB;
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }
}
