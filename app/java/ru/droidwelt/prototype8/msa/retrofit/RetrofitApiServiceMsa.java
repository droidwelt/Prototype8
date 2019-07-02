package ru.droidwelt.prototype8.msa.retrofit;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import rx.Observable;

public interface RetrofitApiServiceMsa {


    @GET("GetFioDevJSON.php")
    Observable<MsaDataStructure> getFioDevJSON(@Query("KEYTOKEN") String sKey);


    @Multipart
    @POST("SetAvatarTextJSON.php")
    Observable<MsaDataStructure> setAvatarJSON(@Part("fio_id") RequestBody fio_id,
                                               @Part("fio_name") RequestBody fio_name,
                                               @Part("fio_subname") RequestBody fio_subname);

    @Multipart
    @POST("SetAvatarTextAndImage.php")
    Observable<MsaDataStructure> setAvatarAndImageJSON(@Part MultipartBody.Part fio_image,
                                                       @Part("fio_id") RequestBody fio_id,
                                                       @Part("fio_name") RequestBody fio_name,
                                                       @Part("fio_subname") RequestBody fio_subname);


    @GET("GetLoadImageJSON.php")
    Observable<MsaDataStructure> getLoadImageJSON(@Query("msa_id") String msa_id);

    @GET("GetLoadRecordJSON.php")
    Observable<MsaDataStructure> getLoadRecordJSON(@Query("KEYTOKEN") String sKey,
                                                   @Query("fio_id") String sfio_id,
                                                   @Query("smsa_nomermax") String smsa_nomermax,
                                                   @Query("daysdownload") String sDaysDownload,
                                                   @Query("recordlimit") String recordlimit);

    @Multipart
    @POST("SetSaveRecordJSON.php")
    Observable<MsaDataStructure> setSaveRecordJSON(@Part MultipartBody.Part msa_image,
                                                   @Part("smsa_id") RequestBody smsa_id,
                                                   @Part("smsa_clr") RequestBody smsa_clr,
                                                   @Part("smsa_lbl") RequestBody smsa_lbl,
                                                   @Part("smsa_title") RequestBody smsa_title,
                                                   @Part("smsa_text") RequestBody smsa_text,
                                                   @Part("sfio_id") RequestBody sfio_id,
                                                   @Part("smsa_filetype") RequestBody smsa_filetype,
                                                   @Part("smsa_filename") RequestBody smsa_filename,
                                                   @Part("smsa_imagesize") RequestBody smsa_imagesize,
                                                   @Part("smsb_list") RequestBody smsb_list);

}



