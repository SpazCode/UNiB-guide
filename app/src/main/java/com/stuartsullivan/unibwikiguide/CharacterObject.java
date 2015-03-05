package com.stuartsullivan.unibwikiguide;

import android.content.Context;
import android.util.Log;

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


        setupLists();

        adapter.open();

        // Set up variables
        int count = 0;

        // Get Character moves
        if (moveCount > 0) {
            String moves = adapter.getCharacterMoves(id);
            for (String move : moves.split("\n")) {
                moveList[count] = new Move();
                moveList[count].load(move);
                count++;
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

        // initalize the arraies
        moveList = new Move[moveCount];
        comboList = new Combo[comboCount];

        // close the DB
        adapter.close();
    }

    public static class Move {
        public String name;
        public String move_type;
        public String damage;
        public String active_frames;
        public String startup_frames;
        public String recovery_frames;
        public String advantage_frames;
        public String block_type;

        public Move() {
            name = "";
            move_type = "";
            damage = "";
            active_frames = "";
            startup_frames = "";
            recovery_frames = "";
            advantage_frames = "";
            block_type = "";
        }

        public Move(String _name, String _mt, String dmg, String _actf, String _strf, String _recf, String _advf, String _blkt) {
            name = _name;
            move_type = _mt;
            damage = dmg;
            active_frames = _actf;
            startup_frames = _strf;
            recovery_frames = _recf;
            advantage_frames = _advf;
            block_type = _blkt;
        }

        public void load(String move) {
            name = move.split(":")[0];
            move_type = move.split(":")[1];
            damage = move.split(":")[2];
            startup_frames = move.split(":")[3];
            active_frames = move.split(":")[4];
            recovery_frames = move.split(":")[5];
            advantage_frames = move.split(":")[6];
            block_type = move.split(":")[7];
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
