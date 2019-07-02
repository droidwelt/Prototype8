package ru.droidwelt.prototype8.management.opl;

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

public class OplFragment extends Fragment {

    private OplCursorAdapter myAdapter;
    public String filter = "";
    private Activity act;
    TextView tv;

    public static OplFragment newInstance(Activity a) {
        OplFragment pageFragment = new OplFragment();
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
        View v = inflater.inflate(R.layout.activity_opl_fragment, null);
        tv = v.findViewById(R.id.textView_OplLastLoad);
        displayLoadDateTime();

        String[] from = new String[]{"CLT_NAME", "OPL_DATE", "OPL_NAME", "OPL_SUM"};
        int[] to = new int[]{R.id.text_OPL_CLT, R.id.text_OPL_DATE, R.id.text_OPL_NAME, R.id.text_OPL_SUM};

        myAdapter = new OplCursorAdapter(getContext(), R.layout.activity_opl_item, getOplRecords(filter), from, to);
        ListView lv = v.findViewById(R.id.lv_opl);
        lv.setAdapter(myAdapter);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        EditText editFilter = v.findViewById(R.id.editText_opl_Filter);
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
                myAdapter.changeCursor(getOplRecords(filter));
                myAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_opl, menu);
    }

    public void startloadPrn() {
        OplPresenter presenter = new OplPresenter();
        presenter.attachView(act, this);
        presenter.getOplData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mi_opl_download:
                if (new NetworkUtils().checkConnection(true)) {
                    startloadPrn();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public Cursor getOplRecords(String filter) {
        String sSQL =
                "select  O._id as _id, 0 as ORD,OPL_ID,CLT_NAME, date(OPL_DATE) as OPL_DATE, "
                        + "OPL_NAME,ROUND(OPL_SUM,0) as OPL_SUM "
                        + "from OPL O "
                        + "where CLT_NAME like '%" + filter + "%' "
                        + "union all "
                        + "select  99999,1 as ORD,-1 as OPL_ID,'Итого :' as CLT_NAME,'' as OPL_DATE,'' as OPL_NAME,SUM(OPL_SUM) as OPL_SUM "
                        + "from OPL O "
                        + "where CLT_NAME like '%" + filter + "%' "
                        + "order by ORD,CLT_NAME,OPL_DATE ";
        return Appl.getDatabase().rawQuery(sSQL, null);
    }


    private void displayLoadDateTime() {
        if (tv != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            String s = getString(R.string.s_lastload) + sp.getString("opl_dateload", getString(R.string.s_time_not_defined));
            tv.setText(s);
        }
    }

    private void setLoadDateTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sdate = DateFormat.format("dd-MM-yyyy HH:mm:ss", new Date()).toString();
        Editor editor = sp.edit();
        editor.putString("opl_dateload", sdate);
        editor.apply();
    }


    public void finishLoadOpl(ArrayList<OplDataClass> ds) {

        Appl.getDatabase().execSQL("delete from OPL");

        for (int i = 0; i < ds.size(); i++) {
            OplDataClass pdc = ds.get(i);
            String OPL_ID = pdc.getOpl_id();
            String CLT_NAME = pdc.getClt_name();
            String OPL_DATE = pdc.getOpl_date();
            String OPL_NAME = pdc.getOpl_name();
            Float OPL_SUM = pdc.getOpl_sum();
            String sLite =
                    "INSERT INTO OPL  (OPL_ID,CLT_NAME, OPL_NAME, OPL_SUM, OPL_DATE) " +
                            "VALUES ('" + OPL_ID + "','" + CLT_NAME + "','" + OPL_NAME + "','" + OPL_SUM + "','" + OPL_DATE + "');";
            Appl.getDatabase().execSQL(sLite);
        }

        myAdapter.changeCursor(getOplRecords(filter));
        setLoadDateTime();
        displayLoadDateTime();

        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
        GlobalBus.getBus().post(ev);
    }


}
