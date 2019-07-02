package ru.droidwelt.prototype8.client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientDataClass {


    @SerializedName("clt_id")
    @Expose
    private String clt_id;

    @SerializedName("clt_name")
    @Expose
    private String clt_name;

    @SerializedName("clt_addr")
    @Expose
    private String clt_addr;

    @SerializedName("clt_phone")
    @Expose
    private String clt_phone;

    @SerializedName("clt_dolg")
    @Expose
    private String clt_dolg;

    @SerializedName("clt_avans")
    @Expose
    private String clt_avans;

    @SerializedName("clt_lat")
    @Expose
    private String clt_lat;

    @SerializedName("clt_lon")
    @Expose
    private String clt_lon;

    @SerializedName("clt_zoom")
    @Expose
    private String clt_zoom;

    public String getClt_lat() {
        return clt_lat;
    }

    public void setClt_lat(String clt_lat) {
        this.clt_lat = clt_lat;
    }

    public String getClt_lon() {
        return clt_lon;
    }

    public void setClt_lon(String clt_lon) {
        this.clt_lon = clt_lon;
    }

    public String getClt_zoom() {
        return clt_zoom;
    }

    public void setClt_zoom(String clt_zoom) {
        this.clt_zoom = clt_zoom;
    }

    public String getClt_id() {
        return clt_id;
    }

    public void setClt_id(String clt_id) {
        this.clt_id = clt_id;
    }

    public String getClt_name() {
        return clt_name;
    }

    public void setClt_name(String clt_name) {
        this.clt_name = clt_name;
    }

    public String getClt_addr() {
        return clt_addr;
    }

    public void setClt_addr(String clt_addr) {
        this.clt_addr = clt_addr;
    }

    public String getClt_phone() {
        return clt_phone;
    }

    public void setClt_phone(String clt_phone) {
        this.clt_phone = clt_phone;
    }

    public String getClt_dolg() {
        return clt_dolg;
    }

    public void setClt_dolg(String clt_dolg) {
        this.clt_dolg = clt_dolg;
    }

    public String getClt_avans() {
        return clt_avans;
    }

    public void setClt_avans(String clt_avans) {
        this.clt_avans = clt_avans;
    }
}
