package ru.droidwelt.prototype8.msa.avatar;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import ru.droidwelt.prototype8.msa.retrofit.RetrofitClientMsa;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AvatarListLoader {


    @SuppressWarnings("UnusedAssignment")
    public void setAvatarList(final AvatarListPresenter cip) {

        if (new NetworkUtils().checkConnection( true)) {
            new NotificationUtils().createNotificationWrite();

            RequestBody fio_id = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(Appl.FIO_ID));
            RequestBody fio_name = RequestBody.create(MediaType.parse("text/plain"), new StringUtils().strnormalize(Appl.FIO_NAME));
            RequestBody fio_subname = RequestBody.create(MediaType.parse("text/plain"), new StringUtils().strnormalize(Appl.FIO_SUBNAME));

            Subscription subscription;
            if (Appl.FIO_IMAGE != null) {
                ByteArrayOutputStream streamBig = new ByteArrayOutputStream();
                Appl.FIO_IMAGE.compress(Bitmap.CompressFormat.JPEG, 75, streamBig);
                byte[] imageInByteBig = streamBig.toByteArray();
                RequestBody mpfileBig = RequestBody.create(MediaType.parse("image/*"), imageInByteBig);
                MultipartBody.Part mpfileToUploadBig = MultipartBody.Part.createFormData("fio_image", "fio_image", mpfileBig);

                subscription = RetrofitClientMsa.getInstance()
                        .setAvatarAndImageJSON(mpfileToUploadBig, fio_id, fio_name, fio_subname)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<MsaDataStructure>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                new NotificationUtils().cancelNotificationWrite();
                                new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                            }

                            @Override
                            public void onNext(MsaDataStructure ds) {
                                new NotificationUtils().cancelNotificationWrite();
                            }
                        });
            } else {

                subscription = RetrofitClientMsa.getInstance()
                        .setAvatarJSON(fio_id, fio_name, fio_subname)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<MsaDataStructure>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                new NotificationUtils().cancelNotificationWrite();
                                new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                            }

                            @Override
                            public void onNext(MsaDataStructure ds) {
                                new NotificationUtils().cancelNotificationWrite();
                            }
                        });

            }

        }
    }




}
