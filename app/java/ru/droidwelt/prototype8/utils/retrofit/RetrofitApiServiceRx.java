package ru.droidwelt.prototype8.utils.retrofit;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import ru.droidwelt.prototype8.model.DataStructure;
import rx.Observable;

public interface RetrofitApiServiceRx {


    @GET("GetLoginDataJSON.php")
    Observable<DataStructure> getLoginDataJSON(@Query("KEYTOKEN") String sKey,
                                               @Query("FIO_PWD") String sParams);

    @GET("GetAppActualVersionJSON.php")
    Observable<DataStructure> getAppActualVersionJSON(@Query("KEYTOKEN") String sKey);

    @GET("GetAppActualBodyJSON.php")
    Observable<DataStructure> getAppActualBodyJSON(@Query("KEYTOKEN") String sKey);


    @GET("GetPriceJSON.php")
    Observable<DataStructure> getPriceJSON(@Query("KEYTOKEN") String sKey);

    @GET("GetDeviceRegisterVfyJSON.php")
    Observable<DataStructure> getDeviceRegisterVfyJSON(@Query("DEV_FCM") String fcm_id);

    @GET("GetDeviceRegisterSetJSON.php")
    Observable<DataStructure> getDeviceRegisterSetJSON(@Query("ACTION") String action,
                                                       @Query("DEV_FCM") String fcm_id,
                                                       @Query("FIO_ID") String fio_id,
                                                       @Query("MODEL") String model,
                                                       @Query("PHONE") String phone);

    @GET("GetOplJSON.php")
    Observable<DataStructure> getOplJSON (@Query("KEYTOKEN") String sKey);

    @GET("GetClientOppJSON.php")
    Observable<DataStructure> getClientOppJSON (@Query("KEYTOKEN") String sKey);


    @GET("GetInspectJSON.php")
    Observable<DataStructure> getInspectJSON (@Query("KEYTOKEN") String sKey);

    @Multipart
    @POST("SetInspectJSON.php")
    Observable<DataStructure> setInspectJSON   (@Part("KEYTOKEN") RequestBody sKey,
                                                @Part("SQLTEXT") RequestBody sqltext);


    @GET("GetContactListJSON.php")
    Observable<DataStructure> getContactListJSON(@Query("KEYTOKEN") String sKey);

    @GET("GetContactInfoJSON.php")
    Observable<DataStructure> getContactInfoJSON(@Query("KEYTOKEN") String sKey,
                                                 @Query("CLT_ID") String sParams);

    @GET("GetContactInfoImageJSON.php")
    Observable<DataStructure> getContactInfoImageJSON(@Query("KEYTOKEN") String sKey,
                                                 @Query("CLT_ID") String sParams);


    @Multipart
    @POST("SetContactInfoText.php")
    Observable<DataStructure> setContactInfoTextJSON(@Part("clt_comment") RequestBody clt_comment,
                                                     @Part("clt_id") RequestBody clt_id,
                                                     @Part("clt_imagesize") RequestBody clt_imagesize);

    @Multipart
    @POST("SetContactInfoTextAndImage.php")
    Observable<DataStructure> setContactInfoTextAndImageJSON(@Part MultipartBody.Part fileBig,
                                                             @Part MultipartBody.Part fileSmall,
                                                             @Part("clt_comment") RequestBody nameBig,
                                                             @Part("clt_id") RequestBody clt_id);


    @GET("GetGalleryIdJSON.php")
    Observable<DataStructure> getGalleryIdJSON(@Query("KEYTOKEN") String sKey);

}



