package com.stuartsullivan.unibwikiguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainMenu extends ActionBarActivity {
    // Log Constants
    private static final String TAG = "APP-DEBUG";
    // Json Constants
    private static final String FILENAME = "guide.json";
    private static final String FILEURL = "http://stuartsullivan.com/guide.json";
    // JSON KEYS
    private static final String CHARACTERS = "Characters";
    private static final String CHARANAME = "Name";
    private static final String CHARAIMAGE = "Portrait";
    private static final String CHARAHEALTH = "Health";
    private static final String CHARATRAITS = "Traits";
    private static final String MOVES = "Moves";
    private static final String MOVENAME = "Name";
    private static final String MOVETYPE = "Type";
    private static final String MOVEDMG = "Damage";
    private static final String MOVESUP = "Startup";
    private static final String MOVEACT = "Active";
    private static final String MOVEREC = "Recovery";
    private static final String MOVEADV = "Advantage";
    private static final String MOVEBLK = "Block";
    private static final String COMBOS = "Combos";
    private static final String COMBOTYPE = "Type";
    private static final String COMBOSEQ = "Sequence";
    private static final String MOVETYPES= "Move Type";
    private static final String COMBOTYPES = "Combo Type";
    private static final String TYPEID = "id";
    private static final String TYPENAME = "Name";
    // DATABASE VARIABLES
    private static final String DATABASE_NAME = "UNiBGuide";
    // Progress dialog to keep us updated
    public ProgressDialog dialog;
    // Main Menu buttons
    private Button menuButtonSystems;
    private Button menuButtonPatches;
    private Button menuButtonCharacters;
    // Database adapters
    DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Log.i(TAG, "MainMenu - onCreate");

        // Setup dialog
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Updating Guide Data");
        dialog.show();

        // Set up all the buttons
        menuButtonSystems = (Button) findViewById(R.id.MainMenuSystems);
        menuButtonPatches = (Button) findViewById(R.id.MainMenuPatches);
        menuButtonCharacters = (Button) findViewById(R.id.MainMenuCharacters);

        // Set up the adapters
        adapter = new DatabaseAdapter(this);
        adapter.open();
        adapter.close();

        // Set up the activity listeners
        menuButtonSystems.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "MainMenu - menuButtonSystems - onClick");
            }
        });

        menuButtonPatches.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "MainMenu - menuButtonPatches - onClick");
            }
        });

        menuButtonCharacters.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "MainMenu - menuButtonCharacters - onClick");
                Intent i = new Intent(getApplicationContext(), CharacterSelect.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "MainMenu - onStart");
        InitializeDatabase iTask = new InitializeDatabase(this);
        iTask.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i(TAG, "MainMenu - onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i(TAG, "MainMenu - onOptionsItemSelected");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class InitializeDatabase extends AsyncTask<Void, String, Void>
    {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public InitializeDatabase() {
            this.context = null;
            this.mWakeLock = null;
        }

        public InitializeDatabase(Context _context) {
            this.context = _context;
        }

        // The first thing that runs in asynctask
        protected void onPreExecute() {
            // Say State in logs
            Log.i(TAG, "InitializeDatabase - onPreExecute");
            // Set up the power manager
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            // Set up the progress dialog
            dialog.setMessage("Updating Guide Data");
            dialog.show();
        }

        @Override
        // The background process running
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "InitializeDatabase - doInBackground");

            // Set up some variables
            InputStream input = null;
            FileOutputStream output = null;
            HttpURLConnection connection = null;
            // File f = new File(context.getFilesDir().getAbsolutePath() + "/" + params[1]);
            String path = "";
            HttpURLConnection conn;
            // File input variables
            FileInputStream fis = null;
            String collected = null;
            // Strings for creating the new entries
            int charaId = -1;
            String charaName = "";
            String charaImage = "";
            int health = -1;
            String trait = "";
            String moveName = "";
            String moveType = "";
            int moveDmg = -1;
            int moveSup = -1;
            int moveAct = -1;
            int moveRec = -1;
            int moveAdv = -1;
            String moveBlk = "";
            String comboType = "";
            String comboSeq = "";
            int moveTypeId = -1;
            String moveTypeName = "";
            int comboTypeId = -1;
            String comboTypeName = "";



            try {
                // Download the JSON
                URL url = new URL(FILEURL);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                // If there is an issue with grabbing the file
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "Connect Failure - " + conn.getResponseCode());
                    return null;
                } else {
                    Log.i(TAG, "Connect Successful - " + conn.getResponseCode());
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = conn.getContentLength();

                // Gets the input stream
                input = conn.getInputStream();
                output = openFileOutput(FILENAME, Context.MODE_PRIVATE);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow this to be canceled
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // Publish the progress...
                    if (fileLength > 0)
                        publishProgress("Downloading Data - " + total  + "\\" + fileLength);
                    output.write(data, 0, count);
                }

                dialog.setMessage("Updating Guide Data");

            } catch (Exception e) {
                // output to log
                Log.i(TAG, "Download Failed due to, " + e.getMessage());
                // return a null object
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (Exception e) {
                    Log.i(TAG, "Error - " + e.getMessage());
                }

                if (connection != null)
                    connection.disconnect();
            }

            // Load in the file
            try {
                fis = openFileInput(FILENAME);
                byte[] dataArray = new byte[fis.available()];
                while (fis.read(dataArray) != -1) {
                    collected = new String(dataArray);
                    publishProgress("Updating...");
                }
            } catch (Exception e) {
                Log.i(TAG, "Parse Error - " + e.getMessage());
            } finally {
                // Close the file loaders at the end
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException e) {
                        Log.i(TAG, "Close - " + e.getMessage());
                    }
            }
            Log.i(TAG, collected);

            // If the JSON is present
            if (collected != null) {
                // Load the JSON into an object
                JSONObject json;
                try {
                    json = new JSONObject(collected);
                    int i = 0;
                    int x = 0;
                    context.deleteDatabase(DATABASE_NAME);
                    adapter.open();
                    for(i = 0; i < json.getJSONArray(MOVETYPES).length(); i++) {
                        // Copy values
                        moveTypeId = json.getJSONArray(MOVETYPES).getJSONObject(i).getInt(TYPEID);
                        moveTypeName = json.getJSONArray(MOVETYPES).getJSONObject(i).getString(TYPENAME);
                        // insert into entry
                        adapter.createMoveTypeEntry(moveTypeId, moveTypeName);
                        // DEBUG
                        Log.i(TAG, "Insert - " + moveTypeName);
                    }
                    for(i = 0; i < json.getJSONArray(COMBOTYPES).length(); i++) {
                        // Copy values
                        comboTypeId = json.getJSONArray(COMBOTYPES).getJSONObject(i).getInt(TYPEID);
                        comboTypeName = json.getJSONArray(COMBOTYPES).getJSONObject(i).getString(TYPENAME);
                        // insert into entry
                        adapter.createComboTypeEntry(comboTypeId, comboTypeName);
                        // DEBUG
                        Log.i(TAG, "Insert - " + moveTypeName);
                    }
                    for(i = 0; i < json.getJSONArray(CHARACTERS).length(); i++) {
                        // Copy all the values
                        charaName = json.getJSONArray(CHARACTERS).getJSONObject(i).getString(CHARANAME);
                        charaImage = json.getJSONArray(CHARACTERS).getJSONObject(i).getString(CHARAIMAGE);
                        health = json.getJSONArray(CHARACTERS).getJSONObject(i).getInt(CHARAHEALTH);
                        trait = json.getJSONArray(CHARACTERS).getJSONObject(i).getString(CHARATRAITS);
                        // Insert the entry
                        adapter.createCharacterEntry(charaName, charaImage, health, trait);
                        // DEBUG
                        Log.i(TAG, "Insert - " + charaName);
                        // Get the character id
                        charaId = adapter.getCharacterId(charaName);
                        for(x = 0; x < json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).length(); x++) {
                            // Copy all the move data
                            moveName = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getString(MOVENAME);
                            moveType = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getString(MOVETYPE);
                            moveDmg = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getInt(MOVEDMG);
                            moveSup = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getInt(MOVESUP);
                            moveAct = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getInt(MOVEACT);
                            moveRec = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getInt(MOVEREC);
                            moveAdv = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getInt(MOVEADV);
                            moveBlk = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(MOVES).getJSONObject(x).getString(MOVEBLK);
                            // Insert the entry
                            adapter.createMoveEntry(moveName, charaId, moveType, moveDmg, ""+moveSup, ""+moveAct, ""+moveRec, ""+moveAdv, moveBlk);
                        }
                        for(x = 0; x < json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(COMBOS).length(); x++) {
                            // Copy all the move data
                            comboType = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(COMBOS).getJSONObject(x).getString(COMBOTYPE);
                            comboSeq = json.getJSONArray(CHARACTERS).getJSONObject(i).getJSONArray(COMBOS).getJSONObject(x).getString(COMBOSEQ);
                            // Insert the entry
                            adapter.createComboEntry(charaId, comboType, comboSeq);
                        }
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "JSON Parser Error - " + e.getMessage());
                } finally {
                    adapter.close();
                }

                return null;
            } else {
                return null;
            }
        }

        @Override
        // When the progress is updated
        protected void onProgressUpdate(String...progress){
            Log.i(TAG, "InitializeDatabase - onProgressUpdate");
            dialog.setMessage(progress[0]);
        }

        @Override
        // After the task is complete
        protected void onPostExecute(Void resultes) {
            Log.i(TAG, "InitializeDatabase - onPostExecute");
            if(dialog != null) {
                dialog.cancel();
                dialog.hide();
            }
        }
    }
}
