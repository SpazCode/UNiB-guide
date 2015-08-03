package com.stuartsullivan.underguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.feras.mdv.MarkdownView;

public class MarkDownFragment extends Fragment {
    // Log Constants
    private static final String TAG = "APP-DEBUG";
    // The view
    private MarkdownView markdownView;

    public MarkDownFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "PatchNotes - onCreateView");
        View view = inflater.inflate(R.layout.fragment_mark_down, container, false);
        markdownView = (MarkdownView) view.findViewById(R.id.markdown_viewer);
        return view;
    }

    public void loadMarkDown(String filepath) {
        Log.i(TAG, "PatchNotes - LoadMarkDown");
        markdownView.loadMarkdownUrl(filepath);
    }

}
