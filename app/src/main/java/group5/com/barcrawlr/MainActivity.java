package group5.com.barcrawlr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mButtonSearchByBeer;
    Button mButtonSearchByLocation;
    Button mButtonSearchEvent;

    Intent mBeerSearchIntent;
    Intent mBarSearchIntent;
    Intent mEventSearchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSearchByBeer = (Button) findViewById(R.id.FindByBeer);
        mButtonSearchByLocation = (Button) findViewById(R.id.FindByLocation);
        mButtonSearchEvent = (Button) findViewById(R.id.FindByEvent);

        mBeerSearchIntent = new Intent(this, BeerSearchActivity.class);
        mBarSearchIntent = new Intent(this, BarSearchActivity.class);
        mEventSearchIntent = new Intent(this, EventSearchActivity.class);

        mButtonSearchByBeer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(mBeerSearchIntent);
            }
        });

        mButtonSearchByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mBarSearchIntent);
            }
        });

        mButtonSearchEvent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(mEventSearchIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_fav_bars:
                viewFavBars();
                return true;
            case R.id.action_view_fav_beers:
                viewFavBeers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void viewFavBeers() {
        Intent favBeersIntent = new Intent(this, FavoriteBeer.class);
        startActivity(favBeersIntent);
    }

    private void viewFavBars() {
        Intent favBarsIntent = new Intent(this, FavoriteBars.class);
        startActivity(favBarsIntent);
    }

}
