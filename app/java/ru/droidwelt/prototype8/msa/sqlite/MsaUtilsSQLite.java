package ru.droidwelt.prototype8.msa.sqlite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;


public class MsaUtilsSQLite {


    @SuppressLint("NewApi")
    public  Drawable getMyAvatar() {
        Drawable dr;
        if (Appl.FIO_ID < 0) {
            Appl.FIO_NAME = "Вход не осуществлён";
            Appl.FIO_SUBNAME = "";
            Appl.FIO_IMAGE = null;
            dr = Appl.context.getDrawable(R.mipmap.ic_avatar_one);
        } else {
            String sSQL = " select FIO_NAME,FIO_SUBNAME,FIO_IMAGE from FIO where FIO_ID=" + Appl.FIO_ID + "";
            Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
            c.moveToFirst();
            if (c.getCount() > 0) {
             /*   FIO_NAME = Appl.strnormalize(c.getString(0));
                FIO_SUBNAME = Appl.strnormalize(c.getString(1));
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                Editor editor = sp.edit();
                editor.putString("fio_name", FIO_NAME);
                editor.putString("fio_subname", FIO_SUBNAME);
                editor.apply(); */

                byte[] img = c.getBlob(2);
                if (img != null) Appl.FIO_IMAGE = BitmapFactory.decodeByteArray(img, 0, img.length);
                else Appl.FIO_IMAGE = null;
                c.close();
            } else
                new InfoUtils().DisplayToastInfo("Обновите базу респондентов.\nВашего имени нет в базе устройства", R.mipmap.ic_avatar_my, Toast.LENGTH_LONG);

            if (Appl.FIO_IMAGE != null)
                dr = new BitmapDrawable(Appl.context.getResources(), Appl.FIO_IMAGE);
            else dr = Appl.context.getDrawable(R.mipmap.ic_avatar_my);
        }
        return dr;
    }

    public  byte[] getByteArrayFromMsaImaget(String xmsa_id) {
        int i = 0;
        int theImagePos = 0;
        int rdbytes = 200000;
        byte[] resall = null;

        while (rdbytes == 200000 & i < 300) {
            Cursor cursor = Appl.getDatabase().rawQuery(
                    "select substr(MSA_IMAGE,1+" + String.valueOf(i) + "*200000,200000) from MSA  where MSA_ID='" + xmsa_id + "'", null);
            i = i + 1;
            cursor.moveToFirst();
            byte[] res;
            rdbytes = cursor.getBlob(0).length;
            if (rdbytes > 0) {
                res = cursor.getBlob(0);
                resall = new GraphicUtils().concatArray(resall, res);
                theImagePos = theImagePos + res.length;
            }
            cursor.close();
        }
        return resall;
    }


    public  byte[] getByteArrayFromMsaImageSmart(String xmsa_id, int imagesize) {
        if (imagesize > 0) {
            return getByteArrayFromMsaImaget (xmsa_id);
        } else {
            return new byte[]{0, 0, 0, 0};
        }
    }


    // cоздать новую запись
    public  void create_new_draft() {
        String sSQL =
                " insert into MSA "
                        + " (MSA_ID,FIO_ID,MSA_STATE,MSA_DATE,MSA_GRP,MSA_TITLE,MSA_TEXT,MSA_FILETYPE,MSA_CLR,MSA_LBL) "
                        + " values " + " ('" + Appl.MSA_ID + "'," + Appl.FIO_ID
                        + ",0,datetime ('now', 'localtime'),'"
                        + Appl.APP_PREFIX + "','Новое','','','',''); ";
        Appl.getDatabase().execSQL(sSQL);

    }


    // переместить в черновики
    public  void move_one_to_draft_SQLite(String xmsa_id) {
        if (!new StringUtils().strnormalize(xmsa_id).equals("")) {
            String sSQL = "update MSA set MSA_STATE=0,FIO_ID=" + Appl.FIO_ID + " where MSA_ID='" + xmsa_id + "';";
            Appl.getDatabase().execSQL(sSQL);
        }
    }

    // копировать в черновики, избранное
    public  String copy_one_to_folder_SQLite(String xmsa_id, int imsa_target) {
        String nes_MSA_ID = "";
        if (!new StringUtils().strnormalize(xmsa_id).equals("")) {
            nes_MSA_ID = new StringUtils().generate_GUID();
            String sSQL = " insert into MSA "
                    + " (MSA_ID,FIO_ID,MSA_STATE,MSA_DATE,MSA_CLR,MSA_LBL,MSA_GRP,MSA_TITLE,MSA_TEXT,MSA_FILETYPE,MSA_FILENAME,MSA_IMAGE) "
                    + " select '" + nes_MSA_ID + "', " + Appl.FIO_ID + ", " + imsa_target + ","
                    + " datetime ('now', 'localtime'),MSA_CLR,MSA_LBL,MSA_GRP,MSA_TITLE,MSA_TEXT," +
                    "case when length(MSA_IMAGE)>0 then MSA_FILETYPE else '' end as MSA_FILETYPE," +
                    "case when length(MSA_IMAGE)>0 then MSA_FILENAME else '' end as MSA_FILENAME," +
                    "MSA_IMAGE "
                    + "from MSA where MSA_ID='" + xmsa_id + "';";
            Appl.getDatabase().execSQL(sSQL);

            sSQL = " select FIO_ID from MSB where MSA_ID='" + xmsa_id + "';";
            Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int fio_id = c.getInt(c.getColumnIndex("FIO_ID"));
                String sBSQL = "insert into MSB  (MSA_ID,FIO_ID)  values ('" + nes_MSA_ID + "'," + fio_id + ");";
                Appl.getDatabase().execSQL(sBSQL);
                c.moveToNext();
            }
            c.close();
        }
        return nes_MSA_ID;
    }

    // удалить выбранную запись
    public  void delete_selected_SQLite(String MSA_ID) {
        if (!new StringUtils().strnormalize(MSA_ID).equals("")) {
            Appl.getDatabase().execSQL("delete from MSA where MSA_ID='" + MSA_ID + "'");
        }
    }


    //изменить статус записи (папку)
    public  void change_record_state_SQLite(int new_state) {
        String sSQL = " update MSA set "
                //	+ " MSA_DATE=datetime ('now', 'localtime'),"
                + " MSA_STATE = " + new_state
                + " where MSA_ID='" + Appl.MSA_ID + "'; ";
        Appl.getDatabase().execSQL(sSQL);
    }


    //изменить статус всех записей папки
    public  void change_all_record_state_SQLite(int old_state, int new_state) {
        String sSQL = " update MSA set "
                + " MSA_STATE = " + new_state
                + " where MSA_STATE=" + old_state + "; ";
        Appl.getDatabase().execSQL(sSQL);
    }


    // удалить одну рабочую запись
    public  void message_delete_from_SQLite() {
        Appl.getDatabase().execSQL("delete from MSB where MSA_ID='" + Appl.MSA_ID + "'");
        Appl.getDatabase().execSQL("delete from MSA where MSA_ID='" + Appl.MSA_ID + "'");
    }

    // составить список выбранных получателей в виде строки
    public  String message_get_send_list() {
        StringBuilder res = new StringBuilder();
        String sSQL =
                " select  FIO_NAME "
                        + " from MSB B "
                        + " left join FIO F on B.FIO_ID=F.FIO_ID "
                        + " where MSA_ID='" + Appl.MSA_ID + "'"
                        + " order by FIO_NAME;";

        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            res.append(c.getString(0)).append("; ");
            c.moveToNext();
        }
        c.close();
        return res.toString();
    }


}
