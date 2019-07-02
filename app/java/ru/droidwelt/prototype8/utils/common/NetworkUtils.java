package ru.droidwelt.prototype8.utils.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.droidwelt.prototype8.R;

public class NetworkUtils {


    public  boolean checkConnection(Boolean showToast) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) Appl.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = null;
                if (connectivityManager != null) {
                    activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        if (showToast) {
            new InfoUtils().DisplayToastError(R.string.s_internet_connection_not_available);
        }
        return false;
    }


}
