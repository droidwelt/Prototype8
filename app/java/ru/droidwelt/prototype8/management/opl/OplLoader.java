package ru.droidwelt.prototype8.management.opl;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.model.StateListClass;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.retrofit.RetrofitClientRx;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OplLoader {

    public void getOplList(final OplPresenter cip) {

        if (!new PrefUtils().verifyRootAddress())
            return;

        if (new NetworkUtils().checkConnection( true)) {
            new NotificationUtils().createNotificationRead();

            RetrofitClientRx.getInstance()
                    .getOplJSON()
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
                            ArrayList<StateListClass> stateList = ds.getStateList();
                            if (stateList.get(0).getIerr().equals("1")) {
                                ArrayList<OplDataClass> oplList;
                                oplList = ds.getOplList();
                                cip.isReadyOplData(oplList);
                            } else {
                                cip.needLogin();
                            }
                        }
                    });
        }
    }





}
