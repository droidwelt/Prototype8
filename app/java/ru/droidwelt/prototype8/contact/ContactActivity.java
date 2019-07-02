package ru.droidwelt.prototype8.contact;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.login.LoginLoader;

public class ContactActivity extends AppCompatActivity {

    private ContactListPresenter presenter;
    LinearLayoutManager mLayoutManager;
    public  ContactListAdapter mAdapter;

    List<ContactListDataClass> list_cont;
    public RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    // private static final String TAG = ContactActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_contact);
        setTitle(R.string.header_contact);
        GlobalBus.getBus().register(this);

        list_cont = new ArrayList<>();
        mRecyclerView = findViewById(R.id.ca_main);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContactListAdapter();
        mAdapter.setContactActivity(this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = findViewById(R.id.ca_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        requestContactListData();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new PrefUtils().animateFinish(this);
    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(false);
            requestContactListData();
        }
    };

    //  запрос данных
    public void requestContactListData() {
        presenter = new ContactListPresenter();
        presenter.attachView(this);
        presenter.getContactListData();
    }

    // вызывается из Presenter по получении ответа
    public void displayContactList(ArrayList<ContactListDataClass> contactList) {
        list_cont = contactList;
        mAdapter.notifyDataSetChanged();
        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
        GlobalBus.getBus().post(ev);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mi_contact_download:
                if (new NetworkUtils().checkConnection(true)) {
                    requestContactListData();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        if (!(returnedIntent == null)) {
            switch (requestCode) {
                case Appl.EXIT_CODE_LOGIN:
                    String code = returnedIntent.getStringExtra("code");
                    new LoginLoader().loginUser(this, code);
                    break;

           /*     case Appl.EXIT_CODE_CLIENTINFO:
                    if (resultCode >= 0) {
                        int Pos = returnedIntent.getIntExtra("POS", -1);
                        if (Pos >= 0) {
                          //  findByLIST_POS(Pos);
                        }
                    }
                    break; */
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(presenter == null))
            presenter.detachView();
        GlobalBus.getBus().unregister(this);
        new NotificationUtils().cancelNotification(0);
    }


    @Subscribe
    public void getMessage(Events.EventsMessage ev) {
        if (ev.getMes_code().equals(Events.EB_CLIENTINTO_UPDATE))
            requestContactListData();
    }


}
