package ru.droidwelt.prototype8.msa.fiodev;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class FioListDataClass {

    @SerializedName("fio_image")
    @Expose
    private String fio_image;

    @SerializedName("fio_id")
    @Expose
    private String fio_id;

    @SerializedName("fio_tp")
    @Expose
    private String fio_tp;

    @SerializedName("fio_idgr")
    @Expose
    private String fio_idgr;

    @SerializedName("fio_idmb")
    @Expose
    private String fio_idmb;

    @SerializedName("fio_name")
    @Expose
    private String fio_name;

    @SerializedName("fio_subname")
    @Expose
    private String fio_subname;

    public String getFio_image() {
        return fio_image;
    }

    public void setFio_image(String fio_image) {
        this.fio_image = fio_image;
    }

    public String getFio_id() {
        return fio_id;
    }

    public void setFio_id(String fio_id) {
        this.fio_id = fio_id;
    }

    public String getFio_tp() {
        return fio_tp;
    }

    public void setFio_tp(String fio_tp) {
        this.fio_tp = fio_tp;
    }

    public String getFio_idgr() {
        return fio_idgr;
    }

    public void setFio_idgr(String fio_idgr) {
        this.fio_idgr = fio_idgr;
    }

    public String getFio_idmb() {
        return fio_idmb;
    }

    public void setFio_idmb(String fio_idmb) {
        this.fio_idmb = fio_idmb;
    }

    public String getFio_name() {
        return fio_name;
    }

    public void setFio_name(String fio_name) {
        this.fio_name = fio_name;
    }

    public String getFio_subname() {
        return fio_subname;
    }

    public void setFio_subname(String fio_subname) {
        this.fio_subname = fio_subname;
    }
}
