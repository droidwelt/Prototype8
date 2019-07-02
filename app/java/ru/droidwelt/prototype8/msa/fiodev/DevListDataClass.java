package ru.droidwelt.prototype8.msa.fiodev;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DevListDataClass {


    @SerializedName("fio_id")
    @Expose
    private String fio_id;

    @SerializedName("dev_id")
    @Expose
    private String dev_id;

    @SerializedName("dev_fcm")
    @Expose
    private String dev_fcm;

    @SerializedName("dev_code")
    @Expose
    private String dev_code;

    @SerializedName("dev_model")
    @Expose
    private String dev_model;

    public String getFio_id() {
        return fio_id;
    }

    public void setFio_id(String fio_id) {
        this.fio_id = fio_id;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getDev_fcm() {
        return dev_fcm;
    }

    public void setDev_fcm(String dev_fcm) {
        this.dev_fcm = dev_fcm;
    }

    public String getDev_code() {
        return dev_code;
    }

    public void setDev_code(String dev_code) {
        this.dev_code = dev_code;
    }

    public String getDev_model() {
        return dev_model;
    }

    public void setDev_model(String dev_model) {
        this.dev_model = dev_model;
    }
}
