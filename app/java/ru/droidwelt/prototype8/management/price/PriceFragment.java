package ru.droidwelt.prototype8.management.price;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;

public class PriceFragment extends Fragment {

    private PriceCursorAdapter myAdapter;
    public  String filter = "";
    private Activity act;
    TextView tv;

    public static PriceFragment newInstance(Activity a) {
        PriceFragment pageFragment = new PriceFragment();
        pageFragment.act = a;
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressWarnings("Annotator")
    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_price_fragment, null);
        tv = v.findViewById(R.id.tv_PriceLastLoad);
        displayLoadDateTime();

        String[] from = new String[]{"PRN_NAME", "PRN_CENA", "PRN_RES", "PRN_OST"};
        int[] to = new int[]{R.id.textView_item_PRN_NAME, R.id.textView_item_PRN_CENA, R.id.textView_item_PRN_RES, R.id.textView_item_PRN_OST};

        myAdapter = new PriceCursorAdapter(getContext(), R.layout.activity_price_item, getPriceRecords(filter), from, to);
        ListView lv = v.findViewById(R.id.lv_price);
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(viewPriceListener);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        EditText editFilter = v.findViewById(R.id.editText_price_Filter);
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
                myAdapter.changeCursor(getPriceRecords(filter));
                myAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    // слушатель событий в ListView
    OnItemClickListener viewPriceListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_price, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mi_price_download:
                if (new NetworkUtils().checkConnection(true)) {
                    startloadPrn();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public  Cursor getPriceRecords(String filter) {
        String sSQL =
                "select  _id, PRN_ID,PRN_NAME,PRN_CENA,PRN_OST,PRN_RES "
                        + "from PRN "
                        + "where PRN_NAME like '%" + filter + "%' "
                        + "order by PRN_NAME ";
        return Appl.getDatabase().rawQuery(sSQL, null);
    }


    public void startloadPrn() {
        PricePresenter presenter = new PricePresenter();
        presenter.attachView(act,this);
        presenter.getPriceData();
    }


    public void finishLoadPrn(ArrayList<PriceDataClass> ds) {
        Appl.getDatabase().execSQL("delete from PRN;");

        for (int i = 0; i < ds.size(); i++) {
            PriceDataClass pdc = ds.get(i);
            String PRN_ID = pdc.getPrn_id();
            String PRN_NAME = pdc.getPrn_name();
            String PRN_CENA = pdc.getPrn_cena();
            String PRN_RES = pdc.getPrn_res();
            String PRN_OST = pdc.getPrn_ost();
            String sLite =
                    "INSERT INTO PRN  (PRN_ID, PRN_NAME, PRN_CENA, PRN_RES,PRN_OST) " +
                            "VALUES ('" + PRN_ID + "','" + PRN_NAME + "','" + PRN_CENA + "','" + PRN_RES + "','" + PRN_OST + "');";
            Appl.getDatabase().execSQL(sLite);
        }

        myAdapter.changeCursor(getPriceRecords(filter));
        setLoadDateTime();
        displayLoadDateTime();

        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
        GlobalBus.getBus().post(ev);
    }

    private void displayLoadDateTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String s = "обновлен " + sp.getString("prn_dateload", getString(R.string.s_time_not_defined));
        tv.setText(s);
    }

    private void setLoadDateTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sdate = DateFormat.format("dd-MM-yyyy HH:mm:ss", new Date()).toString();
        Editor editor = sp.edit();
        editor.putString("prn_dateload", sdate);
        editor.apply();
    }


}
