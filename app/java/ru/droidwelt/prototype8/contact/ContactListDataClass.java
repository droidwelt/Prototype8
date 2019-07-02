package ru.droidwelt.prototype8.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactListDataClass {

    @SerializedName("clt_id")
    @Expose
    private String clt_id;

    @SerializedName("clt_name")
    @Expose
    private String clt_name;

    @SerializedName("clt_addr")
    @Expose
    private String clt_addr;

    @SerializedName("clt_pict")
    @Expose
    private String clt_pict;


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

    public String getClt_pict() {
        return clt_pict;
    }

    public void setClt_pict(String clt_pict) {
        this.clt_pict = clt_pict;
    }



}