package ru.droidwelt.prototype8.msa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.main.PrefActivity;
import ru.droidwelt.prototype8.main.VerifyDbActivity;
import ru.droidwelt.prototype8.msa.fiodev.FioDevListPresenter;
import ru.droidwelt.prototype8.msa.loadrecord.LoadRecordListPresenter;
import ru.droidwelt.prototype8.msa.saverecord.SaveRecordListLoader;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.login.LoginLoader;


public class MsaMainActivity extends AppCompatActivity {

    TextView tv_mes_record_count;
    String myfilter = "";
    ImageButton ib_msa_filter_lbl, ib_msa_filter_clear;
    EditText et_msa_text_filter;
    LinearLayoutManager mLayoutManager;
    List<MsaMainDataStructure> list_msa;
    public RecyclerView mRecyclerView;
    public MsaMainRecyclerAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String filter_lbl = "";

    private FioDevListPresenter fioDevListPresenter;
    private LoadRecordListPresenter loadRecordListPresenter;

    MenuItem mi_msa_new, mi_msa_receive_all, mi_msa_send_all, mi_msa_delete_all, mi_mes_move_all_to_inbox, mi_mes_move_all_to_newbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_msamain);
        drawLayoutMain();

        GlobalBus.getBus().register(this);

        list_msa = new ArrayList<>();
        tv_mes_record_count = findViewById(R.id.tv_msa_record_count);
        ib_msa_filter_lbl = findViewById(R.id.ib_msa_filter_lbl);
        ib_msa_filter_lbl.setOnClickListener(onClickButtonListtiner);
        ib_msa_filter_lbl.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer(filter_lbl, 1));
        ib_msa_filter_clear = findViewById(R.id.ib_msa_filter_clear);
        ib_msa_filter_clear.setOnClickListener(onClickButtonListtiner);

        FloatingActionButton fab_msa_add = findViewById(R.id.fab_msa_add);
        if (new PrefUtils().getMesFabKey()) {
            fab_msa_add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    createNewMessage();
                }
            });
        } else {
            fab_msa_add.setX(10000);
        }

        new PrefUtils().getIMSA_MODE();
        //  new MainUtils().loadFioArray();

        mRecyclerView = findViewById(R.id.rv_msamain);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MsaMainRecyclerAdapter();
        mAdapter.setMsaMainActivity(this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = findViewById(R.id.msa_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        displayTitleOnBar();
        refreshRecords();
        if ((Appl.MSA_MODEVIEW == 2) & (mAdapter.getItemCount() == 0))
            requestloadInspect(true);

        et_msa_text_filter = findViewById(R.id.et_msa_text_filter);
        et_msa_text_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myfilter = (s.toString()).replace(" ", "");
                refreshRecords();
            }
        });

        verifyOldMessages();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new PrefUtils().animateFinish(this);
    }

    private void drawLayoutMain() {
        LinearLayout msa_container = findViewById(R.id.msa_container);
        new PrefUtils().getAppStyle();
        if (Appl.APP_STYLE.equalsIgnoreCase("F")) {
            msa_container.setBackground(getDrawable(R.color.browser_actions_bg_grey));
        } else {
            msa_container.setBackground(getDrawable(R.drawable.list_nav_bar));
        }
    }


    protected void displayTitleOnBar() {
        switch (Appl.MSA_MODEVIEW) {
            case 0:
                setTitle(getString(R.string.s_message_draft));
                break;
            case 1:
                setTitle(getString(R.string.s_message_tmpl));
                break;
            case 2:
                setTitle(getString(R.string.s_message_newbox));
                break;
            case 22:
                setTitle(getString(R.string.s_message_inbox));
                break;
            case 3:
                setTitle(getString(R.string.s_message_sentbox));
                break;
            case 4:
                setTitle(getString(R.string.s_message_outbox));
                break;
            case 10:
                setTitle(getString(R.string.s_message_favorbox));
                break;
            default:
                setTitle(getString(R.string.s_message_box));
                break;
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(getMyBarDrawable());

        mSwipeRefreshLayout.setEnabled(Appl.MSA_MODEVIEW == 2);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(false);
            requestloadInspect(true);
        }
    };


    public Drawable getMyBarDrawable() {
        if (Appl.MSA_MODEVIEW == 0) return new ColorDrawable(Appl.BarColor_0);
        if (Appl.MSA_MODEVIEW == 2) return new ColorDrawable(Appl.BarColor_2);
        if (Appl.MSA_MODEVIEW == 22) return new ColorDrawable(Appl.BarColor_22);
        if (Appl.MSA_MODEVIEW == 3) return new ColorDrawable(Appl.BarColor_3);
        if (Appl.MSA_MODEVIEW == 4) return new ColorDrawable(Appl.BarColor_4);
        if (Appl.MSA_MODEVIEW == 10) return new ColorDrawable(Appl.BarColor_10);
        return new ColorDrawable(Color.BLACK);
    }


    public void refreshRecords() {
        getMesRecords(myfilter);
        mAdapter.notifyDataSetChanged();

        try {
            if (mRecyclerView.getAdapter().getItemCount() > 0) {
                if (Appl.MSA_POS >= 0) {
                    if (mRecyclerView.getAdapter().getItemCount() > Appl.MSA_POS)
                        mRecyclerView.scrollToPosition(Appl.MSA_POS);
                    else
                        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String s = "" + mAdapter.getItemCount();
        tv_mes_record_count.setText(s);
    }


    public void getMesRecords(String filter) {
        String sFilterLBL = "";
        if (!filter_lbl.equals("")) sFilterLBL = " and MSA_LBL='" + filter_lbl + "' ";
        String sSQL;
        //  String sMode = "" + Appl.MSA_MODEVIEW;
        //  if (Appl.MSA_MODEVIEW == 22) sMode = "2,22";
        if (Appl.MSA_MODEVIEW != 0) {
            sSQL = "select  MSA_ID,MSA_TITLE,MSA_CLR,MSA_LBL,"
                    + "MSA_TEXT,A.FIO_ID,MSA_FILETYPE,MSA_FILENAME,"
                    + "FIO_NAME,MSA_DATE,length(MSA_IMAGE) as IMAGESIZE "
                    + "from MSA A "
                    + "left join FIO F on A.FIO_ID=F.FIO_ID "
                    + "where MSA_STATE=" + Appl.MSA_MODEVIEW
                    + " and ((MSA_TITLE like '%" + filter + "%') or (FIO_NAME like '%" + filter + "%')) "
                    + sFilterLBL
                    + "order by MSA_DATE desc ";
        } else {
            sSQL = "select MSA_ID,MSA_TITLE,MSA_CLR,MSA_LBL,"
                    + " MSA_TEXT,A.FIO_ID,MSA_FILETYPE,MSA_FILENAME,"
                    + " FIO_NAME,MSA_DATE,length(MSA_IMAGE) as IMAGESIZE "
                    + " from MSA A "
                    + " left join FIO F on A.FIO_ID=F.FIO_ID "
                    + " where MSA_STATE in (" + Appl.MSA_MODEVIEW + ") "
                    + " order by MSA_DATE desc ";
        }
        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);

        list_msa.clear();
        c.moveToFirst();
        int index_MSA_ID = c.getColumnIndex("MSA_ID");
        int index_MSA_TITLE = c.getColumnIndex("MSA_TITLE");
        int index_MSA_CLR = c.getColumnIndex("MSA_CLR");
        int index_MSA_LBL = c.getColumnIndex("MSA_LBL");
        int index_MSA_TEXT = c.getColumnIndex("MSA_TEXT");
        int index_FIO_ID = c.getColumnIndex("FIO_ID");
        int index_MSA_FILETYPE = c.getColumnIndex("MSA_FILETYPE");
        int index_MSA_FILENAME = c.getColumnIndex("MSA_FILENAME");
        int index_FIO_NAME = c.getColumnIndex("FIO_NAME");
        int index_MSA_DATE = c.getColumnIndex("MSA_DATE");
        int index_IMAGESIZE = c.getColumnIndex("IMAGESIZE");
        while (!c.isAfterLast()) {
            MsaMainDataStructure mes = new MsaMainDataStructure();
            mes.msa_id = c.getString(index_MSA_ID);
            mes.msa_title = c.getString(index_MSA_TITLE);
            mes.msa_clr = c.getString(index_MSA_CLR);
            mes.msa_lbl = c.getString(index_MSA_LBL);
            mes.msa_text = c.getString(index_MSA_TEXT);
            mes.fio_id = c.getInt(index_FIO_ID);
            mes.msa_filetype = c.getString(index_MSA_FILETYPE);
            mes.msa_filename = c.getString(index_MSA_FILENAME);
            mes.fio_name = c.getString(index_FIO_NAME);
            mes.msa_date = c.getString(index_MSA_DATE);
            mes.imagesize = c.getInt(index_IMAGESIZE);
            list_msa.add(mes);
            c.moveToNext();
        }
        c.close();
    }


    OnClickListener onClickButtonListtiner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.ib_msa_filter_lbl:
                    Intent intentfilter = new Intent(MsaMainActivity.this, MsaFilterLblActivity.class);
                    startActivity(intentfilter);
                    break;

                case R.id.ib_msa_filter_clear:
                    et_msa_text_filter.setText("");
                    filter_lbl = "";
                    ib_msa_filter_lbl.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer(filter_lbl, 1));
                    refreshRecords();
                    break;

                default:
                    break;
            }
        }
    };


    // подключение меню----------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_msa, menu);
        mi_msa_new = menu.findItem(R.id.mi_mes_new);
        if (new PrefUtils().getMesFabKey()) mi_msa_new.setVisible(false);
        mi_msa_receive_all = menu.findItem(R.id.mi_mes_receive_all);
        mi_msa_send_all = menu.findItem(R.id.mi_mes_send_all);
        mi_msa_delete_all = menu.findItem(R.id.mi_mes_delete_all);
        mi_mes_move_all_to_inbox = menu.findItem(R.id.mi_mes_move_all_to_inbox);
        mi_mes_move_all_to_newbox = menu.findItem(R.id.mi_mes_move_all_to_newbox);
        set_menu_item_enabled();
        return true;
    }

    public void set_menu_item_enabled() {
        mi_msa_receive_all.setVisible(Appl.MSA_MODEVIEW == 2);
        mi_msa_send_all.setVisible(Appl.MSA_MODEVIEW == 4);
        mi_msa_delete_all.setVisible(true);
        mi_mes_move_all_to_inbox.setVisible(Appl.MSA_MODEVIEW == 2);
        mi_mes_move_all_to_newbox.setVisible(Appl.MSA_MODEVIEW == 22);
    }


    public void createNewMessage() {
        Appl.MSA_MODEVIEW = 0;
        new PrefUtils().setIMSA_MODE();
        displayTitleOnBar();
        Appl.MSA_ID = new StringUtils().generate_GUID();
        Appl.MSA_MODEEDIT = 0;
        new MsaUtilsSQLite().create_new_draft();
        refreshRecords();
        set_menu_item_enabled();
        Intent mesCreate = new Intent(this, MsaEditActivity.class);
        startActivityForResult(mesCreate, Appl.EXIT_CODE_MSAEDIT);
        new PrefUtils().animateStart(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // новое сообщение
            case R.id.mi_mes_new:
                createNewMessage();
                return true;

		/*	Типы в MSA_STATE:   ( СОСТОЯНИЕ )
               0  - черновик
		       1 -  шаблон
		       2 -  новые
		       22 -  входящие
		       3 -  отправленные
		       4 -  исходящие
		       5 -  удаленные*/

            //  исходящие
            case R.id.mi_mes_outbox:
                Appl.MSA_MODEVIEW = 4;
                new PrefUtils().setIMSA_MODE();
                displayTitleOnBar();
                refreshRecords();
                set_menu_item_enabled();
                return true;

            //  отправленные
            case R.id.mi_mes_sentbox:
                Appl.MSA_MODEVIEW = 3;
                new PrefUtils().setIMSA_MODE();
                displayTitleOnBar();
                refreshRecords();
                set_menu_item_enabled();
                return true;

            // новые
            case R.id.mi_mes_newbox:
                Appl.MSA_MODEVIEW = 2;
                new PrefUtils().setIMSA_MODE();
                displayTitleOnBar();
                refreshRecords();
                set_menu_item_enabled();
                return true;

            // входящие
            case R.id.mi_mes_inbox:
                Appl.MSA_MODEVIEW = 22;
                new PrefUtils().setIMSA_MODE();
                displayTitleOnBar();
                refreshRecords();
                set_menu_item_enabled();
                return true;

            // черновики
            case R.id.mi_mes_draft:
                Appl.MSA_MODEVIEW = 0;
                new PrefUtils().setIMSA_MODE();
                displayTitleOnBar();
                refreshRecords();
                set_menu_item_enabled();
                return true;

            // избранное
            case R.id.mi_mes_favbox:
                Appl.MSA_MODEVIEW = 10;
                new PrefUtils().setIMSA_MODE();
                displayTitleOnBar();
                refreshRecords();
                set_menu_item_enabled();
                return true;

            // послать все записи
            case R.id.mi_mes_send_all: {
                if (new NetworkUtils().checkConnection(true)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogColorButton);
                    builder.setTitle(getString(R.string.dlg_confirm_req));
                    builder.setMessage(getString(R.string.s_message_send_confirm_all));
                    builder.setNegativeButton(getString(R.string.dlg_prg_no), null);
                    builder.setPositiveButton((getString(R.string.dlg_prg_yes)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new SaveRecordListLoader().setSaveRecordAllList();
                                }
                            });
                    final AlertDialog dlg = builder.create();
                    dlg.show();
                }
            }
            return true;

            // удалить все записи
            case R.id.mi_mes_delete_all: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogColorButton);
                builder.setTitle(getString(R.string.dlg_confirm_req));
                builder.setMessage(getString(R.string.s_message_delete_confirm_all));
                builder.setNegativeButton(getString(R.string.dlg_prg_no), null);
                builder.setPositiveButton((getString(R.string.dlg_prg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Appl.getDatabase().rawQuery("PRAGMA journal_mode = 'MEMORY'; ", null);
                                Appl.getDatabase().rawQuery("PRAGMA synchronous = 'OFF'; ", null);
                                String sSQL = "delete from MSA where MSA_STATE=" + Appl.MSA_MODEVIEW + "  ;";
                                Appl.getDatabase().execSQL(sSQL);
                                Appl.getDatabase().rawQuery("PRAGMA journal_mode = 'DELETE'; ", null);
                                Appl.getDatabase().rawQuery("PRAGMA synchronous = 'ON'; ", null);

                                Events.EventsMessage ev = new Events.EventsMessage();
                                ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
                                GlobalBus.getBus().post(ev);
                            }

                        });
                final AlertDialog dlg = builder.create();
                dlg.show();
            }
            return true;

            // Переместить все во входящие
            case R.id.mi_mes_move_all_to_inbox: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogColorButton);
                builder.setTitle(getString(R.string.dlg_confirm_req));
                builder.setMessage(getString(R.string.s_message_move_all_to_inbox_confirm));
                builder.setNegativeButton(getString(R.string.dlg_prg_no), null);
                builder.setPositiveButton((getString(R.string.dlg_prg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MsaUtilsSQLite().change_all_record_state_SQLite(2, 22);
                                refreshRecords();
                            }
                        });
                final AlertDialog dlg = builder.create();
                dlg.show();
            }
            return true;

            // Переместить все в новын
            case R.id.mi_mes_move_all_to_newbox: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogColorButton);
                builder.setTitle(getString(R.string.dlg_confirm_req));
                builder.setMessage(getString(R.string.s_message_move_all_to_newbox_confirm));
                builder.setNegativeButton(getString(R.string.dlg_prg_no), null);
                builder.setPositiveButton((getString(R.string.dlg_prg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MsaUtilsSQLite().change_all_record_state_SQLite(22, 2);
                                refreshRecords();
                            }
                        });
                final AlertDialog dlg = builder.create();
                dlg.show();
            }
            return true;

            // принять все
            case R.id.mi_mes_receive_all:
                requestloadInspect(false);
                return true;

            // Аватар
            case R.id.mi_mes_my_avatar:
                if (new NetworkUtils().checkConnection(true)) {
                    Intent intentava = new Intent(MsaMainActivity.this, MsaAvatarActivity.class);
                    startActivityForResult(intentava, Appl.EXIT_CODE_AVATAR);
                    new PrefUtils().animateStart(this);
                }
                return true;

            // Обслуживание базы
            case R.id.mi_mes_verifydb:
                Intent intentvfy = new Intent(MsaMainActivity.this, VerifyDbActivity.class);
                startActivity(intentvfy);
                return true;

            // Настройки
            case R.id.mi_mes_my_setting:
                Intent intentsetting = new Intent(MsaMainActivity.this, PrefActivity.class);
                intentsetting.putExtra("MODE_GLOBAL", "N");
                startActivity(intentsetting);
                new PrefUtils().animateStart(MsaMainActivity.this);
                return true;

            // Список респондентов
            case R.id.mi_mes_fio_list_load:
                requestDevFioListData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        switch (requestCode) {
            case Appl.EXIT_CODE_MSAEDIT:
                set_menu_item_enabled();
                refreshRecords();
                break;

            case Appl.EXIT_CODE_LOGIN:
                if (!(returnedIntent == null)) {
                    String code = returnedIntent.getStringExtra("code");
                    new LoginLoader().loginUser(this, code);
                }
                break;

            case Appl.EXIT_CODE_AVATAR:
                requestDevFioListData();
                break;
        }

    }


    public void verifyOldMessages() {
        int old = new PrefUtils().getDaysDownLoad();
        if (old <= 0) old = 1;
        String sSQL =
                " select " +
                        " (select count(*) from MSA where MSA_STATE=0 and MSA_DATE<date('now','-" + old + " day')) as KVO_0," +
                        " (select count(*) from MSA where MSA_STATE=2 and MSA_DATE<date('now','-" + old + " day')) as KVO_2," +
                        " (select count(*) from MSA where MSA_STATE=3 and MSA_DATE<date('now','-" + old + " day')) as KVO_3," +
                        " (select count(*) from MSA where MSA_STATE=4 and MSA_DATE<date('now','-" + old + " day')) as KVO_4 ";
        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
        c.moveToFirst();
        int kvo_0 = c.getInt(c.getColumnIndex("KVO_0"));
        int kvo_2 = c.getInt(c.getColumnIndex("KVO_2"));
        int kvo_3 = c.getInt(c.getColumnIndex("KVO_3"));
        int kvo_4 = c.getInt(c.getColumnIndex("KVO_4"));
        c.close();
        if ((kvo_0 + kvo_2 + kvo_3 + kvo_4) > 0) {
            Intent intentvfy = new Intent(this, VerifyDbActivity.class);
            startActivity(intentvfy);
        }
    }


    public void setImageSize(String MSA_ID, int IMAGESIZE) {
        for (int i = 0; i < list_msa.size(); i++) {
            if (MSA_ID.equals(list_msa.get(i).msa_id)) {
                // MsaMainDataStructure mes = new MsaMainDataStructure();
                MsaMainDataStructure mes;
                mes = list_msa.get(i);
                mes.imagesize = IMAGESIZE;
                list_msa.set(i, mes);
                return;
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(fioDevListPresenter == null))
            fioDevListPresenter.detachView();
        if (!(loadRecordListPresenter == null))
            loadRecordListPresenter.detachView();
        GlobalBus.getBus().unregister(this);
        new NotificationUtils().cancelNotification(0);
    }

    //  запрос данных
    public void requestDevFioListData() {
        if (new NetworkUtils().checkConnection(true)) {
            fioDevListPresenter = new FioDevListPresenter();
            fioDevListPresenter.attachView(this);
            fioDevListPresenter.getFioDevListData();
        }
    }

    // вызывается из Presenter по получении ответа
    public void displayFioDevList() {
        new InfoUtils().DisplayToastOk(getString(R.string.s_respondent_list_reloaded));
        refreshRecords();
    }

    //  запрос данных
    public void requestloadInspect(boolean recordlimit) {
        loadRecordListPresenter = new LoadRecordListPresenter();
        loadRecordListPresenter.attachView(this);
        loadRecordListPresenter.getRecordListData();
    }


    @Subscribe
    public void getMessage(Events.EventsMessage ev) {
        if (ev.getMes_code().equals(Events.EB_MSAMAIN_REFRESH_RECORDS)) {
            ib_msa_filter_lbl.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer(filter_lbl, 1));
            refreshRecords();
        }

        if (ev.getMes_code().equals(Events.EB_MSAMAIN_DATASET_CHANGED)) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        if (ev.getMes_code().equals(Events.EB_MSAMAIN_ITEM_CHANGED)) {
            mAdapter.notifyItemChanged(ev.getMes_int());
        }

        if (ev.getMes_code().equals(Events.EB_MSAMAIN_SET_IMAGESIZE)) {
            setImageSize(ev.getMes_str(), ev.getMes_int());
        }

        if (ev.getMes_code().equals(Events.EB_MSA_COPY_ONE_RECORD_TO_DRAFT)) {
            copyOneRecordToDraft(ev.getMes_str());
        }

        if (ev.getMes_code().equals(Events.EB_MSA_DELETE_ONE_RECORD)) {
            deleteOneRecord(ev.getMes_str());
        }

        if (ev.getMes_code().equals(Events.EB_MSAMAIN_SET_FILTER_LBL)) {
            filter_lbl = ev.getMes_str();
        }
    }


    public void copyOneRecordToDraft(String msa_id) {
        Appl.MSA_ID = new MsaUtilsSQLite().copy_one_to_folder_SQLite(msa_id, 0);
        Appl.MSA_MODEEDIT = 0;
        Intent mesEdit = new Intent(Appl.getContext(), MsaEditActivity.class);
        this.startActivityForResult(mesEdit, 1001);
        new PrefUtils().animateStart(this);
    }

    public void deleteOneRecord(final String msa_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Appl.getContext().getString(R.string.dlg_confirm_req));
        builder.setMessage(Appl.getContext().getString(R.string.s_message_delete_confirm_one));
        builder.setNegativeButton(Appl.getContext().getString(R.string.dlg_prg_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setPositiveButton((Appl.getContext().getString(R.string.dlg_prg_yes)),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new MsaUtilsSQLite().delete_selected_SQLite(msa_id);
                        refreshRecords();
                    }
                });
        builder.create();
        builder.show();
    }


}
