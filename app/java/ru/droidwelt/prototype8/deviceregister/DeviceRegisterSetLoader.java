package ru.droidwelt.prototype8.deviceregister;

import android.os.Build;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.retrofit.RetrofitClientRx;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeviceRegisterSetLoader {

    public void registerDeviceSet(final DeviceRegisterActivity view, final boolean device_registred) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();
            String action = "1";
            if (device_registred) {
                action = "0";
            }
            RetrofitClientRx.getInstance()
                    .getDeviceRegisterSetJSON(action, Appl.DEVICE_FCM, Integer.toString(Appl.FIO_ID), android.os.Build.BRAND, Build.MODEL)
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
                            if (view != null) {
                                view.displayDevice(ds.getDeviceData());
                                //   new InfoUtils().DisplayToastOk("Ok");
                            }
                        }
                    });

        }
    }


}
