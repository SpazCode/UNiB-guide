package com.stuartsullivan.unibwikiguide;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

public class CharacterSelect extends ActionBarActivity {
    // DEBUG TAGS
    private static final String TAG = "APP-DEBUG";
    // Widgets set ups
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG, "onCreate");

        // Grab the grid
        grid = (GridView) findViewById(R.id.CharactersGrid);
        // Set adapter
        grid.setAdapter(new ImageAdapter(this));

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
