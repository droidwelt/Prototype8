package ru.droidwelt.prototype8.choice;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.fiodev.FioDevListPresenter;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.login.LoginLoader;


public class ChoiceFioActivity extends AppCompatActivity {

    private TextView tv_choicefio_sendlist;
    List<ChoiceFioStructure> list_fio;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    private ChoiceFioRecyclerAdapter mAdapter;
    private FioDevListPresenter  fioDevListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicefio);
        setTheme(R.style.AppTheme_Dialog);
        setTitle(getString(R.string.s_message_choice_template_fio));
        ImageButton ib_choicefio_exit = findViewById(R.id.ib_choicefio_exit);
        ib_choicefio_exit.setOnClickListener(oclBtnOk);
        ImageButton ib_choicefio_refresh = findViewById(R.id.ib_choicefio_refresh);
        ib_choicefio_refresh.setOnClickListener(oclBtnOk);
        tv_choicefio_sendlist = findViewById(R.id.tv_choicefio_sendlist);
        tv_choicefio_sendlist.setText(new MsaUtilsSQLite().message_get_send_list());

        list_fio = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rv_choicefio);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getFioRecords();
        mAdapter =  new ChoiceFioRecyclerAdapter();
        mAdapter.setInspActivity (this);
        mRecyclerView.setAdapter(mAdapter);
    }


    public  void setFilChecked(String FIO_ID, String FIO_CH) {
        String sSQL;
        if (FIO_CH.equals(""))
            FIO_CH = "1";
        else
            FIO_CH = "";
        if (FIO_CH.equals("1"))
            sSQL = "insert into MSB (MSA_ID,FIO_ID) values ('" + Appl.MSA_ID + "'," + FIO_ID + ");";
        else
            sSQL = "delete from MSB  where MSA_ID='" + Appl.MSA_ID + "' and FIO_ID=" + FIO_ID + " ;";
        Appl.getDatabase().execSQL(sSQL);
        getFioRecords();
        mAdapter.notifyDataSetChanged();
        tv_choicefio_sendlist.setText(new MsaUtilsSQLite().message_get_send_list());
        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MSAEDIT_SET_SEND_LIST);
        GlobalBus.getBus().post(ev);
    }


    OnClickListener oclBtnOk = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.ib_choicefio_exit:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;

                case R.id.ib_choicefio_refresh:
                    requestDevFioListData();
                    break;

                default:
                    break;
            }
        }
    };


    public  void getFioRecords() {
        String sSQL = "select F._id as _id,F.FIO_ID,F.FIO_TP," +
                " FIO_NAME,FIO_IMAGE,FIO_TP, FIO_SUBNAME, " +
                " case when (select count(*) from MSB B " +
                " where B.MSA_ID='" + Appl.MSA_ID + "' and B.FIO_ID=F.FIO_ID)>0 " +
                " then '1' else '' end as FIO_CH " +
                " from FIO F" +
                " where FIO_TP in (1,2)" +
                "order by FIO_TP,FIO_NAME";
        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);

        list_fio.clear();
        c.moveToFirst();
        int index_FIO_ID = c.getColumnIndex("FIO_ID");
        int index_FIO_NAME = c.getColumnIndex("FIO_NAME");
        int index_FIO_SUBNAME = c.getColumnIndex("FIO_SUBNAME");
        int index_FIO_CH = c.getColumnIndex("FIO_CH");
        int index_FIO_TP = c.getColumnIndex("FIO_TP");
        int index_FIO_IMAGE = c.getColumnIndex("FIO_IMAGE");

        while (!c.isAfterLast()) {
            ChoiceFioStructure mes = new ChoiceFioStructure();
            mes.fio_id = c.getString(index_FIO_ID);
            mes.fio_name = c.getString(index_FIO_NAME);
            mes.fio_subname = c.getString(index_FIO_SUBNAME);
            mes.fio_tp = c.getInt(index_FIO_TP);
            mes.fio_choice = c.getString(index_FIO_CH);
            byte[] fio_img = c.getBlob(index_FIO_IMAGE);
            if (fio_img != null)
                mes.fio_img = BitmapFactory.decodeByteArray(fio_img, 0, fio_img.length);

            list_fio.add(mes);
            c.moveToNext();
        }
        c.close();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(fioDevListPresenter == null))
            fioDevListPresenter.detachChoice();
        GlobalBus.getBus().unregister(this);
    }

    //  запрос данных
    public void requestDevFioListData() {
        if (new NetworkUtils().checkConnection(true)) {
            fioDevListPresenter = new FioDevListPresenter();
            fioDevListPresenter.attachChoice(this);
            fioDevListPresenter.getFioDevListData();
        }
    }

    // вызывается из Presenter по получении ответа
    public void displayFioDevList() {
        getFioRecords();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch (requestCode) {

            case Appl.EXIT_CODE_LOGIN:
                if (!(returnedIntent == null)) {
                    String code = returnedIntent.getStringExtra("code");
                    loginUser(code);
                }
                break;
        }
    }

    public void loginUser(String code) {
        new LoginLoader().loginUser(this, code);
    }


}