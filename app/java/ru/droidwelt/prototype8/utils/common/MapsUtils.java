package ru.droidwelt.prototype8.utils.common;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;

import java.util.Objects;

public class MapsUtils {


    public void showMapSimple(Activity a) {
        if (new NetworkUtils().checkConnection(true)) {
            Intent intent = new Intent(Appl.INTENT_OSM);
            if (a.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                intent.putExtra("latitude", 60.05);
                intent.putExtra("longitude", 29.97);
                intent.putExtra("zoom", 16.0);
                intent.putExtra("external", "Y");
                a.startActivity(intent);
            } else {
                new InfoUtils().DisplayToastError("Приложение показа карт OpenStreetMap не установлено");
            }
        }
    }


    public void showMaps(Activity a, double lat, double lon, double zoom, String name, String address) {
        new NetworkUtils().checkConnection(true);
            Intent intent = new Intent(Appl.INTENT_OSM);
            if (a.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", lon);
                intent.putExtra("zoom", zoom);
                intent.putExtra("external", "Y");
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                a.startActivity(intent);
            } else {
                new InfoUtils().DisplayToastError("Приложение показа карт OpenStreetMap не установлено");
            }
    }


    public boolean verifyCoord(String lat, String lon, String zoom) {
        lat = new StringUtils().strnormalize(lat);
        lon = new StringUtils().strnormalize(lon);
        zoom = new StringUtils().strnormalize(zoom);
        try {
            double dlat = Double.parseDouble(lat);
        } catch (NumberFormatException e) {
            return false;
        }
        try {
            double dlon = Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            return false;
        }
        try {
            double dzoom = Double.parseDouble(zoom);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public double strToDouble(String s) {
        s = new StringUtils().strnormalize(s);
        try {
            return  Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
            return 0.0;
        }
    }


    public Location createLocation(String lat, String lon, String zoom) {
        lat = new StringUtils().strnormalize(lat);
        lon = new StringUtils().strnormalize(lon);
        boolean err = false;
        Location l = new Location("");
        try {
            l.setLatitude(Double.parseDouble(lat));
        } catch (NumberFormatException e) {
            err = true;
        }
        try {
            l.setLatitude(Double.parseDouble(lon));
        } catch (NumberFormatException e) {
            err = true;
        }
        if (err) {
            return null;
        } else {
            return l;
        }
    }
}
