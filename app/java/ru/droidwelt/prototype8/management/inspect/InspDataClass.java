package ru.droidwelt.prototype8.management.inspect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InspDataClass {


    @SerializedName("rvv_id")
    @Expose
    private String rvv_id;

    @SerializedName("rvv_name")
    @Expose
    private String rvv_name;

    @SerializedName("rvv_bar")
    @Expose
    private String rvv_bar;

    @SerializedName("rvv_comment")
    @Expose
    private String rvv_comment;

    @SerializedName("rvv_daterev")
    @Expose
    private String rvv_daterev;

    @SerializedName("rvv_cena")
    @Expose
    private String rvv_cena;

    public String getRvv_id() {
        return rvv_id;
    }

    public void setRvv_id(String rvv_id) {
        this.rvv_id = rvv_id;
    }

    public String getRvv_name() {
        return rvv_name;
    }

    public void setRvv_name(String rvv_name) {
        this.rvv_name = rvv_name;
    }

    public String getRvv_bar() {
        return rvv_bar;
    }

    public void setRvv_bar(String rvv_bar) {
        this.rvv_bar = rvv_bar;
    }

    public String getRvv_comment() {
        return rvv_comment;
    }

    public void setRvv_comment(String rvv_comment) {
        this.rvv_comment = rvv_comment;
    }

    public String getRvv_daterev() {
        return rvv_daterev;
    }

    public void setRvv_daterev(String rvv_daterev) {
        this.rvv_daterev = rvv_daterev;
    }

    public String getRvv_cena() {
        return rvv_cena;
    }

    public void setRvv_cena(String rvv_cena) {
        this.rvv_cena = rvv_cena;
    }

    public String getRvv_kvo1() {
        return rvv_kvo1;
    }

    public void setRvv_kvo1(String rvv_kvo1) {
        this.rvv_kvo1 = rvv_kvo1;
    }

    public String getRvv_kvo2() {
        return rvv_kvo2;
    }

    public void setRvv_kvo2(String rvv_kvo2) {
        this.rvv_kvo2 = rvv_kvo2;
    }

    @SerializedName("rvv_kvo1")

    @Expose
    private String rvv_kvo1;

    @SerializedName("rvv_kvo2")
    @Expose
    private String rvv_kvo2;


}
