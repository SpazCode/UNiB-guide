package com.stuartsullivan.unibwikiguide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.util.List;


public class CharacterView extends ActionBarActivity {
    // Log Constants
    private static final String TAG = "APP-DEBUG";
    // Character ID
    private int _id = -1;
    // Character Object
    CharacterObject chara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Get passed data
        Intent i = getIntent();
        _id = i.getIntExtra("id", -1);
        // Set up the current character
        chara = new CharacterObject(_id, this);
        chara.load();
        setTables();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTables() {
        Log.i(TAG, "Setting up tables...");
        // Text View
        TextView name = (TextView)findViewById(R.id.CharacterHeader);
        TextView health = (TextView)findViewById(R.id.CharacterHealth);
        TextView trait = (TextView)findViewById(R.id.CharacterTrait);
        Log.i(TAG, "Character Details...");
        // Set the Character Details
        name.setText(" " + chara.name.trim());
        health.setText(" " + chara.health + "");
        trait.setText(" " + chara.trait.trim());

        Log.i(TAG, "Move Details...");

        // Set data for the moves
        ListView moveList = (ListView) findViewById(R.id.move_breakdown_table);
        MoveListAdapter adapter = new MoveListAdapter(this, chara.moveList);
        moveList.setAdapter(adapter);

        /* Log.i(TAG, "Combo Details...");
        // Adding Combo to table
        ListView combosTable = (ListView)findViewById(R.id.combo_breakdown_table);
        */
        Log.i(TAG, "Setup Complete");
    }
}
