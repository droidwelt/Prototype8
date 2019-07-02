package ru.droidwelt.prototype8.deviceregister;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceRegisterVfyDataClass {


    @SerializedName("dev_id")
    @Expose
    private String dev_id;

    @SerializedName("dev_ime")
    @Expose
    private String dev_ime;

    @SerializedName("dev_fcm")
    @Expose
    private String dev_fcm;

    @SerializedName("dev_model")
    @Expose
    private String dev_model;

    @SerializedName("dev_phone")
    @Expose
    private String dev_phone;


    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }
    public String getDev_ime() {
        return dev_ime;
    }

    public void setDev_ime(String dev_ime) {
        this.dev_ime = dev_ime;
    }

    public String getDev_fcm() {
        return dev_fcm;
    }

    public void setDev_fcm(String dev_fcm) {
        this.dev_fcm = dev_fcm;
    }

    public String getDev_model() {
        return dev_model;
    }

    public void setDev_model(String dev_model) {
        this.dev_model = dev_model;
    }

    public String getDev_phone() {
        return dev_phone;
    }

    public void setDev_phone(String dev_phone) {
        this.dev_phone = dev_phone;
    }
}
