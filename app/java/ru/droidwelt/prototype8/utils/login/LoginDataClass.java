package ru.droidwelt.prototype8.utils.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDataClass {

    @SerializedName("fio_id")
    @Expose
    private String fio_id;

    @SerializedName("fio_name")
    @Expose
    private String fio_name;

    @SerializedName("fio_subname")
    @Expose
    private String fio_subname;

    @SerializedName("tok_name")
    @Expose
    private String tok_name;


    public String getFio_id() {
        return fio_id;
    }

    public void setFio_id(String fio_id) {
        this.fio_id = fio_id;
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

    public String getTok_name() {
        return tok_name;
    }

    public void setTok_name(String tok_name) {
        this.tok_name = tok_name;
    }


}
