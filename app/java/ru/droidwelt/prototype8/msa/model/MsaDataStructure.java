package ru.droidwelt.prototype8.msa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ru.droidwelt.prototype8.msa.avatar.AvatarListDataClass;
import ru.droidwelt.prototype8.msa.fiodev.DevListDataClass;
import ru.droidwelt.prototype8.msa.fiodev.FioListDataClass;
import ru.droidwelt.prototype8.msa.loadimage.LoadImageListDataClass;
import ru.droidwelt.prototype8.msa.loadrecord.LoadRecordListDataClass;

public class MsaDataStructure {

    @SerializedName("msastate")
    @Expose
    private ArrayList<MsaStateListClass> stateList = new ArrayList<>();

    public ArrayList<MsaStateListClass> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<MsaStateListClass> stateList) {
        this.stateList = stateList;
    }


    @SerializedName("fiolist")
    @Expose
    private ArrayList<FioListDataClass> fiolist = new ArrayList<>();

    public ArrayList<FioListDataClass> getFioList() {
        return fiolist;
    }

    public void setFioList(ArrayList<FioListDataClass> fiolist) {
        this.fiolist = fiolist;
    }


    @SerializedName("devlist")
    @Expose
    private ArrayList<DevListDataClass> devlist = new ArrayList<>();

    public ArrayList<DevListDataClass> getDevList() {
        return devlist;
    }

    public void setDevList(ArrayList<DevListDataClass> devlist) {
        this.devlist = devlist;
    }


    @SerializedName("avatarlist")
    @Expose
    private ArrayList<AvatarListDataClass> avatarlist = new ArrayList<>();

    public ArrayList<AvatarListDataClass> getAvatarList() {
        return avatarlist;
    }

    public void setAvatarList(ArrayList<AvatarListDataClass> avatarlist) {
        this.avatarlist = avatarlist;
    }


    @SerializedName("loadimagelist")
    @Expose
    private ArrayList<LoadImageListDataClass> loadimagelist = new ArrayList<>();

    public ArrayList<LoadImageListDataClass> getLoadImageList() {
        return loadimagelist;
    }

    public void setLoadImageList(ArrayList<LoadImageListDataClass> loadimagelist) {
        this.loadimagelist = loadimagelist;
    }


    @SerializedName("loadrecordlist")
    @Expose
    private ArrayList<LoadRecordListDataClass> loadrecordlist = new ArrayList<>();

    public ArrayList<LoadRecordListDataClass> getLoadRecordList() {
        return loadrecordlist;
    }

    public void setLoadRecordList(ArrayList<LoadRecordListDataClass> loadrecordlist) {
        this.loadrecordlist = loadrecordlist;
    }

/*
    @SerializedName("saverecordlist")
    @Expose
    private ArrayList<SaveRecordListDataClass> saverecordlist = new ArrayList<>();

    public ArrayList<SaveRecordListDataClass> getSaveRecordList() {
        return saverecordlist;
    }

    public void setSaveRecordList(ArrayList<SaveRecordListDataClass> saverecordlist) {
        this.saverecordlist = saverecordlist;

    }

*/

}