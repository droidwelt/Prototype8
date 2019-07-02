package ru.droidwelt.prototype8.management.opl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OplDataClass {


    @SerializedName("opl_id")
    @Expose
    private String opl_id;

    @SerializedName("clt_name")
    @Expose
    private String clt_name;

    @SerializedName("opl_name")
    @Expose
    private String opl_name;

    @SerializedName("opl_sum")
    @Expose
    private Float opl_sum;

    @SerializedName("opl_date")
    @Expose
    private String opl_date;

    public String getOpl_id() {
        return opl_id;
    }

    public void setOpl_id(String opl_id) {
        this.opl_id = opl_id;
    }

    public String getClt_name() {
        return clt_name;
    }

    public void setClt_name(String clt_name) {
        this.clt_name = clt_name;
    }

    public String getOpl_name() {
        return opl_name;
    }

    public void setOpl_name(String opl_name) {
        this.opl_name = opl_name;
    }

    public Float getOpl_sum() {
        return opl_sum;
    }

    public void setOpl_sum(Float opl_sum) {
        this.opl_sum = opl_sum;
    }

    public String getOpl_date() {
        return opl_date;
    }

    public void setOpl_date(String opl_date) {
        this.opl_date = opl_date;
    }
}
