package ru.droidwelt.prototype8.msa.retrofit;

import android.support.annotation.NonNull;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import rx.Observable;


public class RetrofitClientMsa {

    private static RetrofitClientMsa instance;
    private RetrofitApiServiceMsa apiService;

    private RetrofitClientMsa() {
        Retrofit retrofit = new Retrofit.Builder()  // было final
                .baseUrl(new PrefUtils().getRootURL())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(RetrofitApiServiceMsa.class);
    }

    public static RetrofitClientMsa getInstance() {
        if (instance == null) {
            instance = new RetrofitClientMsa();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }


    public Observable<MsaDataStructure> getFioDevJSON() {
        return apiService.getFioDevJSON(new PrefUtils().getFio_keyToken());
    }

    public Observable<MsaDataStructure> setAvatarJSON(RequestBody fio_id,
                                                      RequestBody fio_name,
                                                      RequestBody fio_subname) {
        return apiService.setAvatarJSON(fio_id, fio_name, fio_subname);
    }

    public Observable<MsaDataStructure> setAvatarAndImageJSON(MultipartBody.Part fio_image,
                                                              RequestBody fio_id,
                                                              RequestBody fio_name,
                                                              RequestBody fio_subname) {
        return apiService.setAvatarAndImageJSON(fio_image, fio_id, fio_name, fio_subname);
    }


    public Observable<MsaDataStructure> getLoadImageJSON(@NonNull String smsa_id) {
        return apiService.getLoadImageJSON(smsa_id);
    }

    public Observable<MsaDataStructure> getLoadRecordJSON(@NonNull String smsa_nomermax,
                                                          @NonNull String recordlimit) {
        String sFio_id = Integer.toString(Appl.FIO_ID);
        String sDaysDownload = Integer.toString(new PrefUtils().getDaysDownLoad());
        return apiService.getLoadRecordJSON(new PrefUtils().getFio_keyToken(), sFio_id, smsa_nomermax, sDaysDownload, recordlimit);
    }

    public Observable<MsaDataStructure> setSaveRecordJSON(MultipartBody.Part msa_image,
                                                          RequestBody smsa_id,
                                                          RequestBody smsa_clr,
                                                          RequestBody smsa_lbl,
                                                          RequestBody smsa_title,
                                                          RequestBody smsa_text,
                                                          RequestBody sfio_id,
                                                          RequestBody smsa_filetype,
                                                          RequestBody smsa_filename,
                                                          RequestBody smsa_imagesize,
                                                          RequestBody smsb_list) {
        return apiService.setSaveRecordJSON(msa_image, smsa_id, smsa_clr, smsa_lbl, smsa_title, smsa_text,
                sfio_id, smsa_filetype, smsa_filename, smsa_imagesize,smsb_list);
    }


}
