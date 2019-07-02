package ru.droidwelt.prototype8.utils.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;

import ru.droidwelt.prototype8.R;

public class MainUtils {


    public  int getVersionCode() {
        PackageInfo pinfo;
        try {
            pinfo = Appl.getContext().getPackageManager().getPackageInfo(Appl.getContext().getPackageName(), 0);
            return  pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public  String getVersionName() {
        PackageInfo pinfo;
        try {
            pinfo = Appl.getContext().getPackageManager().getPackageInfo(Appl.getContext().getPackageName(), 0);
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }


    public  String getMainTitle() {
        PackageInfo pinfo;
        try {
            pinfo = Appl.getContext().getPackageManager().getPackageInfo(Appl.getContext().getPackageName(), 0);
            return Appl.getContext().getString(R.string.app_name) + "  " + pinfo.versionName + "." + pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return Appl.getContext().getString(R.string.app_name);
        }
    }





}
