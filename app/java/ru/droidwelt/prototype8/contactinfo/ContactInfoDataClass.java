package ru.droidwelt.prototype8.contactinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContactInfoDataClass {

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

    @SerializedName("clt_comment")
    @Expose
    private String clt_comment;

    @SerializedName("clt_image")
    @Expose
    private String clt_image;

    @SerializedName("clt_pict")
    @Expose
    private String clt_pict;

    @SerializedName("clt_lat")
    @Expose
    private String clt_lat;

    @SerializedName("clt_lon")
    @Expose
    private String clt_lon;

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

    @SerializedName("clt_zoom")
    @Expose

    private String clt_zoom;

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

    public String getClt_comment() {
        return clt_comment;
    }

    public void setClt_comment(String clt_comment) {
        this.clt_comment = clt_comment;
    }

    public String getClt_image() {
        return clt_image;
    }

    public void setClt_image(String clt_image) {
        this.clt_image = clt_image;
    }

    public String getClt_pict() {
        return clt_pict;
    }

    public void setClt_pict(String clt_pict) {
        this.clt_pict = clt_pict;
    }
}
