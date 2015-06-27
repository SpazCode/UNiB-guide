package com.stuartsullivan.unibwikiguide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stuartsullivan.unibwikiguide.CharacterObject.*;

/**
 * Created by stuart on 27/04/15.
 */
public class MoveListAdapter extends ArrayAdapter<CharacterObject.Move> {
    // Global Variables
    public final Context context;
    public final Move[] values;

    // Constructors
    public MoveListAdapter(Context context, Move[] values) {
        super(context, R.layout.listitem_move_single, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // The view is used for the row
        View rowView;
        // Get the current data for the row
        Move cur = (Move) values[position];
        if (cur.data.length > 1) {
            // Set row view for the multiple move data
            rowView = inflater.inflate(R.layout.listitem_move_multi, parent, false);
            // Set basic data
            TextView move_name = (TextView) rowView.findViewById(R.id.move_name);
            move_name.setText(cur.name);
            TextView move_input = (TextView) rowView.findViewById(R.id.move_input);
            move_input.setText(cur.input);

            // Loop through the different move data
            for(int i = 0; i < cur.data.length; i++)
            {
                // Get the current data
                MoveData data = cur.data[i];
                // Get the current version
                String ver = data.version;

                // Pick the right list
                if(ver.toLowerCase().contains("a")) {
                    TextView data_ver = (TextView) rowView.findViewById(R.id.move_data_version_a);
                    data_ver.setText(data.version);
                    TextView data_dmg = (TextView) rowView.findViewById(R.id.move_data_damage_a);
                    data_dmg.setText(data.damage);
                    TextView data_act = (TextView) rowView.findViewById(R.id.move_data_active_a);
                    data_act.setText(data.active_frames);
                    TextView data_str = (TextView) rowView.findViewById(R.id.move_data_startup_a);
                    data_str.setText(data.startup_frames);
                    TextView data_rec = (TextView) rowView.findViewById(R.id.move_data_recovery_a);
                    data_rec.setText(data.recovery_frames);
                    TextView data_adv = (TextView) rowView.findViewById(R.id.move_data_advantage_a);
                    data_adv.setText(data.advantage_frames);
                    TextView data_blk = (TextView) rowView.findViewById(R.id.move_data_blocks_a);
                    data_blk.setText(data.block_type);
                    TextView data_desc = (TextView) rowView.findViewById(R.id.move_data_desc_a);
                    data_desc.setText(data.desc);
                } else if(ver.toLowerCase().contains("b")) {
                    TextView data_ver = (TextView) rowView.findViewById(R.id.move_data_version_b);
                    data_ver.setText(data.version);
                    TextView data_dmg = (TextView) rowView.findViewById(R.id.move_data_damage_b);
                    data_dmg.setText(data.damage);
                    TextView data_act = (TextView) rowView.findViewById(R.id.move_data_active_b);
                    data_act.setText(data.active_frames);
                    TextView data_str = (TextView) rowView.findViewById(R.id.move_data_startup_b);
                    data_str.setText(data.startup_frames);
                    TextView data_rec = (TextView) rowView.findViewById(R.id.move_data_recovery_b);
                    data_rec.setText(data.recovery_frames);
                    TextView data_adv = (TextView) rowView.findViewById(R.id.move_data_advantage_b);
                    data_adv.setText(data.advantage_frames);
                    TextView data_blk = (TextView) rowView.findViewById(R.id.move_data_blocks_b);
                    data_blk.setText(data.block_type);
                    TextView data_desc = (TextView) rowView.findViewById(R.id.move_data_desc_b);
                    data_desc.setText(data.desc);
                } else {
                    TextView data_ver = (TextView) rowView.findViewById(R.id.move_data_version_ex);
                    data_ver.setText(data.version);
                    TextView data_dmg = (TextView) rowView.findViewById(R.id.move_data_damage_ex);
                    data_dmg.setText(data.damage);
                    TextView data_act = (TextView) rowView.findViewById(R.id.move_data_active_ex);
                    data_act.setText(data.active_frames);
                    TextView data_str = (TextView) rowView.findViewById(R.id.move_data_startup_ex);
                    data_str.setText(data.startup_frames);
                    TextView data_rec = (TextView) rowView.findViewById(R.id.move_data_recovery_ex);
                    data_rec.setText(data.recovery_frames);
                    TextView data_adv = (TextView) rowView.findViewById(R.id.move_data_advantage_ex);
                    data_adv.setText(data.advantage_frames);
                    TextView data_blk = (TextView) rowView.findViewById(R.id.move_data_blocks_ex);
                    data_blk.setText(data.block_type);
                    TextView data_desc = (TextView) rowView.findViewById(R.id.move_data_desc_ex);
                    data_desc.setText(data.desc);
                }

            }
        }
        else {
            // Set row view for the single move data
            rowView = inflater.inflate(R.layout.listitem_move_single, parent, false);
            // Set basic data
            TextView move_name = (TextView) rowView.findViewById(R.id.move_name);
            move_name.setText(cur.name);
            TextView move_input = (TextView) rowView.findViewById(R.id.move_input);
            move_input.setText(cur.input);

            // Loop through the different move data
            for(int i = 0; i < cur.data.length; i++) {
                // Get the current data
                MoveData data = cur.data[i];
                // Get the current version
                String ver = data.version;

                // Fill the components with data
                TextView data_dmg = (TextView) rowView.findViewById(R.id.move_data_damage);
                data_dmg.setText(data.damage.replace("<br>", "\n").replace("</ br>", "\n"));
                TextView data_act = (TextView) rowView.findViewById(R.id.move_data_active);
                data_act.setText(data.active_frames.replace("<br>", "\n").replace("</ br>", "\n"));
                TextView data_str = (TextView) rowView.findViewById(R.id.move_data_startup);
                data_str.setText(data.startup_frames.replace("<br>", "\n").replace("</ br>", "\n"));
                TextView data_rec = (TextView) rowView.findViewById(R.id.move_data_recovery);
                data_rec.setText(data.recovery_frames.replace("<br>", "\n").replace("</ br>", "\n"));
                TextView data_adv = (TextView) rowView.findViewById(R.id.move_data_advantage);
                data_adv.setText(data.advantage_frames.replace("<br>", "\n").replace("</ br>", "\n"));
                TextView data_blk = (TextView) rowView.findViewById(R.id.move_data_blocks);
                data_blk.setText(data.block_type.replace("<br>", "\n").replace("</ br>", "\n"));
                TextView data_desc = (TextView) rowView.findViewById(R.id.move_data_desc);
                data_desc.setText(data.desc.replace("<br>", "\n").replace("</ br>", "\n"));

            }
        }
        if(position % 2 == 0)
            rowView.setBackgroundColor(Color.BLACK);
        else
            rowView.setBackgroundColor(Color.BLUE);
        return rowView;
    }

}
