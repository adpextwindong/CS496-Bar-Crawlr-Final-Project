package group5.com.barcrawlr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by georgecrary on 6/4/17.
 */

public class BeerSearchActivity extends AppCompatActivity {

    Button mButtonSearch;
    EditText mEditTextSearch;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_search_activity);

        mButtonSearch = (Button) findViewById(R.id.btn_search);
        mEditTextSearch = (EditText) findViewById(R.id.et_search_box);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("yo");
                System.out.println(mEditTextSearch.getText());

                //search database for value
            }
        });
    }
}
