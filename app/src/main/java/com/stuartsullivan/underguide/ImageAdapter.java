package com.stuartsullivan.underguide;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Stuart on 2014-12-26.
 */
public class ImageAdapter extends BaseAdapter {
    // DEBUG TAGS
    private static final String TAG = "APP-DEBUG";
    // Variables
    private Context context;
    private DatabaseAdapter adapter;
    private static String[] characters = {"Hyde", "Linne", "Waldstein", "Carmine",
            "Orie", "Gordeau", "Merkava", "Vatista", "Seth", "Yuzuriha", "Hilda",
            "Eltnum", "Chaos","Akatsuki", "Nanase", "Byakuya"};

    // Keep the images in the array
    public Integer[] thumbIds = {
            R.drawable.ch_hyde_icon,  R.drawable.ch_linne_icon, R.drawable.ch_wald_icon,
            R.drawable.ch_car_icon, R.drawable.ch_orie_icon, R.drawable.ch_gord_icon,
            R.drawable.ch_mer_icon, R.drawable.ch_vat_icon, R.drawable.ch_seth_icon,
            R.drawable.ch_yuzu_icon, R.drawable.ch_hilda_icon, R.drawable.ch_elt_icon,
            R.drawable.ch_cha_icon, R.drawable.ch_aka_icon, R.drawable.ch_nan_icon,
            R.drawable.ch_bya_icon
    };

    // Constructor
    public ImageAdapter(Context c) {
        context = c;
        adapter = new DatabaseAdapter(context);
    }

    @Override
    public int getCount() {
        return thumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return thumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(thumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(160, 160));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.open();
                Log.i(TAG, "Loading Character with: " + position + 1);
                Intent i = new Intent(context, CharacterView.class);
                i.putExtra("id", adapter.getCharacterId(characters[position]));
                context.startActivity(i);
                adapter.close();
            }
        });
        return imageView;
    }
}
