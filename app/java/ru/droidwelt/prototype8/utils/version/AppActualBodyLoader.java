package ru.droidwelt.prototype8.utils.version;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.main.MainActivity;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.retrofit.RetrofitClientRx;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppActualBodyLoader {


    public void updateAppAction(final MainActivity view) {
        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            RetrofitClientRx.getInstance()
                    .getAppActualBodyJSON()
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
                                view.updateAppActionResult(ds);
                            }
                        }
                    });
        }
    }


}
