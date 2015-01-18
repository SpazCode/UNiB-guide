package com.stuartsullivan.unibwikiguide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        // Adding Moves to table
        TableLayout movesTable = (TableLayout)findViewById(R.id.MoveBreakdownTable);

        // Iterating through move properties
        int i = 0;
        for(i = 0; i < chara.moveList.length; i++)
        {
            // Create View objects
            TableRow row = new TableRow(this);
            TextView input = new TextView(this);
            TextView dmg = new TextView(this);
            TextView act = new TextView(this);
            TextView str = new TextView(this);
            TextView rec = new TextView(this);
            TextView adv = new TextView(this);
            TextView blc = new TextView(this);

            // Set Text to the view
            input.setText(chara.moveList[i].name);
            input.setTextColor(Color.WHITE);
            dmg.setText("" + chara.moveList[i].damage);
            dmg.setTextColor(Color.WHITE);
            act.setText(chara.moveList[i].active_frames);
            act.setTextColor(Color.WHITE);
            str.setText(chara.moveList[i].startup_frames);
            str.setTextColor(Color.WHITE);
            rec.setText(chara.moveList[i].recovery_frames);
            rec.setTextColor(Color.WHITE);
            adv.setText(chara.moveList[i].advantage_frames);
            adv.setTextColor(Color.WHITE);
            blc.setText(chara.moveList[i].block_type);
            blc.setTextColor(Color.WHITE);

            // Add views
            row.addView(input);
            row.addView(dmg);
            row.addView(str);
            row.addView(act);
            row.addView(adv);
            row.addView(rec);
            row.addView(blc);

            movesTable.addView(row);
        }

        Log.i(TAG, "Combo Details...");
        // Adding Combo to table
        TableLayout combosTable = (TableLayout)findViewById(R.id.ComboBreakdownTable);

        // Iterating through combo properties
        i = 0;
        for(i = 0; i < chara.comboList.length; i++)
        {
            // Create View objects
            TableRow row = new TableRow(this);
            TextView input = new TextView(this);

            // Set Text to the view
            input.setText(chara.comboList[i].sequence);
            input.setTextColor(Color.WHITE);

            // Add views
            row.addView(input);
            combosTable.addView(row);
        }

        Log.i(TAG, "Setup Complete");
    }
}
