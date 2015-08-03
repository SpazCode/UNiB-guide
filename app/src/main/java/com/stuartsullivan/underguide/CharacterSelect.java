package com.stuartsullivan.underguide;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

public class CharacterSelect extends AppCompatActivity {
    // DEBUG TAGS
    private static final String TAG = "APP-DEBUG";
    // Widgets set ups
    private GridView gridView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
        // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG, "onCreate");

        // get Character view
        gridView = (GridView) findViewById(R.id.Characters);
        // Set adapter
        gridView.setAdapter(new ImageAdapter(this));
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character_select, menu);
        Log.i(TAG, "onCreateOptionMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCharacterSelect() {
        //
    }

}
