package ru.droidwelt.prototype8.management.price;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceDataClass {

    public String getPrn_name() {
        return prn_name;
    }

    public String getPrn_id() {
        return prn_id;
    }

    public void setPrn_id(String prn_id) {
        this.prn_id = prn_id;
    }

    public void setPrn_name(String prn_name) {
        this.prn_name = prn_name;
    }

    public String getPrn_cena() {
        return prn_cena;
    }

    public void setPrn_cena(String prn_cena) {
        this.prn_cena = prn_cena;
    }

    public String getPrn_res() {
        return prn_res;
    }

    public void setPrn_res(String prn_res) {
        this.prn_res = prn_res;
    }

    public String getPrn_ost() {
        return prn_ost;
    }

    public void setPrn_ost(String prn_ost) {
        this.prn_ost = prn_ost;
    }

    @SerializedName("prn_id")
    @Expose
    private String prn_id;

    @SerializedName("prn_name")
    @Expose
    private String prn_name;

    @SerializedName("prn_cena")
    @Expose
    private String prn_cena;

    @SerializedName("prn_res")
    @Expose
    private String prn_res;

    @SerializedName("prn_ost")
    @Expose
    private String prn_ost;


}
