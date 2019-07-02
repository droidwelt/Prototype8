package ru.droidwelt.prototype8.management.inspect;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class InspPresenter {

    private InspFragment view;
    private Activity act;

    public void attachView(Activity activity, InspFragment usersActivity) {
        view = usersActivity;
        act = activity;
    }

    public void detachView() {
        act = null;
        view = null;
    }


    public void getInspectData(String sqltext) {
        new InspLoader().getInspectList(this, sqltext);
    }

    public void isReadyInspectData(ArrayList<InspDataClass> inspectList) {
        if (act != null) {
            view.finishLoadInspect(inspectList);
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
