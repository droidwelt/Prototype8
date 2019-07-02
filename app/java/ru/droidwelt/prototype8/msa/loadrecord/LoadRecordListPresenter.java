package ru.droidwelt.prototype8.msa.loadrecord;

import android.content.Intent;

import ru.droidwelt.prototype8.msa.MsaMainActivity;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class LoadRecordListPresenter {

    private MsaMainActivity view;


    public void attachView(MsaMainActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void getRecordListData() {
        new LoadRecordListLoader().getLoadRecordList(this, true);
    }

    public void isReadyRecordListData() {
        Events.EventsMessage ev = new Events.EventsMessage();
        if ((view != null) && (Appl.MSA_MODEVIEW == 2)) {
            ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
            GlobalBus.getBus().post(ev);
        }
        ev.setMes_code(Events.EB_MAIN_REFRESH_RECORDS);
        GlobalBus.getBus().post(ev);
    }

    public void needLogin() {
        if (!(view == null)) {
            Intent intent = new Intent(view, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            if (view != null) {
                view.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
            }
        }
    }


}
