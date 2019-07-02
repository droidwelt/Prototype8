package ru.droidwelt.prototype8.msa.loadrecord;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class LoadRecordListDataClass {

    @SerializedName("msa_id")
    @Expose
    private String msa_id;

    @SerializedName("fio_id")
    @Expose
    private String fio_id;

    @SerializedName("msa_nomer")
    @Expose
    private String msa_nomer;

    @SerializedName("msa_clr")
    @Expose
    private String msa_clr;

    @SerializedName("msa_lbl")
    @Expose
    private String msa_lbl;

    @SerializedName("msa_date")
    @Expose
    private String msa_date;

    @SerializedName("msa_title")
    @Expose
    private String msa_title;

    @SerializedName("msa_text")
    @Expose
    private String msa_text;

    @SerializedName("msa_filetype")
    @Expose
    private String msa_filetype;

    @SerializedName("msa_filename")
    @Expose
    private String msa_filename;

    public String getMsa_id() {
        return msa_id;
    }

    public void setMsa_id(String msa_id) {
        this.msa_id = msa_id;
    }

    public String getFio_id() {
        return fio_id;
    }

    public void setFio_id(String fio_id) {
        this.fio_id = fio_id;
    }

    public String getMsa_nomer() {
        return msa_nomer;
    }

    public void setMsa_nomer(String msa_nomer) {
        this.msa_nomer = msa_nomer;
    }

    public String getMsa_clr() {
        return msa_clr;
    }

    public void setMsa_clr(String msa_clr) {
        this.msa_clr = msa_clr;
    }

    public String getMsa_lbl() {
        return msa_lbl;
    }

    public void setMsa_lbl(String msa_lbl) {
        this.msa_lbl = msa_lbl;
    }

    public String getMsa_date() {
        return msa_date;
    }

    public void setMsa_date(String msa_date) {
        this.msa_date = msa_date;
    }

    public String getMsa_title() {
        return msa_title;
    }

    public void setMsa_title(String msa_title) {
        this.msa_title = msa_title;
    }

    public String getMsa_text() {
        return msa_text;
    }

    public void setMsa_text(String msa_text) {
        this.msa_text = msa_text;
    }

    public String getMsa_filetype() {
        return msa_filetype;
    }

    public void setMsa_filetype(String msa_filetype) {
        this.msa_filetype = msa_filetype;
    }

    public String getMsa_filename() {
        return msa_filename;
    }

    public void setMsa_filename(String msa_filename) {
        this.msa_filename = msa_filename;
    }
}
