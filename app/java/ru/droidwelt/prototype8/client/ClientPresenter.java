package ru.droidwelt.prototype8.client;

import android.content.Intent;

import java.util.ArrayList;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class ClientPresenter {

    private ClientActivity view;

    public void attachView(ClientActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void getClientOppData() {
        new ClientLoader().getOplList(view, this);
    }

    public void isReadyClientOppData(ArrayList<OppDataClass> oppList, ArrayList<ClientDataClass> clientList) {
        if (view != null) {
            view.finishLoadClientOpp(oppList, clientList);
        }
    }

    public void needLogin() {
        if (view != null) {
            Intent intent = new Intent(view, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            view.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
        }
    }

}
