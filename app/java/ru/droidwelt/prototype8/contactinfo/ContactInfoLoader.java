package ru.droidwelt.prototype8.contactinfo;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.retrofit.RetrofitClientRx;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactInfoLoader {


    public void getContactInfo(final ContactInfoActivity view, final ContactInfoPresenter cip, final String sclt_id) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

             RetrofitClientRx.getInstance()
                    .getContactInfoJSON(sclt_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DataStructure>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            new NotificationUtils().cancelNotificationRead();
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                        }

                        @Override
                        public void onNext(DataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();
                            ArrayList<ContactInfoDataClass> contactinfoList = ds.getContactInfo();
                            if (contactinfoList.size() > 0) {
                                ContactInfoDataClass cic = contactinfoList.get(0);
                                cip.isReadyContactInfoData(view, cic);
                            }
                        }
                    });
        }
    }

    public void getContactImageInfo(final ContactInfoActivity view, final ContactInfoPresenter cip, final String sclt_id) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            Events.EventsMessage ev = new Events.EventsMessage();
            ev.setMes_code(Events.EB_CONTACTINFO_INDICATOR_ON);
            GlobalBus.getBus().post(ev);

            RetrofitClientRx.getInstance()
                    .getContactInfoImageJSON(sclt_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DataStructure>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            new NotificationUtils().cancelNotificationRead();
                            Events.EventsMessage ev = new Events.EventsMessage();
                            ev.setMes_code(Events.EB_CONTACTINFO_INDICATOR_OFF);
                            GlobalBus.getBus().post(ev);
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                        }

                        @Override
                        public void onNext(DataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();
                            Events.EventsMessage ev = new Events.EventsMessage();
                            ev.setMes_code(Events.EB_CONTACTINFO_INDICATOR_OFF);
                            GlobalBus.getBus().post(ev);
                            ArrayList<ContactInfoDataClass> contactinfoList = ds.getContactInfo();
                            if (contactinfoList.size() > 0) {
                                ContactInfoDataClass cic = contactinfoList.get(0);
                                cip.isReadyContactInfoImageData(view, cic);
                            }
                        }
                    });
        }
    }


    public void saveClientInfo(final ContactInfoActivity view, final String sclt_id, final String sclt_comment, final String sclt_imagesize) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationWrite();

            RequestBody clt_id = RequestBody.create(MediaType.parse("text/plain"), sclt_id);
            RequestBody clt_comment = RequestBody.create(MediaType.parse("text/plain"), sclt_comment);
            RequestBody clt_imagesize = RequestBody.create(MediaType.parse("text/plain"), sclt_imagesize);

            //  Запись
            if ((view.getContactIimageModified()) && (sclt_imagesize.equals("1"))) {
                Bitmap bm = new GraphicUtils().getImageBig(Appl.CONTACT_TEMP, 0);
                if (bm == null) {
                    new NotificationUtils().cancelNotificationWrite();
                    return;
                }
                ByteArrayOutputStream streamBig = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 75, streamBig);
                byte[] imageInByteBig = streamBig.toByteArray();
                RequestBody mpfileBig = RequestBody.create(MediaType.parse("image/*"), imageInByteBig);
                MultipartBody.Part mpfileToUploadBig = MultipartBody.Part.createFormData("clt_image", "clt_image", mpfileBig);

                Bitmap imageSmall = ThumbnailUtils.extractThumbnail(bm, Appl.CONTACT_THUMNNAIL_SIZE, Appl.CONTACT_THUMNNAIL_SIZE);

                ByteArrayOutputStream streamSmall = new ByteArrayOutputStream();
                imageSmall.compress(Bitmap.CompressFormat.JPEG, 75, streamSmall);
                byte[] imageInByteSmall = streamSmall.toByteArray();
                RequestBody mpfileSmall = RequestBody.create(MediaType.parse("image/*"), imageInByteSmall);
                MultipartBody.Part mpfileToUploadSmall = MultipartBody.Part.createFormData("clt_pict", "clt_pict", mpfileSmall);

                 RetrofitClientRx.getInstance()
                        .setContactInfoTextAndImageJSON(mpfileToUploadBig, mpfileToUploadSmall, clt_comment, clt_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DataStructure>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                new NotificationUtils().cancelNotificationWrite();
                                new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "/n" + e.getMessage());
                            }

                            @Override
                            public void onNext(DataStructure ds) {
                                new NotificationUtils().cancelNotificationWrite();
                                view.isContactInfoSaved(sclt_comment);
                                //  new InfoUtils().DisplayToastOk(CommonUtils.getContext().getString(R.string.s_success_ok));
                            }
                        });
            } else {

                 RetrofitClientRx.getInstance()
                        .setContactInfoTextJSON(clt_comment, clt_id, clt_imagesize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DataStructure>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                new NotificationUtils().cancelNotificationWrite();
                                new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                            }

                            @Override
                            public void onNext(DataStructure ds) {
                                new NotificationUtils().cancelNotificationWrite();
                                view.isContactInfoSaved(sclt_comment);
                                // new InfoUtils().DisplayToastOk(CommonUtils.getContext().getString(R.string.s_success_ok));
                            }
                        });

            }

        }
    }


}
