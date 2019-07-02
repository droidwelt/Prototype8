package ru.droidwelt.prototype8.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryIdDataClass {

    public String getClt_id() {
        return clt_id;
    }

    public void setClt_id(String clt_id) {
        this.clt_id = clt_id;
    }

    @SerializedName("clt_id")
    @Expose
    private String clt_id;

}