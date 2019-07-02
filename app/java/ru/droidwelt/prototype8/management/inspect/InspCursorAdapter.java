package ru.droidwelt.prototype8.management.inspect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import ru.droidwelt.prototype8.R;

class InspCursorAdapter extends SimpleCursorAdapter {

    private Context context;
    private InspFragment act;


    @SuppressWarnings("deprecation")
    InspCursorAdapter(Context context,InspFragment inspFragment, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.act=inspFragment;
        this.context = context;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int pos, View inView, ViewGroup parent) {
        View v = super.getView(pos, inView, parent);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater != null ? inflater.inflate(R.layout.activity_inspect_item, null) : null;
        }

        int index = act.myAdapter.getCursor().getColumnIndex("RVV_STATE");
        String rvv_state = act.myAdapter.getCursor().getString(index);

        if (rvv_state.contains("N")) {
            if (v != null) {
                v.setBackgroundColor(Color.parseColor("#ffe8e8"));
            }
        } else if (rvv_state.contains("V")) {
            if (v != null) {
                v.setBackgroundColor(Color.parseColor("#e8ffe8"));
            }
        } else if (v != null) {
            v.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return (v);
    }


}
