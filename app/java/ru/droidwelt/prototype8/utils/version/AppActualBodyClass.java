package ru.droidwelt.prototype8.utils.version;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppActualBodyClass {

    @SerializedName("appactualbody_body")
    @Expose
    private String appactualbody_body;

    public String getAppactualbody_body() {
        return appactualbody_body;
    }

    public void setAppactualbody_body(String appactualbody_body) {
        this.appactualbody_body = appactualbody_body;
    }



}