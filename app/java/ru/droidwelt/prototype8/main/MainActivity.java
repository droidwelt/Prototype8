package ru.droidwelt.prototype8.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.droidwelt.prototype8.BuildConfig;
import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.client.ClientActivity;
import ru.droidwelt.prototype8.contact.ContactActivity;
import ru.droidwelt.prototype8.deviceregister.DeviceRegisterActivity;
import ru.droidwelt.prototype8.gallery.GalleryActivity;
import ru.droidwelt.prototype8.management.inspect.InspFragment;
import ru.droidwelt.prototype8.management.ManagementActivity;
import ru.droidwelt.prototype8.model.DataStructure;
import ru.droidwelt.prototype8.model.StateListClass;
import ru.droidwelt.prototype8.msa.MsaMainActivity;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.management.opl.OplFragment;
import ru.droidwelt.prototype8.management.price.PriceFragment;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.DatabaseHelper;
import ru.droidwelt.prototype8.utils.common.FcmUtils;
import ru.droidwelt.prototype8.utils.common.FileUtils;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.MapsUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.login.LoginActivity;
import ru.droidwelt.prototype8.utils.login.LoginLoader;
import ru.droidwelt.prototype8.utils.version.AppActualBodyClass;
import ru.droidwelt.prototype8.utils.version.AppActualBodyLoader;
import ru.droidwelt.prototype8.utils.version.AppActualVersionClass;
import ru.droidwelt.prototype8.utils.version.AppVerifyVersionLoader;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    DatabaseHelper databaseHelper;

    NavigationView navigationView;
    Toolbar main_toolbar;
    public static final int RequestPermissionCode = 1;
    private boolean mastRestartedOnResume = false;
    List<MainDataStructure> list_main;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    MainRecyclerAdapter mAdapter;
    final private String FILENAMETOUPDATE = "PROTO8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appl.getComponent().inject(this);

        new PrefUtils().getAppStyle();
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        GlobalBus.getBus().register(this);

        new PrefUtils().defineMainRecyclerHeight(this);

        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        main_toolbar.setTitle(new MainUtils().getMainTitle());
        main_toolbar.setBackgroundColor(Appl.BarColor_main);

        drawLayoutMain();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /* Called when a drawer has settled in a completely closed state. */
           /* public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }*/

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                ImageView iv_main_avatar = findViewById(R.id.iv_main_avatar);
                iv_main_avatar.setImageDrawable(new MsaUtilsSQLite().getMyAvatar());
                TextView tv_avatar = findViewById(R.id.tv_main_avatar);
                tv_avatar.setText(Appl.FIO_NAME);
                TextView tv_avatar2 = findViewById(R.id.tv_main_avatar2);
                tv_avatar2.setText(Appl.FIO_SUBNAME);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list_main = new ArrayList<>();
        loadList();

        mRecyclerView = findViewById(R.id.rv_main);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        //   mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MainRecyclerAdapter();
        mAdapter.setMainActivity(this);
        mRecyclerView.setAdapter(mAdapter);
        new FcmUtils().getDeviceFCM(this);

        if (checkMyPremissoins()) {
            startMyActions();
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
        }

        new FcmUtils().verityFcmCode();
    }


    public List<MainDataStructure> getListMain() {
        return list_main;
    }

    private void drawLayoutMain() {
        LinearLayout ly_main = findViewById(R.id.ly_main);
        new PrefUtils().getAppStyle();
        if (Appl.APP_STYLE.equalsIgnoreCase("F")) {
            ly_main.setBackground(null);
        } else {
            ly_main.setBackground(getDrawable(R.drawable.list_nav_bar));
        }
    }

    @Override
    protected void onResume() {
        if (mastRestartedOnResume) {
            mastRestartedOnResume = false;
            finish();
            startActivity(getIntent());
        }
        super.onResume();
    }


    protected boolean checkMyPremissoins() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                !((ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission & ReadExternalStoragePermission & WriteExternalStoragePermission) {
                        startMyActions();
                    } else {
                        finish();
                    }
                }
                break;
        }
    }


    protected void startMyActions() {
        databaseHelper.openDataBase();
        new PrefUtils().getFIOData();
        Intent intenstart = new Intent(this, StartActivity.class);
        startActivity(intenstart);
        refreshMainMenu();
    }


    // подключение меню----------------------------------------
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    } */

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_main_register_gcm:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } */


    //защита от закрытия по Back Обработка нажатия
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (Appl.FIO_ID == -1) {
                    MainActivity.super.onBackPressed();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogColorButton);
                    builder.setTitle(getString(R.string.dlg_prg_exit));
                    builder.setMessage(getString(R.string.dlg_prg_confirm));
                    builder.setNegativeButton((getString(R.string.dlg_prg_no)), null);
                    builder.setPositiveButton((getString(R.string.dlg_prg_yes)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Appl.FIO_ID = -1;
                            MainActivity.super.onBackPressed();
                        }
                    });
                    final AlertDialog dlg = builder.create();
                    dlg.show();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mi_main_logput:

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogColorButton);
                builder.setTitle(getString(R.string.dlg_confirm_req));
                builder.setMessage(getString(R.string.s_confirm_logout));
                builder.setNegativeButton(getString(R.string.dlg_prg_no), null);
                builder.setPositiveButton((getString(R.string.dlg_prg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Appl.FIO_ID = -1;
                                Appl.FIO_NAME = getString(R.string.action_no_user);
                                Appl.FIO_SUBNAME = "";
                                Appl.FIO_SUBNAME = "";
                                new PrefUtils().setFIOData();
                                Appl.FIO_KEYTOKEN = "";
                                new PrefUtils().setFio_keyToken("");
                                finish();
                            }

                        });
                builder.create();
                builder.show();
                break;

            case R.id.mi_main_about:

                Intent intentabout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentabout);
                break;

            case R.id.mi_main_setting:

                startPrefActivity ();
                break;

            case R.id.mi_main_register_db:

                if (new NetworkUtils().checkConnection(true)) {
                    Intent intentregdb = new Intent(MainActivity.this, DeviceRegisterActivity.class);
                    startActivity(intentregdb);
                }
                break;

            case R.id.mi_main_check_update:
                if (new PrefUtils().verifyRootAddress()) {
                    verifyAppInit();
                }
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public  void  startPrefActivity () {
        Intent intentsetting = new Intent(MainActivity.this, PrefActivity.class);
        intentsetting.putExtra("MODE_GLOBAL", "Y");
        startActivity(intentsetting);
        new PrefUtils().animateStart(MainActivity.this);
    }

    public void startActivity(String MAIN_ID) {
        if (MAIN_ID.equalsIgnoreCase("1")) {
            Intent intent = new Intent(this, ClientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            new PrefUtils().animateStart(this);
        }

        if (MAIN_ID.equalsIgnoreCase("5")) {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            new PrefUtils().animateStart(this);
        }

        if (MAIN_ID.equalsIgnoreCase("6")) {
            if (Appl.FIO_ID >= 0) {
                Intent intent = new Intent(this, MsaMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, Appl.EXIT_CODE_MSA);
                new PrefUtils().animateStart(this);
            } else {
                enterLogin();
            }
        }

        if (MAIN_ID.equalsIgnoreCase("7")) {
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            new PrefUtils().animateStart(this);
        }

        if (MAIN_ID.equalsIgnoreCase("8")) {
            Intent intent = new Intent(this, ManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            new PrefUtils().animateStart(this);
        }

        if (MAIN_ID.equalsIgnoreCase("9")) {
            new MapsUtils().showMapSimple(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch (requestCode) {

            case Appl.EXIT_CODE_MSA:
                getBoxCount();
                refreshMainMenu();
                break;

            case Appl.EXIT_CODE_LOGIN:
                if (!(returnedIntent == null)) {
                    String code = returnedIntent.getStringExtra("code");
                    loginUser(code);
                }
                break;
        }
    }


    public void refreshMainMenu() {
        loadList();
        for (int i = 0; i <= mAdapter.getItemCount() - 1; i = i + 1) {
            mAdapter.notifyItemChanged(i);
        }
    }


    public String getBoxCount() {
        String s = "";
        if (Appl.getDatabase() != null) {
            String sSQL = " select "
                    + " (select count(*) from  MSA  where MSA_STATE=4) as KVO4,"
                    + " (select count(*) from  MSA  where MSA_STATE=2) as KVO2";
            Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
            c.moveToFirst();
            int cnt4 = c.getInt(0);
            int cnt2 = c.getInt(1);
            c.close();
            if (cnt2 > 0) {
                s = s + "<font color=\"#388E3C\">Новых - " + cnt2 + "</font>";
            }

            if (cnt4 > 0) {
                if (!s.equals("")) s = s + "; ";
                s = s + "<font color=\"#AD1457\">Исходящих - " + cnt4 + "</font>";
            }
        }
        return s;
    }

    protected void loadList() {
        list_main.clear();

        {
            MainDataStructure mes = new MainDataStructure();
            mes.main_id = "6";
            mes.main_title = "Сообщения";
            mes.main_text = getBoxCount();
            mes.main_img = getApplicationContext().getResources().getDrawable(R.mipmap.main_message);
            mes.main_color = "#f9fbe7";
            list_main.add(mes);
        }

        {
            MainDataStructure mes = new MainDataStructure();
            mes.main_id = "5";
            mes.main_title = "Контакты";
            mes.main_img = getApplicationContext().getResources().getDrawable(R.mipmap.main_task);
            mes.main_text = "on-line список контактов";
            mes.main_color = "#FBE9E7";
            list_main.add(mes);
        }

        {
            MainDataStructure mes = new MainDataStructure();
            mes.main_id = "1";
            mes.main_title = "Клиенты";
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            mes.main_text = "обновлено " + sp.getString("clt_dateload", getString(R.string.s_time_not_defined));
            mes.main_img = getApplicationContext().getResources().getDrawable(R.mipmap.main_client);
            mes.main_color = "#FFF8E1";
            list_main.add(mes);
        }

        {
            MainDataStructure mes = new MainDataStructure();
            mes.main_id = "7";
            mes.main_title = "Галерея";
            mes.main_text = "on-line галерея контактов";
            mes.main_img = getApplicationContext().getResources().getDrawable(R.mipmap.main_galery);
            mes.main_color = "#E1F577";
            list_main.add(mes);
        }

        {
            MainDataStructure mes = new MainDataStructure();
            mes.main_id = "8";
            mes.main_title = "Менеджмент";
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            mes.main_text = "платежи, ревизия, прайс";
            mes.main_img = getApplicationContext().getResources().getDrawable(R.mipmap.main_oplat);
            mes.main_color = "#F1F8E9";
            list_main.add(mes);
        }

        {
            MainDataStructure mes = new MainDataStructure();
            mes.main_id = "9";
            mes.main_title = "Карта";
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            mes.main_text = "показывает карту Open Street Map";
            mes.main_img = getApplicationContext().getResources().getDrawable(R.mipmap.main_maps);
            mes.main_color = "#F1F8E9";
            list_main.add(mes);
        }

    }

    //----------------------------------------------------------------------------------------------

    protected void dlgLastVersion() {
        String s = getString(R.string.s_dlg_lastversion_text1) + " " +
                new MainUtils().getVersionCode() + " (" + new MainUtils().getVersionName() + " )\n\n" +
                getString(R.string.s_dlg_lastversion_text2);
        new AlertDialog.Builder(this, R.style.AlertDialogColorButton)
                .setTitle(getString(R.string.s_dlg_confirm_appupdate_title))
                .setMessage(s)
                .setPositiveButton((getString(R.string.s_dlg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).create().show();
    }

    protected void dlgUpdateApp(String newCode, String newName) {
        String s = getString(R.string.s_dlg_confirm_appupdate_text1) + " " +
                new MainUtils().getVersionCode() + " (" + new MainUtils().getVersionName() + " )\n\n" +
                getString(R.string.s_dlg_confirm_appupdate_text2) + " " +
                newCode + " (" + newName + " )\n\n" +
                getString(R.string.s_dlg_confirm_appupdate_text3);
        new AlertDialog.Builder(this, R.style.AlertDialogColorButton)
                .setTitle(getString(R.string.s_dlg_confirm_appupdate_title))
                .setMessage(s)
                .setNegativeButton((getString(R.string.s_dlg_no)), null)
                .setPositiveButton((getString(R.string.s_dlg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                updateAppActionInit();
                            }

                        }).create().show();
    }

    protected void updateAppActionInit() {
        new AppActualBodyLoader().updateAppAction(this);
    }


    public void updateAppActionResult(DataStructure ds) {
        ArrayList<AppActualBodyClass> appBodyList = ds.getAppActualBody();
        if (appBodyList.isEmpty()) {
            new InfoUtils().DisplayToastError(R.string.s_appupdate_loading_error);
            return;
        }

        byte[] decodedString = Base64.decode(appBodyList.get(0).getAppactualbody_body(), Base64.DEFAULT);
        //showToast(getApplicationContext(), "Размер приложения "+ decodedString.length);

        String myApkFileName = new FileUtils().getApkPath() + "/" + FILENAMETOUPDATE;
        OutputStream localDbStream;
        try {
            localDbStream = new FileOutputStream(myApkFileName);
        } catch (FileNotFoundException e) {
            new InfoUtils().DisplayToastError(getString(R.string.s_error) + " " + e.getMessage());
            return;
        }
        try {
            localDbStream.write(decodedString);
        } catch (IOException e) {
            new InfoUtils().DisplayToastError(getString(R.string.s_error) + " " + e.getMessage());
            return;
        }

        updateProcess();
    }


    public void updateProcess() {
        new InfoUtils().DisplayToastOk(R.string.s_update_app);
        String myApkPath = new FileUtils().getApkPath();
        File toInstall = new File(myApkPath + FILENAMETOUPDATE);
        if (!toInstall.exists()) {
            new InfoUtils().DisplayToastError(getString(R.string.s_error_file_not_exist) + myApkPath + FILENAMETOUPDATE);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  // Build.VERSION_CODES.N = 24
            Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.setDataAndType(apkUri, "application/vnd.android" + ".package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

    //-------------------------------------------------------------------------------


    protected void verifyAppInit() {
        new AppVerifyVersionLoader().verifyApp(this);
    }

    public void verifyAppAction(DataStructure ds) {
        ArrayList<StateListClass> stateList = ds.getStateList();
        if (stateList.get(0).getIerr().equals("1")) {
            ArrayList<AppActualVersionClass> appverList;
            appverList = ds.getAppActualVersion();
            if (appverList.isEmpty()) {
                dlgLastVersion();
                return;
            }

            String newCode = appverList.get(0).getAppactuvalver_nomer();
            String oldCode = Integer.toString(new MainUtils().getVersionCode());
            String newName = appverList.get(0).getAppactuvalver_name();
            if (!(oldCode).equals(newCode)) {
                dlgUpdateApp(newCode, newName);
            } else {
                dlgLastVersion();
            }
        } else {
            enterLogin();
        }
    }

    protected void enterLogin() {
        if (new NetworkUtils().checkConnection(true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
        }
    }

    public void loginUser(String code) {
        new LoginLoader().loginUser(this, code);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
        new NotificationUtils().cancelNotification(0);
    }

    @Subscribe
    public void getMessage(Events.EventsMessage ev) {
        if (ev.getMes_code().equals(Events.EB_MAIN_REFRESH_RECORDS)) {
            getBoxCount();
            refreshMainMenu();
        }

        if (ev.getMes_code().equals(Events.EB_MAIN_REFRESH_MAINMENU)) {
            drawLayoutMain();
            refreshMainMenu();
        }

        if (ev.getMes_code().equals(Events.EB_MAIN_SET_MASTRESTARTONRESUME)) {
            mastRestartedOnResume = ev.getMes_bool();
        }

    }


}
