package group5.com.barcrawlr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import group5.com.barcrawlr.utils.BreweryDBUtils;

/**
 * Created by aidan on 6/12/17.
 */

public class BarDescriptionActivity extends AppCompatActivity {
    TextView mBarNameTV;
    BreweryDBUtils.barDetail barItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_description);

        mBarNameTV = (TextView) findViewById(R.id.tv_bar_name);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BreweryDBUtils.barDetail.EXTRA_BAR_ITEM )) {
            barItem = (BreweryDBUtils.barDetail)intent.getSerializableExtra(
                    BreweryDBUtils.barDetail.EXTRA_BAR_ITEM
            );
        }

        mBarNameTV.setText(barItem.barName);
    }
}
