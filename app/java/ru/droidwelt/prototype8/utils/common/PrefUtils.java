package ru.droidwelt.prototype8.utils.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.Display;

import ru.droidwelt.prototype8.R;

public class PrefUtils {

    public void animateStart(Activity a) {
        switch (getAnimation_mode()) {
            case 1:
                a.overridePendingTransition(R.anim.activity_down_up_enter, R.anim.activity_down_up_exit);
                break;
            case 2:
                a.overridePendingTransition(R.anim.activity_up_down_enter, R.anim.activity_up_down_exit);
                break;
            case 3:
                a.overridePendingTransition(R.anim.activity_left_rigth_enter, R.anim.activity_left_rigth_exit);
                break;
            case 4:
                a.overridePendingTransition(R.anim.activity_rigth_left_enter, R.anim.activity_rigth_left_exit);
                break;
            default:
                break;
        }
    }

    public void animateFinish(Activity a) {
        switch (getAnimation_mode()) {
            case 1:
                a.overridePendingTransition(R.anim.activity_down_up_close_enter, R.anim.activity_down_up_close_exit);
                break;
            case 2:
                a.overridePendingTransition(R.anim.activity_up_down_close_enter, R.anim.activity_up_down_close_exit);
                break;
            case 3:
                a.overridePendingTransition(R.anim.activity_left_rigth_close_enter, R.anim.activity_left_rigth_close_exit);
                break;
            case 4:
                a.overridePendingTransition(R.anim.activity_rigth_left_close_enter, R.anim.activity_rigth_left_close_exit);
                break;
            default:
                break;
        }
    }

    public int getAnimation_mode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String picture_size_val = sp.getString("animation_mode", "0");
        return Integer.parseInt(picture_size_val);
    }

    public String getServer_nomer() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getString("server_nomer", "0");
    }

    public String getServer_name(String nomer) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String key = "server_name_" + nomer;
        return sp.getString(key, "Адрес сервера не определён");
    }


    public boolean getUploadImmediately() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getBoolean("upload_immediately", false);
    }

    public int getDaysDownLoad() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String sd = sp.getString("download_dates", "10");
        return Integer.parseInt(sd);
    }

    public int getKvoDownLoad() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String sd = sp.getString("download_kvo", "100");
        return Integer.parseInt(sd);
    }

    public String getFcmCode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String sd = sp.getString("fcm_code", "");
        if (sd.isEmpty()) sd = "";
        return sd;
    }

    public void setFcmCode(String code) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fcm_code", code);
        editor.apply();
    }


    public boolean verifyRootAddress() {
        boolean res = (android.util.Patterns.WEB_URL.matcher(getRootURL()).matches());
        if (!res) {
            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_server_adress_invalid) + "\n" + getRootURL());
        }
        return res;
    }

    public void getIMSA_MODE() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        Appl.MSA_MODEVIEW = sp.getInt("imsa_mode", -1);
    }


    public void setIMSA_MODE() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("imsa_mode", Appl.MSA_MODEVIEW);
        editor.apply();
    }


    public String getRootURL() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String serverNomer = sp.getString("server_nomer", "0");
        if (serverNomer.isEmpty()) serverNomer = "0";

        switch (serverNomer) {
            case "0": {
                String s = "http://" + sp.getString("server_name_0", "http://192.168.1.2") + Appl.PHPName;
                return s;
            }
            case "1": {
                String s = sp.getString("server_name_1", "http://192.168.1.2");
                if (s.isEmpty()) {
                    s = "http://192.168.1.2";
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("server_name", s);
                    editor.apply();
                }
                return "http://" + s + Appl.PHPName;
            }
            case "2": {
                return "http://" + sp.getString("server_name_2", "http://192.168.1.2") + Appl.PHPName;
            }
            default:
                return "";
        }
    }

    public void getFIOData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        try {
            String s = new StringUtils().strnormalize(sp.getString("fio_id", "-1"));
            if (s.equals(""))
                s = "-1";
            Appl.FIO_ID = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            Appl.FIO_ID = -1;
        }
        Appl.FIO_NAME = sp.getString("fio_name", "");
        Appl.FIO_SUBNAME = sp.getString("fio_subname", "");
        Appl.FIO_KEYTOKEN = sp.getString("fio_keytoken", "");
    }

    public void setFIOData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fio_id", Integer.toString(Appl.FIO_ID));
        editor.putString("fio_name", Appl.FIO_NAME);
        editor.putString("fio_subname", Appl.FIO_SUBNAME);
        editor.apply();
    }

    public void setFio_pw(String fio_pw) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fio_pw", fio_pw);
        editor.apply();
    }


    public String getFio_keyToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getString("fio_keytoken", "");
    }

    public void setFio_keyToken(String fio_keytoken) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fio_keytoken", fio_keytoken);
        editor.apply();
    }

    public String getFio_id() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getString("fio_id", "");
    }

    public void setFio_id(String fio_id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fio_id", fio_id);
        editor.apply();
    }

    public String getFio_name() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getString("fio_name", "");
    }

    public void setFio_name(String fio_name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fio_name", fio_name);
        editor.apply();
    }


    public void setFio_subname(String fio_subname) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fio_subname", fio_subname);
        editor.apply();
    }


    public void defineMainRecyclerHeight(Activity a) {
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        Appl.MAIN_RECYCLER_HEIGHT = (size.y - 100);
    }

    public String getAppStyle() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String s = sp.getString("app_style", "F");
        Appl.APP_STYLE = s;
        return s;
    }

    public boolean getMesFabKey() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getBoolean("mes_fab_key", true);
    }

    public int getDownload_maxsize() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String s = new StringUtils().strnormalize(sp.getString("download_maxsize", "10"));
        if (s.equals("")) s = "10";
        int i;

        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            i = 10;
        }
        if (i < 0) i = 0;
        return i * 1024 * 1024;
    }


    public int getImageMaxSize() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        int i;
        try {
            String s = sp.getString("image_max_size", "2048");
            i = Integer.valueOf(s);
        } catch (Exception e) {
            i = 2048;
        }
        return i;
    }


    public boolean getGalleryEffect() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        return sp.getBoolean("gallery_effect", true);
    }


    public void setTextSizePosit(int iVal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        SharedPreferences.Editor editor = sp.edit();
        String s = Integer.toString(iVal);
        editor.putString("textsize_xpos", s);
        editor.apply();
    }

    public int getTextSizePosit() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Appl.getContext());
        String s = sp.getString("textsize_xpos", "3");
        if (s.equals("")) s = "3";
        return Integer.parseInt(s);
    }


}
