package ru.droidwelt.prototype8.gallery;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.contactinfo.ContactInfoDataClass;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.retrofit.RetrofitClientRx;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GalleryInfoLoader {


    public void getGalleryInfo(final GalleryActivity act, final String frTag, final String sclt_id) {

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
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" +
                                    e.getMessage()+"\n sclt_id="+sclt_id);
                        }

                        @Override
                        public void onNext(DataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();

                            if (act != null) {
                                ArrayList<ContactInfoDataClass> contactinfoList = ds.getContactInfo();
                                if (contactinfoList.size() > 0) {
                                    ContactInfoDataClass cic = contactinfoList.get(0);
                                    act.isReadyContactInfoData(frTag,cic);
                                }
                            }
                        }
                    });
        }
    }


    public void getGalleryInfoImage(final GalleryActivity act, final String frTag, final String sclt_id) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

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
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" +
                                    e.getMessage()+"\n sclt_id="+sclt_id);
                        }

                        @Override
                        public void onNext(DataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();

                            if (act != null) {
                                ArrayList<ContactInfoDataClass> contactinfoList = ds.getContactInfo();
                                if (contactinfoList.size() > 0) {
                                    ContactInfoDataClass cic = contactinfoList.get(0);
                                    act.isReadyContactInfoImageData(frTag,cic);
                                }
                            }
                        }
                    });
        }
    }


 /*   public void getGalleryInfoFr(final GalleryFragment fr, final String frTag, final String sclt_id) {

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
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" +
                                    e.getMessage()+"\n sclt_id="+sclt_id);
                        }

                        @Override
                        public void onNext(DataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();

                            if (fr != null) {
                                ArrayList<ContactInfoDataClass> contactinfoList = ds.getContactInfo();
                                if (contactinfoList.size() > 0) {
                                    ContactInfoDataClass cic = contactinfoList.get(0);
                                    fr.isReadyContactInfoData(cic);
                                }
                            }
                        }
                    });
        }
    }


    public void getGalleryInfoImageFr (final GalleryFragment fr, final String frTag, final String sclt_id) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

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
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" +
                                    e.getMessage()+"\n sclt_id="+sclt_id);
                        }

                        @Override
                        public void onNext(DataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();

                            if (fr != null) {
                                ArrayList<ContactInfoDataClass> contactinfoList = ds.getContactInfo();
                                if (contactinfoList.size() > 0) {
                                    ContactInfoDataClass cic = contactinfoList.get(0);
                                    fr.isReadyContactInfoImageData(cic);
                                }
                            }
                        }
                    });
        }
    }
    */

}
