package ru.droidwelt.prototype8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ru.droidwelt.prototype8.client.ClientDataClass;
import ru.droidwelt.prototype8.client.OppDataClass;
import ru.droidwelt.prototype8.contact.ContactListDataClass;
import ru.droidwelt.prototype8.contactinfo.ContactInfoDataClass;
import ru.droidwelt.prototype8.deviceregister.DeviceRegisterVfyDataClass;
import ru.droidwelt.prototype8.gallery.GalleryIdDataClass;
import ru.droidwelt.prototype8.management.inspect.InspDataClass;
import ru.droidwelt.prototype8.management.opl.OplDataClass;
import ru.droidwelt.prototype8.management.price.PriceDataClass;
import ru.droidwelt.prototype8.utils.login.LoginDataClass;
import ru.droidwelt.prototype8.utils.version.AppActualBodyClass;
import ru.droidwelt.prototype8.utils.version.AppActualVersionClass;

public class DataStructure {


    @SerializedName("state")
    @Expose
    private ArrayList<StateListClass> stateList = new ArrayList<>();

    public ArrayList<StateListClass> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<StateListClass> stateList) {
        this.stateList = stateList;
    }



    @SerializedName("logindata")
    @Expose
    private ArrayList<LoginDataClass> loginData = new ArrayList<>();

    public ArrayList<LoginDataClass> getLoginData() {
        return loginData;
    }

    public void setLoginData(ArrayList<LoginDataClass> loginData) {
        this.loginData = loginData;
    }


    @SerializedName("appactualversion")
    @Expose
    private ArrayList<AppActualVersionClass> appactualversion = new ArrayList<>();

    public ArrayList<AppActualVersionClass> getAppActualVersion() {
        return appactualversion;
    }

    public void setAppActualVersion(ArrayList<AppActualVersionClass> appactualversion) {
        this.appactualversion = appactualversion;
    }


    @SerializedName("appactualbody")
    @Expose
    private ArrayList<AppActualBodyClass> appactualbody = new ArrayList<>();

    public ArrayList<AppActualBodyClass> getAppActualBody() {
       return appactualbody;
    }

    public void setAppActualBody(ArrayList<AppActualBodyClass> appactualbody) {
        this.appactualbody = appactualbody;
    }


    private String success;

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }



    @SerializedName("pricelist")
    @Expose
    private ArrayList<PriceDataClass> priceList = new ArrayList<>();

    public ArrayList<PriceDataClass> getPriceList() {
        return priceList;
    }

    public void setPriceList(ArrayList<PriceDataClass> priceList) {
        this.priceList = priceList;
    }




    @SerializedName("devicedatavfy")
    @Expose
    private ArrayList<DeviceRegisterVfyDataClass> devicedata = new ArrayList<>();

    public ArrayList<DeviceRegisterVfyDataClass> getDeviceData() {
        return devicedata;
    }

    public void setDeviceData(ArrayList<DeviceRegisterVfyDataClass> devicedata) {
        this.devicedata = devicedata;
    }


    @SerializedName("opllist")
    @Expose
    private ArrayList<OplDataClass> opllist = new ArrayList<>();

    public ArrayList<OplDataClass> getOplList() {
        return opllist;
    }

    public void setOplList(ArrayList<OplDataClass> opllist) {
        this.opllist = opllist;
    }


    @SerializedName("opplist")
    @Expose
    private ArrayList<OppDataClass> opplist = new ArrayList<>();

    public ArrayList<OppDataClass> getOppList() {
        return opplist;
    }

    public void setOppList(ArrayList<OppDataClass> opplist) {
        this.opplist = opplist;
    }



    @SerializedName("inspectlist")
    @Expose
    private ArrayList<InspDataClass> inspectlist = new ArrayList<>();

    public ArrayList<InspDataClass> getInspectList() {
        return inspectlist;
    }

    public void setInspectList(ArrayList<InspDataClass> inspectlist) {
        this.inspectlist = inspectlist;
    }


    @SerializedName("clientlist")
    @Expose
    private ArrayList<ClientDataClass> clientlist = new ArrayList<>();

    public ArrayList<ClientDataClass> getClientList() {
        return clientlist;
    }

    public void setClientlistList(ArrayList<ClientDataClass> clientlist) {
        this.clientlist = clientlist;
    }



    @SerializedName("contactlist")
    @Expose
    private ArrayList<ContactListDataClass> contactList = new ArrayList<>();

    public ArrayList<ContactListDataClass> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<ContactListDataClass> contactList) {
        this.contactList = contactList;
    }


    @SerializedName("contactinfo")
    @Expose
    private ArrayList<ContactInfoDataClass> clientInfo = new ArrayList<>();

    public ArrayList<ContactInfoDataClass> getContactInfo() {
        return clientInfo;
    }

    public void setClientInfo(ArrayList<ContactInfoDataClass> clientInfo) {
        this.clientInfo = clientInfo;
    }


    @SerializedName("galleryidlist")
    @Expose
    private ArrayList<GalleryIdDataClass> galleryidList = new ArrayList<>();

    public ArrayList<GalleryIdDataClass> getGalleryIdList() {
        return galleryidList;
    }

    public void setGalleryIdList(ArrayList<GalleryIdDataClass> galleryidList) {
        this.galleryidList = galleryidList;
    }


}