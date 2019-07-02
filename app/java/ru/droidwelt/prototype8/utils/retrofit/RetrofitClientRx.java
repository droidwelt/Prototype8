package ru.droidwelt.prototype8.utils.retrofit;

import android.support.annotation.NonNull;

import ru.droidwelt.prototype8.model.DataStructure;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import ru.droidwelt.prototype8.utils.common.PrefUtils;
import rx.Observable;


public class RetrofitClientRx {

    private static RetrofitClientRx instance;
    private RetrofitApiServiceRx apiService;

    private RetrofitClientRx() {
        Retrofit retrofit = new Retrofit.Builder()  // было final
                .baseUrl(new PrefUtils().getRootURL())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(RetrofitApiServiceRx.class);
    }

    public static RetrofitClientRx getInstance() {
        if (instance == null) {
            instance = new RetrofitClientRx();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }



    public Observable<DataStructure> getLoginDataJSON(@NonNull String sParams) {
        return apiService.getLoginDataJSON(new PrefUtils().getFio_keyToken(), sParams);
    }



    public Observable<DataStructure> getAppActualVersionJSON() {
        return apiService.getAppActualVersionJSON(new PrefUtils().getFio_keyToken());
    }

    public Observable<DataStructure> getAppActualBodyJSON() {
        return apiService.getAppActualBodyJSON(new PrefUtils().getFio_keyToken());
    }

    public Observable<DataStructure> getPriceJSON() {
        return apiService.getPriceJSON(new PrefUtils().getFio_keyToken());
    }

    public Observable<DataStructure> getDeviceRegisterVfyJSON(@NonNull String sParams) {
        return apiService.getDeviceRegisterVfyJSON( sParams);
    }

    public Observable<DataStructure> getDeviceRegisterSetJSON(@NonNull String action, String fcm_id,String fio_id,String model,String phone) {
        return apiService.getDeviceRegisterSetJSON( action, fcm_id,fio_id,model,phone);
    }


    public Observable<DataStructure> getOplJSON() {
        return apiService.getOplJSON( new PrefUtils().getFio_keyToken());
    }

    public Observable<DataStructure> getClientOppJSON() {
        return apiService.getClientOppJSON( new PrefUtils().getFio_keyToken());
    }

    public Observable<DataStructure> getInspectJSON() {
        return apiService.getInspectJSON( new PrefUtils().getFio_keyToken());
    }


    public Observable<DataStructure> setInspectJSON(RequestBody sKey, @NonNull RequestBody sqltext) {
        return apiService.setInspectJSON(sKey, sqltext);
    }


    public Observable<DataStructure> getContactListJSON() {
        return apiService.getContactListJSON(new PrefUtils().getFio_keyToken());
    }



    public Observable<DataStructure> getContactInfoJSON(@NonNull String sClt_Id) {
        return apiService.getContactInfoJSON(new PrefUtils().getFio_keyToken(), sClt_Id);
    }

    public Observable<DataStructure> getContactInfoImageJSON(@NonNull String sClt_Id) {
        return apiService.getContactInfoImageJSON(new PrefUtils().getFio_keyToken(), sClt_Id);
    }


    public Observable<DataStructure> setContactInfoTextJSON(RequestBody clt_comment, @NonNull RequestBody clt_id,  @NonNull RequestBody clt_imagesize) {
        return apiService.setContactInfoTextJSON(clt_comment, clt_id, clt_imagesize);
    }

    public Observable<DataStructure> setContactInfoTextAndImageJSON(MultipartBody.Part fileBig, MultipartBody.Part fileSmall, RequestBody clt_comment, @NonNull RequestBody clt_id) {
        return apiService.setContactInfoTextAndImageJSON(fileBig, fileSmall, clt_comment, clt_id);
    }


    public Observable<DataStructure> getGalleryIdJSON() {
        return apiService.getGalleryIdJSON(new PrefUtils().getFio_keyToken());
    }
}
