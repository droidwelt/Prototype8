package ru.droidwelt.prototype8.management.price;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import ru.droidwelt.prototype8.R;

class PriceCursorAdapter extends SimpleCursorAdapter {

    private Context context;

    @SuppressWarnings("deprecation")
    PriceCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int pos, View inView, ViewGroup parent) {
        View v = super.getView(pos, inView, parent);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater != null ? inflater.inflate(R.layout.activity_price_fragment, null) : null;
        }
        return (v);
    }
}
