package ru.droidwelt.prototype8.management.inspect;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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

public class InspLoader {

    public void getInspectList(final InspPresenter cip, final String strsqltext) {
        if (!new PrefUtils().verifyRootAddress())
            return;

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), new PrefUtils().getFio_keyToken());
            RequestBody sqltext = RequestBody.create(MediaType.parse("text/plain"), strsqltext);

            RetrofitClientRx.getInstance()
                    .setInspectJSON(key, sqltext)
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
                                ArrayList<InspDataClass> inspectList = ds.getInspectList();
                                cip.isReadyInspectData(inspectList);
                            } else {
                                cip.needLogin();
                            }
                        }
                    });



        }
    }


}
