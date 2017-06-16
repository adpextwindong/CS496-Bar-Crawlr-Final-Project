package group5.com.barcrawlr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

import group5.com.barcrawlr.utils.NetworkUtils;

import static group5.com.barcrawlr.utils.BreweryDBUtils.buildBarSearchURL;
import static group5.com.barcrawlr.utils.BreweryDBUtils.parseRandomLocalBarForPanicPint;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    Button mButtonSearchByBeer;
    Button mButtonSearchByLocation;
    Button mButtonSearchEvent;
    Button mButtonPanicPint;

    Intent mBeerSearchIntent;
    Intent mBarSearchIntent;
    Intent mEventSearchIntent;

    Toast mInvalidZipToast = null;


    LocationManager mLocationManager;
    MainActivity mContextForPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSearchByBeer = (Button) findViewById(R.id.FindByBeer);
        mButtonSearchByLocation = (Button) findViewById(R.id.FindByLocation);
        mButtonSearchEvent = (Button) findViewById(R.id.FindByEvent);
        mButtonPanicPint = (Button) findViewById(R.id.PanicPint);

        mBeerSearchIntent = new Intent(this, BeerSearchActivity.class);
        mBarSearchIntent = new Intent(this, BarSearchActivity.class);
        mEventSearchIntent = new Intent(this, EventSearchActivity.class);

        //Get postal code from location and geoIntent map directions to the closest bar

        mContextForPermissions = this;
        mInvalidZipToast = Toast.makeText(this, "Invalid Zip Code. Has to be 5 digits long", Toast.LENGTH_SHORT);
        //mPanicPintIntent = new Intent(this, PanicPintActivity.class);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mButtonSearchByBeer.setOnClickListener(new View.OnClickListener() {
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

        mButtonSearchEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(mEventSearchIntent);
            }
        });


        mButtonPanicPint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContextForPermissions);

                String zipString = mSharedPreferences.getString(getString(R.string.pref_zip_key),"97330");

                Integer intParse = null;
                try{
                    intParse = Integer.parseInt(zipString);
                }catch(java.lang.NumberFormatException e){
                    intParse = null;
                }
                if(intParse != null && zipString.length() == 5){
                    final String URL = buildBarSearchURL(zipString, "zip");


                    class PanicTask extends AsyncTask<Void, Integer, String>{

                        @Override
                        protected String doInBackground(Void... params) {
                            InputStream is = getResources().openRawResource(R.raw.panicpints);
                            Writer writer = new StringWriter();
                            char[] buffer = new char[1024];
                            try {
                                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                                int n;
                                while ((n = reader.read(buffer)) != -1) {
                                    writer.write(buffer, 0, n);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            String json = writer.toString();
                            String latlong;
                            latlong = parseRandomLocalBarForPanicPint(json);

                            String geoIntentURI = "google.navigation:q="+latlong;
                            Log.d(TAG, "doInBackground: GEO INTENT URI FOR PANIC PINT IS " + geoIntentURI);
                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+latlong);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);

                            return null;
                        }
                    }
                    new PanicTask().execute();
                }else{
                    mInvalidZipToast.show();
                }
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
