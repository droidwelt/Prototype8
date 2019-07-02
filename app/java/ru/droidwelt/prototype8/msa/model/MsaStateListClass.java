package ru.droidwelt.prototype8.msa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MsaStateListClass {

    @SerializedName("ierr")
    @Expose
    private String ierr;

    @SerializedName("serr")
    @Expose
    private String serr;


    public String getIerr() {
        return ierr;
    }

    public void setIerr(String ierr) {
        this.ierr = ierr;
    }

    public String getSerr() {
        return serr;
    }

    public void setSerr(String serr) {
        this.serr = serr;
    }


}