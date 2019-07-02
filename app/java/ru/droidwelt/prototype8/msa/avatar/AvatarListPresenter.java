package ru.droidwelt.prototype8.msa.avatar;

import android.content.Intent;

import ru.droidwelt.prototype8.msa.MsaAvatarActivity;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class AvatarListPresenter {

    private MsaAvatarActivity view;


    public void attachView(MsaAvatarActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void getAvatarListData() {
        new AvatarListLoader().setAvatarList( this);
    }



    public void needLogin() {
        if (view != null) {
            Intent intent = new Intent(view, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            view.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
        }
    }

}
