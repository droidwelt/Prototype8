package ru.droidwelt.prototype8.client;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
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


public class ClientActivity extends AppCompatActivity {

    public final int ClIENTVIEWCODE = 1;
    private ClientCursorAdapter myClientAdapter;
    public String filter = "";
    public ListView lv;
    private List<Integer> LIST_CLT_ID;
    private ClientPresenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_client);
        setTitle(R.string.header_client);
        LIST_CLT_ID = new ArrayList<>();

        displayLoadDateTime();

        String[] from = new String[]{"CLT_NAME", "CLT_DOLG", "CLT_AVANS"};
        int[] to = new int[]{R.id.text_client_item_CLT_NAME, R.id.text_item_client_CLT_DOLG, R.id.text_item_client_CLT_AVANS};

        setmyAdapter(new ClientCursorAdapter(this, R.layout.activity_client_item, getClientRecords(filter), from, to));
        lv = findViewById(R.id.lv_client);
        lv.setOnItemClickListener(viewContactListener);
        lv.setAdapter(getClientAdapter());

        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        EditText editFilter = findViewById(R.id.client_edit_Filter);
        editFilter.setText(filter);
        editFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter = s.toString();
                getClientAdapter().changeCursor(getClientRecords(filter));
                getClientAdapter().notifyDataSetChanged();
            }
        });
    }

    // слушатель событий в ListView
    OnItemClickListener viewContactListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            Cursor cursor = Appl.getDatabase().rawQuery("select CLT_ID from CLT where _id = " + String.valueOf(id), null);
            cursor.moveToFirst();
            int CLT_ID = cursor.getInt(0);
            cursor.close();

            Intent viewClient = new Intent(ClientActivity.this, ClientViewActivity.class);
            viewClient.putExtra("CLT_ID", CLT_ID);
            viewClient.putExtra("POS", position);
            viewClient.putIntegerArrayListExtra("LIST_CLT_ID", (ArrayList<Integer>) LIST_CLT_ID);
            startActivityForResult(viewClient, ClIENTVIEWCODE);
            new PrefUtils().animateStart(ClientActivity.this);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new PrefUtils().animateFinish(this);
    }



    public void startloadClientOpp() {
        presenter = new ClientPresenter();
        presenter.attachView(this);
        presenter.getClientOppData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mi_client_download:
                if (new NetworkUtils().checkConnection(true)) {
                    startloadClientOpp();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public Cursor getClientRecords(String filter) {
        String sSQL = "select  _id, CLT_ID,CLT_NAME,CLT_DOLG,CLT_AVANS,CLT_LAT,CLT_LON,CLT_ZOOM "
                + "from CLT "
                + "where CLT_NAME like '%" + filter + "%' "
                + "order by CLT_NAME ";
        LIST_CLT_ID.clear();
        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
        c.moveToFirst();
        int index_CLT_ID = c.getColumnIndex("CLT_ID");
        while (!c.isAfterLast()) {
            LIST_CLT_ID.add(c.getInt(index_CLT_ID));
            c.moveToNext();
        }
        return c;
    }


    public ClientCursorAdapter getClientAdapter() {
        return myClientAdapter;
    }

    public void setmyAdapter(ClientCursorAdapter ClientatAdapter) {
        myClientAdapter = ClientatAdapter;
    }

    private void displayLoadDateTime() {
        TextView tv = findViewById(R.id.textView_ClientLastLoad);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String sdate = getString(R.string.s_lastload) + sp.getString("clt_dateload", getString(R.string.s_time_not_defined));
        tv.setText(sdate);
    }

    private void setLoadDateTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String sdate = DateFormat.format("dd-MM-yyyy HH:mm:ss", new Date()).toString();
        Editor editor = sp.edit();
        editor.putString("clt_dateload", sdate);
        editor.apply();
    }


    public void findByLIST_POS(int LIST_POS) {
        lv.setSelection(LIST_POS);
        lv.smoothScrollToPosition(LIST_POS);
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

                case ClIENTVIEWCODE:
                    if (resultCode == RESULT_OK) {
                        int Pos = returnedIntent.getIntExtra("POS", -1);
                        if (Pos >= 0) {
                            findByLIST_POS(Pos);
                        }
                    }
                    break;
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


    public void finishLoadClientOpp(ArrayList<OppDataClass> dsOpp, ArrayList<ClientDataClass> dsClient) {

        Appl.getDatabase().execSQL("delete from OPP");
        Appl.getDatabase().execSQL("delete from CLT");

        for (int i = 0; i < dsOpp.size(); i++) {
            OppDataClass pdc = dsOpp.get(i);
            String CLT_ID = pdc.getClt_id();
            String DT = pdc.getDt();
            String OPP_OPER = pdc.getOpp_oper();
            String OPP_DATE = pdc.getOpp_date();
            String OPP_NAME = pdc.getOpp_name();
            String OPP_SUM = pdc.getOpp_sum();
            String sLite =
                    "INSERT INTO OPP  (CLT_ID, DT, OPP_OPER, OPP_DATE,OPP_NAME,OPP_SUM) " +
                            "VALUES ('" + CLT_ID + "','" + DT + "','" + OPP_OPER + "','" + OPP_DATE + "','" + OPP_NAME + "','" + OPP_SUM + "');";
            Appl.getDatabase().execSQL(sLite);
        }

        for (int i = 0; i < dsClient.size(); i++) {
            ClientDataClass pdc = dsClient.get(i);
            String CLT_ID = pdc.getClt_id();
            String CLT_NAME = pdc.getClt_name();
            String CLT_ADDR = pdc.getClt_addr();
            String CLT_PHONE = pdc.getClt_phone();
            String CLT_DOLG = pdc.getClt_dolg();
            String CLT_AVANS = pdc.getClt_avans();
            String CLT_LAT = pdc.getClt_lat();
            String CLT_LON = pdc.getClt_lon();
            String CLT_ZOOM = pdc.getClt_zoom();
            String sLite =
                    "INSERT INTO CLT  (CLT_ID, CLT_NAME, CLT_ADDR, CLT_PHONE,CLT_DOLG,CLT_AVANS,CLT_LAT,CLT_LON,CLT_ZOOM) " +
                            "VALUES ('" + CLT_ID + "','" + CLT_NAME + "','" + CLT_ADDR + "','" + CLT_PHONE + "','" + CLT_DOLG + "','" +
                            CLT_AVANS + "','"+CLT_LAT+"','"+CLT_LON+"','"+CLT_ZOOM+"');";
            Appl.getDatabase().execSQL(sLite);
        }

        getClientAdapter().changeCursor(getClientRecords(filter));
        setLoadDateTime();
        displayLoadDateTime();

        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
        GlobalBus.getBus().post(ev);
    }


}
