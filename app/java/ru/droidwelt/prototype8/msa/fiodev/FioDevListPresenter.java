package ru.droidwelt.prototype8.msa.fiodev;

import android.content.Intent;

import java.util.ArrayList;

import ru.droidwelt.prototype8.choice.ChoiceFioActivity;
import ru.droidwelt.prototype8.msa.MsaMainActivity;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class FioDevListPresenter {

    private MsaMainActivity view;
    private ChoiceFioActivity choice;


    public void attachView(MsaMainActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }

    public void attachChoice(ChoiceFioActivity usersActivity) {
        choice = usersActivity;
    }

    public void detachChoice() {
        choice = null;
    }


    public void getFioDevListData() {
        new FioDevListLoader().getFioDevList(view, this);
    }

    public void isReadyFioDevListData(ArrayList<FioListDataClass> fioList, ArrayList<DevListDataClass> devList) {
        FioDevListRecoder.recordFio(fioList);
        FioDevListRecoder.recordDev(devList);
        if (!(view == null)) {
            view.displayFioDevList();
        }

        if (!(choice == null)) {
            choice.displayFioDevList();
        }
    }

    public void needLogin() {
        if (view != null) {
            Intent intent = new Intent(view, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            view.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
        }

        if (choice != null) {
            Intent intent = new Intent(choice, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            choice.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
        }
    }

}
