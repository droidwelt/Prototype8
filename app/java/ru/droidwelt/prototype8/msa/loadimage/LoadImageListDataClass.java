package ru.droidwelt.prototype8.msa.loadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoadImageListDataClass {

    @SerializedName("msa_image")
    @Expose
    private String msa_image;

    @SerializedName("msa_imagesize")
    @Expose
    private Integer msa_imagesize;

    public String getMsa_image() {
        return msa_image;
    }

    public void setMsa_image(String msa_image) {
        this.msa_image = msa_image;
    }

    public Integer getMsa_imagesize() {
        return msa_imagesize;
    }

    public void setMsa_imagesize(Integer msa_imagesize) {
        this.msa_imagesize = msa_imagesize;
    }
}
