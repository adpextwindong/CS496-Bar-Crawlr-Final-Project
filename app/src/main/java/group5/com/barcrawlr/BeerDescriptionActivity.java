package group5.com.barcrawlr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import group5.com.barcrawlr.utils.BreweryDBUtils;

/**
 * Created by aidan on 6/12/17.
 */

public class BeerDescriptionActivity extends AppCompatActivity {

    TextView mBeerNameTV;
    TextView mBeerDescriptionTV;
    TextView mBeerABVTV;
    TextView mBeerShortName;

    BreweryDBUtils.beerDetail beerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_description);

        mBeerNameTV = (TextView) findViewById(R.id.tv_beer_name);
        mBeerDescriptionTV = (TextView) findViewById(R.id.tv_beer_description);
        mBeerABVTV = (TextView) findViewById(R.id.tv_beer_abv);
        mBeerShortName = (TextView) findViewById(R.id.tv_beer_shortname);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM )) {
            beerItem = (BreweryDBUtils.beerDetail)intent.getSerializableExtra(
                    BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM
            );
        }

        mBeerNameTV.setText(beerItem.beerName);
        mBeerDescriptionTV.setText(beerItem.description);
        mBeerABVTV.setText(beerItem.abv);
        mBeerShortName.setText(beerItem.style);
    }
}
