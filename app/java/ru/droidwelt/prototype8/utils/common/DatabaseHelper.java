package ru.droidwelt.prototype8.utils.common;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.droidwelt.prototype8.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;


    public DatabaseHelper() {
        super(Appl.context, Appl.DB_NAME, null, 1);
        File PATH_TO = new File(Appl.DB_PATH); // куда
        if (!(PATH_TO.isDirectory() && PATH_TO.canExecute() && PATH_TO.canRead() && PATH_TO.canWrite())) {
            if (!PATH_TO.mkdir())
                return;
        }
        db = openDataBase();
        Appl.setDatabase(db);
    }


    private void copyDataBase() {
        try {
            InputStream externalDbStream = Appl.context.getAssets().open(Appl.DB_NAMEMODEL);
            String outFileName = Appl.DB_PATH + Appl.DB_NAME;
            OutputStream localDbStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = externalDbStream.read(buffer)) > 0)
                localDbStream.write(buffer, 0, bytesRead);
            localDbStream.close();
            externalDbStream.close();
        } catch (IOException ignored) {
        }
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDb;
        try {
            checkDb = SQLiteDatabase.openDatabase(Appl.DB_PATH + Appl.DB_NAME,
                    null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            //Log.i("checkDataBase", "checkDataBase  - no file");
            return false;
        }
        if (checkDb != null) checkDb.close();
        return checkDb != null;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        if (db == null) {
            if (!checkDataBase()) {
                new InfoUtils().DisplayToastOk(R.string.s_db_create);
                copyDataBase();
            }
            try {
                db = SQLiteDatabase.openDatabase(Appl.DB_PATH + Appl.DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            } catch (SQLException e) {
                // Log.e("SQLiteDatabase", "openDataBase");
            }
        }
        return db;
    }


    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}