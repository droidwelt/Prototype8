package ru.droidwelt.prototype8.management.opl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.droidwelt.prototype8.R;

class OplCursorAdapter extends SimpleCursorAdapter {

    private Context context;

    @SuppressWarnings("deprecation")
    OplCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int pos, View inView, ViewGroup parent) {
        View v = super.getView(pos, inView, parent);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater != null ? inflater.inflate(R.layout.activity_opl_fragment, null) : null;
        }

        if (v != null) {
            TextView tvn = v.findViewById(R.id.text_OPL_SUM);
            String sSum = tvn.getText().toString();
            if (sSum.contains("-")) {
                tvn.setTextColor(Color.RED);
            } else {
                tvn.setTextColor(Color.BLACK);
            }
        }
        return (v);
    }
}
