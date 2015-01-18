package com.stuartsullivan.unibwikiguide;

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
    private Context context;

    // Keep the images in the array
    public Integer[] thumbIds = {
            R.drawable.red, R.drawable.yln, R.drawable.org,
            R.drawable.blu, R.drawable.grn, R.drawable.dgn,
            R.drawable.brn, R.drawable.bln, R.drawable.lav,
            R.drawable.pnk, R.drawable.lbl, R.drawable.gry
    };

    // Constructor
    public ImageAdapter(Context c) {
        context = c;
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
        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Loading Character with: " + position + 1);
                Intent i = new Intent(context, CharacterView.class);
                i.putExtra("id", position+1);
                context.startActivity(i);
            }
        });
        return imageView;
    }
}
