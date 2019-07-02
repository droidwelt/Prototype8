package ru.droidwelt.prototype8.management.opl;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;


import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class OplPresenter {

    private OplFragment view;
    private Activity act;

    public void attachView(Activity activity  ,OplFragment usersActivity) {
        act=activity;
        view = usersActivity;
    }

    public void detachView() {
        view = null;
        act = null;
    }


    public void getOplData() {
        new OplLoader().getOplList(this);
    }

    public void isReadyOplData(ArrayList<OplDataClass> oplList) {
        if (view != null) {
            view.finishLoadOpl(oplList);
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
