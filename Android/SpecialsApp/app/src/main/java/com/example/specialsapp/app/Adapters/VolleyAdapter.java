package com.example.specialsapp.app.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Adapter used to make HTTP requests with Google Volley.
 */
public class VolleyAdapter extends ArrayAdapter<String> implements SectionIndexer {
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections;

    public VolleyAdapter(Context context, int resource, List<String> data) {
        super(context, resource, data);
        alphaIndexer = new HashMap<String, Integer>();
        for (int i = 0; i < data.size(); i++) {
            String s = data.get(i).substring(0, 1).toUpperCase();
            if (!alphaIndexer.containsKey(s))
                alphaIndexer.put(s, i);
        }

        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        for (int i = 0; i < sectionList.size(); i++)
            sections[i] = sectionList.get(i);
    }

    public int getPositionForSection(int section) {
        return alphaIndexer.get(sections[section]);
    }

    public int getSectionForPosition(int position) {
        return 1;
    }

    public Object[] getSections() {
        return sections;
    }
}
