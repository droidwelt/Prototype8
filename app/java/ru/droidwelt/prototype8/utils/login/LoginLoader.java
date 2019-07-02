package ru.droidwelt.prototype8.utils.login;

import android.app.Activity;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.retrofit.RetrofitClientRx;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginLoader {


    public void loginUser(final Activity view, final String code) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            RetrofitClientRx.getInstance()
                    .getLoginDataJSON(code)
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
                            ArrayList<LoginDataClass> loginDataList = ds.getLoginData();
                            if ((!loginDataList.isEmpty()) & (!loginDataList.get(0).getFio_id().equals("0"))) {
                                String s = Appl.getContext().getString(R.string.s_wellcome_login) + " "
                                        + loginDataList.get(0).getFio_name()
                                        + "\n" + loginDataList.get(0).getFio_subname()
                                        + "\n" + Appl.getContext().getString(R.string.s_wellcome_login2);
                                new InfoUtils().DisplayToastOk(s);

                                Appl.FIO_ID = Integer.valueOf(loginDataList.get(0).getFio_id());
                                Appl.FIO_NAME = loginDataList.get(0).getFio_name();
                                Appl.FIO_SUBNAME = loginDataList.get(0).getFio_subname();
                                Appl.FIO_KEYTOKEN = loginDataList.get(0).getTok_name();

                                new PrefUtils().setFio_id(loginDataList.get(0).getFio_id());
                                new PrefUtils().setFio_name(loginDataList.get(0).getFio_name());
                                new PrefUtils().setFio_subname(loginDataList.get(0).getFio_subname());
                                new PrefUtils().setFio_pw(code);
                                new PrefUtils().setFio_keyToken(loginDataList.get(0).getTok_name());
                            } else {
                                new InfoUtils().DisplayToastError(R.string.s_wrong_code);
                            }
                        }
                    });

        }
    }


}
