package ru.droidwelt.prototype8.management.inspect;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;


public class InspCursorLoader extends CursorLoader {

    private String filter;

    public void setFilter  (String s) {
        filter = s;
    }

    InspCursorLoader(Context context, String s) {
        super(context);
        filter = s;
    }

    @Override
    public Cursor loadInBackground() {
        return new InspCommon().getInspectRecords(filter);
    }


}
