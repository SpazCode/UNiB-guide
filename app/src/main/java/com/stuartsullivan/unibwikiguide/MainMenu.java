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
    private static final String[] FILENAMES = { "properties", "Hyde", "Linne", "Waldstein",
                                                "Carmine", "Orie", "Gordeau", "Merkava",
                                                "Vatista", "Seth", "Yuzuriha", "Hilda", "Eltnum",
                                                "Chaos", "Akatsuki", "Nanase", "Byakuya" };
    private static final String FILEBASEURL = "http://stuartsullivan.com//uniel/";
    // private static final String FILENAME = "guide.json";
    // private static final String FILEURL = "http://stuartsullivan.com/guide.json";
    // JSON KEYS
    private static final String CHARACTERS = "characters";
    private static final String CHARANAME = "name";
    private static final String CHARAIMAGE = "portrait";
    private static final String CHARAHEALTH = "health";
    private static final String CHARATRAITS = "trait";
    private static final String MOVES = "moves";
    private static final String MOVEDATA = "data";
    private static final String MOVENAME = "name";
    private static final String MOVETYPE = "type";
    private static final String MOVEINPUT = "input";
    private static final String MOVEDMG = "damage";
    private static final String MOVESUP = "startup";
    private static final String MOVEACT = "active";
    private static final String MOVEREC = "recovery";
    private static final String MOVEADV = "frameAdv";
    private static final String MOVEBLK = "guard";
    private static final String MOVECAN = "cancel";
    private static final String MOVEDES = "description";
    private static final String MOVEVER = "version";
    private static final String COMBOS = "combos";
    private static final String COMBOTYPE = "type";
    private static final String COMBOSEQ = "sequence";
    private static final String BLOCKTYPES = "blocks";
    private static final String CANCELTYPES = "cancels";
    private static final String MOVETYPES= "movetype";
    private static final String COMBOTYPES = "combotype";
    private static final String TYPEID = "id";
    private static final String TYPENAME = "name";
    private static final String TYPEVALUE = "value";
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
        // If the database has not been set up yet run the async tasks to put it together
        if(!adapter.checkUpdated()) {
            InitializeDatabase iTask = new InitializeDatabase(this);
            iTask.execute();
        } else {
            dialog.hide();
        }
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
            Log.i(TAG, "MainMenu - Settings");
            return true;
        }

        if (id == R.id.action_update) {
            // Set up the Menu button for updates
            Log.i(TAG, "MainMenu - Sync");
            runUpdate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Function that will run the updates
    private void runUpdate() {
        // Set up the dialog
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Updating Guide Data");
        dialog.show();
        // Run the async task
        InitializeDatabase iTask = new InitializeDatabase(this);
        iTask.execute();
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
            String moveInput = "";
            String moveDmg = "";
            String moveSup = "";
            String moveAct = "";
            String moveRec = "";
            String moveAdv = "";
            String moveCan = "";
            String moveBlk = "";
            String moveVer = "";
            String moveDes = "";
            String comboType = "";
            String comboSeq = "";
            int typeId = -1;
            String typeName = "";
            String typeValue = "";

            // Download all the different JSON files
            for (String chara:FILENAMES) {
                try {
                // Download the JSON
                    URL url = new URL(FILEBASEURL + chara + ".json");
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
                    output = openFileOutput(chara + ".json", Context.MODE_PRIVATE);

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
                            publishProgress("Saving File - " + chara + ".json : " + total + "\\" + fileLength);
                        output.write(data, 0, count);
                    }
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
            }

            // dialog.setMessage("Updating Guide Data");
            // initalize the adapters
            context.deleteDatabase(DATABASE_NAME);
            // Loop through each file
            for (String chara:FILENAMES) {
                // open the DB adapter
                adapter.open();
                // Load in the file for each character
                try {
                    fis = openFileInput(chara + ".json");
                    byte[] dataArray = new byte[fis.available()];
                    collected = "";
                    while (fis.read(dataArray) != -1) {
                        collected += new String(dataArray);
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
                Log.i(TAG, "" + collected.length());

                // If the JSON is present
                if (collected != null) {
                    // Load the JSON into an object
                    JSONObject json;
                    collected.replace("null", "");
                    try {
                        json = new JSONObject(collected.trim());

                        int i = 0;
                        int x = 0;
                        int n = 0;

                        // try to get move type data for the JSON
                        if(json.has(MOVETYPES)) {
                            // DEBUG
                            Log.i(TAG, "Insert Move Types");
                            for (i = 0; i < json.getJSONArray(MOVETYPES).length(); i++) {
                                // Copy values
                                typeId = json.getJSONArray(MOVETYPES).getJSONObject(i).getInt(TYPEID);
                                typeName = json.getJSONArray(MOVETYPES).getJSONObject(i).getString(TYPENAME);
                                // insert into entry
                                adapter.createMoveTypeEntry(typeId, typeName);
                                // DEBUG
                                Log.i(TAG, "Insert - " + typeName);
                            }
                        }

                        // try to get block type data from JSON
                        if(json.has(BLOCKTYPES)) {
                            // DEBUG
                            Log.i(TAG, "Insert Block Types");
                            for (i = 0; i < json.getJSONArray(BLOCKTYPES).length(); i++) {
                                // Copy values
                                typeId = json.getJSONArray(BLOCKTYPES).getJSONObject(i).getInt(TYPEID);
                                typeName = json.getJSONArray(BLOCKTYPES).getJSONObject(i).getString(TYPENAME);
                                typeValue = json.getJSONArray(BLOCKTYPES).getJSONObject(i).getString(TYPEVALUE);
                                // insert into entry
                                adapter.createBlockTypeEntry(typeId, typeName, typeValue);
                                // DEBUG
                                Log.i(TAG, "Insert - " + typeName);
                            }
                        }

                        // Try to get cancel type data from JSON
                        if(json.has(CANCELTYPES)) {
                            // DEBUG
                            Log.i(TAG, "Insert Cancel Types");
                            for (i = 0; i < json.getJSONArray(CANCELTYPES).length(); i++) {
                                // Copy values
                                typeId = json.getJSONArray(CANCELTYPES).getJSONObject(i).getInt(TYPEID);
                                typeName = json.getJSONArray(CANCELTYPES).getJSONObject(i).getString(TYPENAME);
                                typeValue = json.getJSONArray(CANCELTYPES).getJSONObject(i).getString(TYPEVALUE);
                                // insert into entry
                                adapter.createCancelTypeEntry(typeId, typeName, typeValue);
                                // DEBUG
                                Log.i(TAG, "Insert - " + typeName);
                            }
                        }
                        // Try to get combo type data from the JSON
                        if (json.has(COMBOTYPES)) {
                            // DEBUG
                            Log.i(TAG, "Insert Combo Types");
                            for (i = 0; i < json.getJSONArray(COMBOTYPES).length(); i++) {
                                // Copy values
                                typeId = json.getJSONArray(COMBOTYPES).getJSONObject(i).getInt(TYPEID);
                                typeName = json.getJSONArray(COMBOTYPES).getJSONObject(i).getString(TYPENAME);
                                // insert into entry
                                adapter.createComboTypeEntry(typeId, typeName);
                                // DEBUG
                                Log.i(TAG, "Insert - " + typeName);
                            }
                        }
                        // try to get character data from the JSON
                        if (json.has(CHARANAME)) {
                            // Copy all the values
                            charaName = json.getString(CHARANAME);
                            charaImage = null; // json.getString(CHARAIMAGE);
                            health = Integer.parseInt(json.getString(CHARAHEALTH).replace(",", ""));
                            trait = json.getString(CHARATRAITS);
                            // Insert the entry
                            adapter.createCharacterEntry(charaName, charaImage, health, trait);
                            // DEBUG
                            Log.i(TAG, "Insert - " + charaName);
                            // Get the character id
                            charaId = adapter.getCharacterId(charaName);
                            if (json.has(MOVES)) {
                                for (x = 0; x < json.getJSONArray(MOVES).length(); x++) {
                                    // Copy all the move data
                                    moveName = json.getJSONArray(MOVES).getJSONObject(x).getString(MOVENAME);
                                    moveType = json.getJSONArray(MOVES).getJSONObject(x).getString(MOVETYPE);
                                    moveInput = json.getJSONArray(MOVES).getJSONObject(x).getString(MOVEINPUT);
                                    moveInput = moveInput != "null" ? moveInput : moveName;
                                    // Insert the entry
                                    adapter.createMoveEntry(moveName, charaId, adapter.getMoveType(moveType), moveInput);
                                    for (n = 0; n < json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).length(); n++) {
                                        moveDmg = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEDMG);
                                        moveSup = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVESUP);
                                        moveAct = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEACT);
                                        moveRec = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEREC);
                                        moveAdv = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEADV);
                                        moveBlk = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEBLK);
                                        moveVer = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEVER);
                                        moveCan = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVECAN);
                                        moveDes = json.getJSONArray(MOVES).getJSONObject(x).getJSONArray(MOVEDATA).getJSONObject(n).getString(MOVEDES);

                                        adapter.createMoveDataEntry(x + 1, moveVer, moveDmg, moveSup, moveAct, moveRec, moveAdv, moveCan, moveDes, moveBlk);
                                    }
                                }
                            }

                            if (json.has(COMBOS)) {
                                for (x = 0; x < json.getJSONArray(COMBOS).length(); x++) {
                                    // Copy all the move data
                                    comboType = json.getJSONArray(COMBOS).getJSONObject(x).getString(COMBOTYPE);
                                    comboSeq = json.getJSONArray(COMBOS).getJSONObject(x).getString(COMBOSEQ);
                                    // Insert the entry
                                    adapter.createComboEntry(charaId, comboType, comboSeq);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        Log.i(TAG, "JSON Parser Error - " + e.getMessage());
                    } finally {
                        adapter.updateDateUpdated();
                        adapter.close();
                    }
                } else {
                    return null;
                }
            }
            return null;
        }

        @Override
        // When the progress is updated
        protected void onProgressUpdate(String... progress){
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
