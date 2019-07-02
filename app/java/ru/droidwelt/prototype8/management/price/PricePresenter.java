package ru.droidwelt.prototype8.management.price;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class PricePresenter {

    private PriceFragment view;
    private Activity act;

    public void attachView(Activity activity  ,PriceFragment usersActivity) {
        act=activity;
        view = usersActivity;
    }

    public void detachView() {
        view = null;
        act = null;
    }


    public void getPriceData() {
        new PriceLoader().getPriceList( this);
    }

    public void isReadyPriceData(ArrayList<PriceDataClass> priceList) {
        if (view != null) {
            view.finishLoadPrn(priceList);
        }
    }

    public void needLogin() {
        if (act != null) {
            Intent intent = new Intent(act, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            act.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
        }
    }

}
