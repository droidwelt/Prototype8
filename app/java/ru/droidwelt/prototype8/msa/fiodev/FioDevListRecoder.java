package ru.droidwelt.prototype8.msa.fiodev;

import android.content.ContentValues;
import android.util.Base64;

import java.util.ArrayList;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.StringUtils;

import static android.util.Base64.DEFAULT;

public class FioDevListRecoder {


    public static void recordFio(ArrayList<FioListDataClass> fioList) {
        String sLite = "DELETE FROM FIO;";
        Appl.getDatabase().execSQL(sLite);

        for (int i = 0; i < fioList.size(); i++) {
            FioListDataClass pdc = fioList.get(i);
            String FIO_ID = new StringUtils().strnormalize(pdc.getFio_id());
            String FIO_TP = new StringUtils().strnormalize(pdc.getFio_tp());
            String FIO_IDGR = new StringUtils().strnormalize(pdc.getFio_idgr());
            String FIO_IDMB = new StringUtils().strnormalize(pdc.getFio_idmb());
            String FIO_NAME = new StringUtils().strnormalize(pdc.getFio_name());
            String FIO_SUBNAME = new StringUtils().strnormalize(pdc.getFio_subname());
            sLite =
                    "INSERT INTO FIO  (FIO_ID,FIO_TP,FIO_IDGR,FIO_IDMB,FIO_NAME,FIO_SUBNAME) " +
                            "VALUES ('" + FIO_ID + "','" + FIO_TP + "','" + FIO_IDGR + "','" + FIO_IDMB + "','" + FIO_NAME + "','" + FIO_SUBNAME + "');";
            Appl.getDatabase().execSQL(sLite);

            if (!(pdc.getFio_image().isEmpty())) {
                byte[] decodedStringBig = Base64.decode(pdc.getFio_image(), DEFAULT);
                if (decodedStringBig != null) {
                    ContentValues editFIO = new ContentValues();
                    editFIO.put("FIO_IMAGE", decodedStringBig);
                    Appl.getDatabase().update("FIO", editFIO, "FIO_ID='" + FIO_ID + "'", null);
                }
            }
        }
    }


    public static void recordDev(ArrayList<DevListDataClass> devList) {
        String sLite = "DELETE FROM DEV;";
        Appl.getDatabase().execSQL(sLite);

        for (int i = 0; i < devList.size(); i++) {
            DevListDataClass pdc = devList.get(i);
            String DEV_ID = new StringUtils().strnormalize(pdc.getDev_id());
            String FIO_ID = new StringUtils().strnormalize(pdc.getFio_id());
            String DEV_FCM = new StringUtils().strnormalize(pdc.getDev_fcm());
            String DEV_CODE = new StringUtils().strnormalize(pdc.getDev_code());
            String DEV_MODEL = new StringUtils().strnormalize(pdc.getDev_model());

            sLite = "INSERT INTO DEV  (DEV_ID,FIO_ID,DEV_FCM,DEV_CODE,DEV_MODEL) " +
                    "VALUES ('" + DEV_ID + "','" + FIO_ID + "','" + DEV_FCM + "','" + DEV_CODE + "','" + DEV_MODEL + "');";
            Appl.getDatabase().execSQL(sLite);
        }

    }


}
