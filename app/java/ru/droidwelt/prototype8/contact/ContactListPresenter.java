package ru.droidwelt.prototype8.contact;

import android.content.Intent;

import java.util.ArrayList;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.login.LoginActivity;

public class ContactListPresenter {

    private ContactActivity view;


    public void attachView(ContactActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void getContactListData() {
        new ContactListLoader().getContactList(this);
    }

    public void isReadyContactListData(ArrayList<ContactListDataClass> contactList) {
        if (view != null) {
            view.displayContactList(contactList);
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
