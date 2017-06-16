package group5.com.barcrawlr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import group5.com.barcrawlr.utils.BreweryDBUtils;

/**
 * Created by aidan on 6/12/17.
 */

public class BarDescriptionActivity extends AppCompatActivity {
    TextView mBarNameTV;
    TextView mBarDescriptionTV;
    TextView mBarCharacteristicsTV;
    TextView mBarWebsiteTV;
    TextView mBarOrganicTV;

    ImageView mBarImageIV;

    BreweryDBUtils.barDetail barItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_description);

        mBarNameTV = (TextView) findViewById(R.id.tv_bar_name);
        mBarDescriptionTV = (TextView) findViewById(R.id.tv_bar_description);
        mBarCharacteristicsTV = (TextView) findViewById(R.id.tv_bar_established);
        mBarOrganicTV = (TextView) findViewById(R.id.tv_bar_organic);
        mBarWebsiteTV = (TextView) findViewById(R.id.tv_bar_website);

        mBarImageIV = (ImageView) findViewById(R.id.iv_bar_image);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BreweryDBUtils.barDetail.EXTRA_BAR_ITEM )) {
            barItem = (BreweryDBUtils.barDetail)intent.getSerializableExtra(
                    BreweryDBUtils.barDetail.EXTRA_BAR_ITEM
            );
        }

        mBarNameTV.setText(barItem.barName);
        mBarDescriptionTV.setText(barItem.description);
        mBarCharacteristicsTV.setText(barItem.established);
        mBarWebsiteTV.setText(barItem.websiteUrl);
        mBarOrganicTV.setText(barItem.isOrganic);

        /*mBarImageIV.setText(barItem.imageUrl); Need to download image*/
    }
}
