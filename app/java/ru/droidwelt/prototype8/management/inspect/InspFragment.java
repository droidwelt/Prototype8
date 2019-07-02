package ru.droidwelt.prototype8.management.inspect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;

import static android.app.Activity.RESULT_OK;


public class InspFragment extends Fragment {

    private ListView lv;
    public InspCursorAdapter myAdapter;
    public String filter = "";
    private int find_id = -1;
    TextView tv;
    private Activity act;

    public static InspFragment newInstance(Activity a) {
        InspFragment pageFragment = new InspFragment();
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
        View v = inflater.inflate(R.layout.activity_inspect, null);
        tv = v.findViewById(R.id.textView_InspectLastLoad);
        displayLoadDateTime();

        GlobalBus.getBus().register(this);

        String[] from = new String[]{"RVV_NAME", "RVV_KVO1", "RVV_KVO2"};
        int[] to = new int[]{R.id.textView_item_RVV_NAME, R.id.textView_item_RVV_KVO1, R.id.textView_item_RVV_KVO2};

        myAdapter = new InspCursorAdapter(getContext(), this, R.layout.activity_inspect_item,
                new InspCommon().getInspectRecords(filter), from, to);
        lv = v.findViewById(R.id.lv_inspect);
        lv.setOnItemClickListener(viewInspectListener);
        lv.setOnItemLongClickListener(viewInspestLongListener);
        lv.setAdapter(myAdapter);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        FloatingActionButton ins_add_fab = v.findViewById(R.id.ins_add_fab);
        ins_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInspInputActivity(-1);
            }
        });

        FloatingActionButton ins_scanner_fab = v.findViewById(R.id.ins_scanner_fab);
        ins_scanner_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, 0);
            }
        });

        EditText editFilter = v.findViewById(R.id.editText_inspect_Filter);
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
                myAdapter.changeCursor(new InspCommon().getInspectRecords(filter));
                myAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }


    OnItemClickListener viewInspectListener = new OnItemClickListener() {
        // посмотр записи
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            startInspInputActivity(id);
        }
    };


    private void startInspInputActivity(long id) {
        Intent editrec = new Intent(act, InspInputActivity.class);
        editrec.putExtra("_id", (int) (id));
        if (id >= 0) {
            int i_name = myAdapter.getCursor().getColumnIndex("RVV_NAME");
            int i_kvo1 = myAdapter.getCursor().getColumnIndex("RVV_KVO1");
            int i_kvo2 = myAdapter.getCursor().getColumnIndex("RVV_KVO2");
            int i_bar = myAdapter.getCursor().getColumnIndex("RVV_BAR");
            int i_comment = myAdapter.getCursor().getColumnIndex("RVV_COMMENT");
            String s_name = myAdapter.getCursor().getString(i_name);
            String s_kvo1 = myAdapter.getCursor().getString(i_kvo1);
            String s_kvo2 = myAdapter.getCursor().getString(i_kvo2);
            String s_bar = myAdapter.getCursor().getString(i_bar);
            String s_comment = myAdapter.getCursor().getString(i_comment);
            editrec.putExtra("RVV_NAME", s_name);
            editrec.putExtra("RVV_KVO1", s_kvo1);
            editrec.putExtra("RVV_KVO2", s_kvo2);
            editrec.putExtra("RVV_BAR", s_bar);
            editrec.putExtra("RVV_COMMENT", s_comment);
        } else {
            editrec.putExtra("RVV_NAME", "");
            editrec.putExtra("RVV_KVO1", "");
            editrec.putExtra("RVV_KVO2", "");
            editrec.putExtra("RVV_BAR", "");
            editrec.putExtra("RVV_COMMENT", "");
        }
        startActivity(editrec);
    }

    OnItemLongClickListener viewInspestLongListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View v,
                                       int position, long id) {
            int indexst = myAdapter.getCursor().getColumnIndex("RVV_STATE");
            String state = myAdapter.getCursor().getString(indexst);

            if (state.contains("N")) {
                final long id_delete = id;
                new AlertDialog.Builder(act)
                        .setTitle(getString(R.string.dlg_confirm_req))
                        .setMessage(
                                getString(R.string.s_inspest_delete_confirm))
                        .setNegativeButton((getString(R.string.dlg_prg_no)), null)
                        .setPositiveButton((getString(R.string.dlg_prg_yes)),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String sLite = "DELETE FROM RVV WHERE _id=" + id_delete;
                                        Appl.getDatabase().execSQL(sLite);
                                        refreshRecords();
                                    }

                                }).create().show();
            } else {
                int index = myAdapter.getCursor().getColumnIndex("RVV_KVO2");
                String kvo2str = myAdapter.getCursor().getString(index);
                if (kvo2str.isEmpty()) {
                    String sLite = "UPDATE RVV set RVV_KVO2=RVV_KVO1,RVV_STATE='V' where _id= " + id;
                    //Log.i("LOG", "onItemClick sLite=" + sLite);
                    Appl.getDatabase().execSQL(sLite);
                    refreshRecords();
                }
            }
            return true;
        }
    };


    public void refreshRecords() {
        myAdapter.changeCursor(new InspCommon().getInspectRecords(filter));
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inspect, menu);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                findRecordByScanCode(contents);
                if (find_id == -1)
                    new InfoUtils().DisplayToastInfo(getString(R.string.s_tovar_not_found), R.mipmap.ic_fab_scanner, Toast.LENGTH_LONG);
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    public void findRecordByScanCode(String scan_code) {
        //  new InfoUtils().DisplayToastOk(scan_code);
        int index = myAdapter.getCursor().getColumnIndex("RVV_BAR");
        find_id = -1;
        boolean b_find = false;
        int pos = 0;

        if (myAdapter.getCursor().moveToFirst()) {
            String str = myAdapter.getCursor().getString(index);
            if ((str != null) & (scan_code.equals(str))) {
                b_find = true;
                find_id = myAdapter.getCursor().getInt(0);

            } else {
                while (myAdapter.getCursor().moveToNext() & !b_find) {
                    pos = pos + 1;
                    str = myAdapter.getCursor().getString(index);
                    if ((str != null) & (scan_code.equals(str))) {
                        b_find = true;
                        find_id = myAdapter.getCursor().getInt(0);
                    }
                }
            }
        }
        if (b_find) {
            lv.setItemChecked(pos, true);
            lv.smoothScrollToPosition(pos);
            myAdapter.getItemId(pos);
            myAdapter.getCursor().moveToPosition(pos);
        }
    }


    private void displayLoadDateTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sdate = getString(R.string.s_lastload) + sp.getString("ins_dateload", getString(R.string.s_time_not_defined));
        tv.setText(sdate);
    }

    private void setLoadDateTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sdate = DateFormat.format("dd-MM-yyyy HH:mm:ss", new Date()).toString();
        Editor editor = sp.edit();
        editor.putString("ins_dateload", sdate);
        editor.apply();
    }


    public String prepereSQLText() {
        String sSQL = "select  RVV_ID,RVV_NAME,RVV_CENA,RVV_KVO1,RVV_KVO1,RVV_KVO2,RVV_COMMENT,RVV_BAR,RVV_DATEREV,RVV_STATE "
                + "from RVV where RVV_STATE='N' or RVV_STATE='V' ";

        Cursor mc = Appl.getDatabase().rawQuery(sSQL, null);
        int i_name = mc.getColumnIndex("RVV_NAME");
        int i_kvo2 = mc.getColumnIndex("RVV_KVO2");
        int i_comment = mc.getColumnIndex("RVV_COMMENT");
        int i_bar = mc.getColumnIndex("RVV_BAR");
        int i_rvvid = mc.getColumnIndex("RVV_ID");

        StringBuilder eExec = new StringBuilder();  // INSERT
        mc.moveToFirst();
        for (int i = 0; i < mc.getCount(); i++) {
            if (mc.getString(mc.getColumnIndex("RVV_STATE")).equals("N")) {
                String sName = mc.getString(i_name);
                String sKvo = mc.getString(i_kvo2);
                String sComment = mc.getString(i_comment);
                String sBar = mc.getString(i_bar);

                sName = sName.replace("'", "");
                sComment = sComment.replace("'", "");
                sBar = sBar.replace("'", "");

                sName = sName.replace("null", "");
                sComment = sComment.replace("null", "");
                sBar = sBar.replace("null", "");

                eExec.append(" insert into dbo.RVV (RVV_NAME,RVV_KVO1,RVV_COMMENT,RVV_BAR) values ").
                        append("('").append(sName).append("','").
                        append(sKvo).append("','").
                        append(sComment).append("','").
                        append(sBar).append("')  ");
            }
            mc.moveToNext();
        }

        mc.moveToFirst();
        for (int i = 0; i < mc.getCount(); i++) {
            if (mc.getString(mc.getColumnIndex("RVV_STATE")).equals("V")) {

                String sKvo = mc.getString(i_kvo2);
                String sComment = mc.getString(i_comment);
                String sBar = mc.getString(i_bar);

                sComment = sComment.replace("'", "");
                sBar = sBar.replace("'", "");

                sComment = sComment.replace("null", "");
                sBar = sBar.replace("null", "");

                String oneExec =
                        " update dbo.RVV set " +
                                "  RVV_KVO1=" + sKvo + ", " +
                                "  RVV_COMMENT='" + sComment + "', " +
                                "  RVV_BAR='" + sBar + "'" +
                                " where RVV_ID=" + mc.getString(i_rvvid);
                eExec.append(oneExec);
            }

            mc.moveToNext();
        }

        mc.close();
        return eExec.toString();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mi_inspect_transfer:
                if (new NetworkUtils().checkConnection(true)) {
                    startloadInspect();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void startloadInspect() {
        InspPresenter presenter = new InspPresenter();
        presenter.attachView(act, this);
        String sqltext = prepereSQLText();
        presenter.getInspectData(sqltext);
    }


    public void finishLoadInspect(ArrayList<InspDataClass> dsInsp) {
        Appl.getDatabase().execSQL("delete from RVV;");

        for (int i = 0; i < dsInsp.size(); i++) {
            InspDataClass pdc = dsInsp.get(i);
            String RVV_ID = pdc.getRvv_id();
            String RVV_NAME = pdc.getRvv_name();
            String RVV_CENA = pdc.getRvv_cena();
            String RVV_KVO1 = pdc.getRvv_kvo1();
            String RVV_KVO2 = pdc.getRvv_kvo2();
            String RVV_COMMENT = pdc.getRvv_comment();
            String RVV_BAR = pdc.getRvv_bar();
            String sLite =
                    "INSERT INTO RVV  (RVV_ID, RVV_NAME, RVV_CENA, RVV_KVO1,RVV_KVO2,RVV_COMMENT,RVV_BAR,RVV_STATE) " +
                            "VALUES ('" + RVV_ID + "','" + RVV_NAME + "','" + RVV_CENA + "','" + RVV_KVO1 + "','" + RVV_KVO2 +
                            "','" + RVV_COMMENT + "','" + RVV_BAR + "','');";
            Appl.getDatabase().execSQL(sLite);
        }

        myAdapter.changeCursor(new InspCommon().getInspectRecords(filter));
        setLoadDateTime();
        displayLoadDateTime();

        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
        GlobalBus.getBus().post(ev);
    }

    @Subscribe
    public void getMessage(Events.EventsMessage ev) {
        if (ev.getMes_code().equals(Events.EB_INSPECT_REFRESH_RECORDS)) {
            refreshRecords();
        }
    }


}
