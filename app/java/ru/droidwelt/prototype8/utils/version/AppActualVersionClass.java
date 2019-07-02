package ru.droidwelt.prototype8.utils.version;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AppActualVersionClass {

    @SerializedName("appactualver_nomer")
    @Expose
    private String appactualver_nomer;

    @SerializedName("appactualver_name")
    @Expose
    private String appactualver_name;


    public String getAppactuvalver_nomer() {
        return appactualver_nomer;
    }

    public void setAppactuvalver_nomer(String appactuvalver_nomer) {
        this.appactualver_nomer = appactuvalver_nomer;
    }

    public String getAppactuvalver_name() {
        return appactualver_name;
    }

    public void setAppactuvalver_name(String appactuvalver_name) {
        this.appactualver_name = appactuvalver_name;
    }

}