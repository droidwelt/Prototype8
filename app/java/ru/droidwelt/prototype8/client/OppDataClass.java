package ru.droidwelt.prototype8.client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OppDataClass {


    @SerializedName("clt_id")
    @Expose
    private String clt_id;

    @SerializedName("dt")
    @Expose
    private String dt;

    @SerializedName("opp_oper")
    @Expose
    private String opp_oper;

    @SerializedName("opp_date")
    @Expose
    private String opp_date;

    @SerializedName("opp_name")
    @Expose
    private String opp_name;

    @SerializedName("opp_sum")
    @Expose
    private String opp_sum;

    public String getClt_id() {
        return clt_id;
    }

    public void setClt_id(String clt_id) {
        this.clt_id = clt_id;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getOpp_oper() {
        return opp_oper;
    }

    public void setOpp_oper(String opp_oper) {
        this.opp_oper = opp_oper;
    }

    public String getOpp_date() {
        return opp_date;
    }

    public void setOpp_date(String opp_date) {
        this.opp_date = opp_date;
    }

    public String getOpp_name() {
        return opp_name;
    }

    public void setOpp_name(String opp_name) {
        this.opp_name = opp_name;
    }

    public String getOpp_sum() {
        return opp_sum;
    }

    public void setOpp_sum(String opp_sum) {
        this.opp_sum = opp_sum;
    }
}
