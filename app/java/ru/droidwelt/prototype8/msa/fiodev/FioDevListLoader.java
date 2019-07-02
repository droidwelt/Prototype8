package ru.droidwelt.prototype8.msa.fiodev;

import android.app.Activity;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import ru.droidwelt.prototype8.msa.model.MsaStateListClass;
import ru.droidwelt.prototype8.msa.retrofit.RetrofitClientMsa;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FioDevListLoader {


    public void getFioDevList(final Activity view, final FioDevListPresenter cip) {

        if (!new PrefUtils().verifyRootAddress())
            return;

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            RetrofitClientMsa.getInstance()
                    .getFioDevJSON()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MsaDataStructure>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            new NotificationUtils().cancelNotificationRead();
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                        }

                        @Override
                        public void onNext(MsaDataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();
                            ArrayList<MsaStateListClass> stateList = ds.getStateList();
                            if (stateList.get(0).getIerr().equals("1")) {
                                ArrayList<FioListDataClass> fioList = ds.getFioList();
                                ArrayList<DevListDataClass> devList = ds.getDevList();
                                cip.isReadyFioDevListData(fioList, devList);
                            } else {
                                cip.needLogin();
                            }
                        }
                    });
        }
    }


}
