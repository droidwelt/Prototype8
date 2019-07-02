package ru.droidwelt.prototype8.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import android.app.Application;


@SuppressLint({"CommitPrefEdits", "StaticFieldLeak"})
public class Appl extends Application {

    private static AppComponent component;

    public static Context context;
    public static final String APP_PREFIX = "PROTO8";

    public static final String INTENT_OSM  = "droidwelt.ru.osm1.MAIN"; //показ карт OpenStreetMap

	/*	Типы в MSA_STATE:   ( СОСТОЯНИЕ )
       0  - черновик
       1 -  шаблон
       2 -  входящие, полученные, лента ,,,,,,,  22 - прочтенные
       3 -  отправленные
       4 -  исходящие
       5 -  удаленные
       10 - избранное

        et_msa_title.setBackgroundColor(ContextCompat.getColor(Appl.getContext(),R.color.background_text));
       */

	// https://www.materialui.co/colors
    public static final int BarColor_0 = Color.parseColor("#BF360C"); //черновик
    public static final int BarColor_2 = Color.parseColor("#388E3C"); //новые
    public static final int BarColor_22 = Color.parseColor("#00796b"); //входящие
    public static final int BarColor_3 = Color.parseColor("#1976D2"); //отправленные
    public static final int BarColor_4 = Color.parseColor("#AD1457"); //исходящие
    public static final int BarColor_10 = Color.parseColor("#BD07FD"); //избранное
    public static final int BarColor_avatar = Color.parseColor("#376197"); //аватар
    public static final int BarColor_main = Color.parseColor("#00ACC1"); //главная форма

    public static int FIO_ID = -1;
    public static String FIO_KEYTOKEN = "";
    public static String FIO_NAME = "Вход не осуществлён";
    public static String FIO_SUBNAME = "";
    public static Bitmap FIO_IMAGE = null;

    public static String APP_STYLE = "F"; // FLAT, CARD
    public static final int FIO_AVATAR_SIZE = 160;
    public static final int CONTACT_THUMNNAIL_SIZE = 160;
    public static int MAIN_RECYCLER_HEIGHT = 1000;

    public static int MSA_MODEVIEW = -1;
    public static int MSA_MODEEDIT = 0;
    public static String MSA_ID = "";
    public static int MSA_POS = -1;

    public static String DEVICE_FCM = "";

    public static String APP_PATH = Environment.getExternalStorageDirectory().toString() + "/Proto8/";
    public static String DB_PATH = APP_PATH + "Database/";
    public static String TEMP_PATH = APP_PATH + "Temp/";
    public static String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/";

    public static String CONTACT_FN = "Contact_temp.jpg";
    public static String CONTACT_TEMP = TEMP_PATH + CONTACT_FN;
    public static Uri CONTACT_URI = Uri.fromFile(new File(Appl.TEMP_PATH, "Contact_temp.jpg"));

    private static SQLiteDatabase database = null;
    public static String DB_NAME = "proto8.db3";
    public static String DB_NAMEMODEL = "proto8_etal.db3";

    public static String PHPName = "/Proto8/";

    public static final int EXIT_CODE_LOGIN = 1000;
    public static final int EXIT_CODE_CLIENTINFO = 2000;
    public static final int EXIT_CODE_MSA = 3000;
    public static final int EXIT_CODE_MSAEDIT = 4000;
    public static final int EXIT_CODE_AVATAR = 5000;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getBaseContext();
        new FileUtils().createTempPath();
        APP_STYLE = new PrefUtils().getAppStyle();

        component = DaggerAppComponent.create();
        setTextSizeToApp ();
    }

    public static AppComponent getComponent() {
        return component;
    }


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Appl.context = context;
    }


    public static SQLiteDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(SQLiteDatabase db) {
        database = db;
    }


    public  void  setTextSizeToApp () {
        int size_coef = new PrefUtils().getTextSizePosit ();
        final float start_value = 0.55f; //начальное значение размера шрифта
        final float step = 0.15f; //шаг увеличения коэффициента
        Resources res = getContext().getResources();

        float new_value = start_value + size_coef * step;
        Configuration configuration = new Configuration(res.getConfiguration());
        configuration.fontScale = new_value;
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }
























}
