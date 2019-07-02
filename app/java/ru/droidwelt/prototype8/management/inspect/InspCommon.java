package ru.droidwelt.prototype8.management.inspect;

import android.database.Cursor;

import ru.droidwelt.prototype8.utils.common.Appl;

public class InspCommon {

    public  Cursor getInspectRecords(String ff) {
        String sSQL =
                "select  _id, RVV_ID,RVV_NAME,RVV_CENA,RVV_KVO1,RVV_KVO1,RVV_KVO2,RVV_COMMENT,RVV_BAR,RVV_DATEREV,RVV_STATE "
                        + "from RVV "
                        + "where RVV_NAME like '%" + ff + "%' "
                        + "order by RVV_NAME ";
        return Appl.getDatabase().rawQuery(sSQL, null);
    }
}
