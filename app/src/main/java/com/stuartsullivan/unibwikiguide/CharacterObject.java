package com.stuartsullivan.unibwikiguide;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Stuart on 2014-12-28.
 */
public class CharacterObject {
    // Log Constants
    private static final String TAG = "APP-DEBUG";
    // Character Variables
    public int id;
    public String name;
    public int health;
    public String trait;
    public Move[] moveList;
    public Combo[] comboList;
    private int moveCount;
    private int comboCount;
    private Context context;
    private DatabaseAdapter adapter;

    public CharacterObject(int _id, Context _context) {
        id = _id;
        context = _context;
        adapter = new DatabaseAdapter(context);
    }

    public boolean load() {
        // Open DB
        adapter.open();
        String info = adapter.getCharacterInfo(id);
        adapter.close();
        name = info.split(":")[0];
        health = Integer.parseInt(info.split(":")[1]);
        trait = info.split(":")[2];
        adapter.close();

        // initialize the objects
        setupLists();

        // Reopen the database
        adapter.open();

        // Set up variables
        int count = 0;

        // Get Character moves
        if (moveCount > 0) {
            JSONArray moves = adapter.getCharacterMoves(id);
            try {
                int length = moves.length();
                for (int i = 0; i < length; i++) {
                    moveList[i] = new Move();
                    moveList[i].load(moves.getJSONObject(i));
                }
            } catch (Exception ignore) {

            }

        }

        count = 0;
        // Get Character Combos
        if (comboCount > 0) {
            String combos = adapter.getCharacterCombos(id);
            for (String combo : combos.split("\n")) {
                comboList[count] = new Combo();
                comboList[count].load(combo);
                count++;
            }
        }

        // Close DB
        adapter.close();
        // DEBUG
        Log.i(TAG, "Character Loaded");
        return true;
    }

    private void setupLists() {
        // Open the database adapter
        adapter.open();
        // Get the counts
        moveCount = adapter.getCharacterMoveCount(id);
        comboCount = adapter.getCharacterComboCount(id);

        // initialize the arrays
        moveList = new Move[moveCount];
        comboList = new Combo[comboCount];

        // close the DB
        adapter.close();
    }

    public static class Move {
        // Parameters for moves
        public String name;
        public String input;
        public String move_type;
        public MoveData[] data;

        // ** CONSTRUCTOR **
        public Move() {
            name = "";
            input = "";
            move_type = "";
            data =  null;
        }

        // Load the move values and the data
        public void load(JSONObject move) {
            try {
                name = move.getString("name");
                input = move.getString("input");
                move_type = move.getString("move_type");
                int data_count = move.getJSONArray("data").length();
                data = new MoveData[data_count];
                for (int x = 0; x < data_count; x++) {
                    data[x] = new MoveData();
                    data[x].load(move.getJSONArray("data").getJSONObject(x));
                }
            } catch (Exception e) {
                blank();
            }
        }

        private void blank() {
            name = "";
            input = "";
            move_type = "";
        }
    }

    public static class MoveData{
        public String version;
        public String damage;
        public String active_frames;
        public String startup_frames;
        public String recovery_frames;
        public String advantage_frames;
        public String block_type;
        public String cancels;
        public String desc;

        // ** CONSTRUCTOR **
        public MoveData() {
            blank();
        }

        // Load the data from the json object
        public void load(JSONObject data) {
            try {
                version = data.getString("version");
                damage = data.getString("damage");
                startup_frames = data.getString("startup");
                active_frames = data.getString("active");
                recovery_frames = data.getString("recovery");
                advantage_frames = data.getString("advantage");
                block_type = data.getString("blocktype");
                cancels = data.getString("cancels");
                desc = data.getString("description");
            } catch (Exception e) {
                blank();
            }
        }

        // default all the values
        private void blank() {
            version = "";
            damage = "";
            active_frames = "";
            startup_frames = "";
            recovery_frames = "";
            advantage_frames = "";
            block_type = "";
            cancels = "";
            desc = "";
        }
    }

    public static class Combo {
        public int combo_type;
        public String sequence;

        public Combo() {
            combo_type = -1;
            sequence = "";
        }

        public Combo(int type, String seq) {
            combo_type = type;
            sequence = seq;
        }

        public void load(String combo) {
            combo_type = Integer.parseInt(combo.split(":")[0]);
            sequence = combo.split(":")[1];
        }
    }
}
