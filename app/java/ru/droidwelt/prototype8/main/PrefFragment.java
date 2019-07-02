package ru.droidwelt.prototype8.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;

public class PrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String sMode = "Y";
        if (getArguments() != null) {
            sMode = getArguments().getString("MODE_GLOBAL");
        }

        if (sMode != null && sMode.equals("Y")) {
            addPreferencesFromResource(R.xml.pref);
        } else {
            addPreferencesFromResource(R.xml.pref_mes);
        }

        addPreferencesFromResource(R.xml.pref);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        displayServerNomer();
        displayServerName("0");
        displayServerName("1");
        displayServerName("2");
        displayAppStyle();
        displayAnimationMode();
        displayDownLoadDates();
        displayDownLoadMaxSize();
        displayTextSize();
    }


    private void displayTextSize() {
        ListPreference appTextSize = (ListPreference) findPreference("textsize_xpos");
        int iPos = new PrefUtils().getTextSizePosit();
        switch (iPos) {
            case 0:
                appTextSize.setSummary("Установлен размер 55");
                break;
            case 1:
                appTextSize.setSummary("Установлен размер 70");
                break;
            case 2:
                appTextSize.setSummary("Установлен размер 85");
                break;
            case 3:
                appTextSize.setSummary("Установлен размер 100");
                break;
            case 4:
                appTextSize.setSummary("Установлен размер 115");
                break;
            case 5:
                appTextSize.setSummary("Установлен размер 130");
                break;
            case 6:
                appTextSize.setSummary("Установлен размер 145");
                break;
            default:
                break;
        }
    }

    private void displayServerNomer() {
        ListPreference serverNomer = (ListPreference) findPreference("server_nomer");
        String s = new PrefUtils().getServer_nomer();
        switch (s) {
            case "0":
                serverNomer.setSummary("Выбран сервер 0");
                break;
            case "1":
                serverNomer.setSummary("Выбран сервер 1");
                break;
            case "2":
                serverNomer.setSummary("Выбран сервер 2");
                break;
            default:
                serverNomer.setSummary("Сервер не выбран");
                break;
        }
    }

    private void displayServerName(String nomer) {
        EditTextPreference serverName;
        String name = new PrefUtils().getServer_name(nomer);
        switch (nomer) {
            case "0":
                serverName = (EditTextPreference) findPreference("server_name_0");
                serverName.setSummary(name);
                break;
            case "1":
                serverName = (EditTextPreference) findPreference("server_name_1");
                serverName.setSummary(name);
                break;
            case "2":
                serverName = (EditTextPreference) findPreference("server_name_2");
                serverName.setSummary(name);
                break;
            default:
                break;
        }
    }

    private void displayAppStyle() {
        ListPreference appStyle = (ListPreference) findPreference("app_style");
        String sAppStyle = new PrefUtils().getAppStyle();
        switch (sAppStyle) {
            case "C":
                appStyle.setSummary("С - карточки");
                break;
            case "F":
                appStyle.setSummary("F - плоский дизайн");
                break;
            default:
                break;
        }
    }

    private void displayAnimationMode() {
        ListPreference animMode = (ListPreference) findPreference("animation_mode");
        int nAnimMode = new PrefUtils().getAnimation_mode();
        switch (nAnimMode) {
            case 1:
                animMode.setSummary("down -> up");
                break;
            case 2:
                animMode.setSummary("up -> down");
                break;
            case 3:
                animMode.setSummary("right -> lef");
                break;
            case 4:
                animMode.setSummary("left -> right");
                break;
            default:
                animMode.setSummary("none");
                break;
        }
    }


    private void displayDownLoadDates() {
        EditTextPreference editTextPreference = (EditTextPreference) findPreference("download_dates");
        int n = new PrefUtils().getDaysDownLoad();
        editTextPreference.setSummary("Число дней за которые загружаются данные: " + n);
    }

    private void displayDownLoadMaxSize() {
        EditTextPreference editTextPreference = (EditTextPreference) findPreference("download_maxsize");
        int n = (new PrefUtils().getDownload_maxsize() / 1024 / 1024);
        editTextPreference.setSummary("Размер вложения в сообщение не может быть более " + n + " Мб");
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("textsize_xpos")) {
            displayTextSize();
            new Appl().setTextSizeToApp();

            PrefActivity a = (PrefActivity) getActivity();
            if (a != null)
                a.refreshFragment();
        }

        if (key.equals("server_nomer")) {
            displayServerNomer();
        }

        if (key.equals("server_name_0")) {
            displayServerName("0");
        }

        if (key.equals("server_name_1")) {
            displayServerName("1");
        }

        if (key.equals("server_name_2")) {
            displayServerName("2");
        }

        if (key.equals("app_style")) {
            displayAppStyle();
        }

        if (key.equals("animation_mode")) {
            displayAnimationMode();
        }

        if (key.equals("download_dates")) {
            displayDownLoadDates();
        }

        if (key.equals("download_maxsize")) {
            displayDownLoadMaxSize();
        }

        if (key.equals("font_size")) {
            Events.EventsMessage ev = new Events.EventsMessage();
            ev.setMes_code(Events.EB_MAIN_SET_MASTRESTARTONRESUME);
            ev.setMes_bool(true);
            GlobalBus.getBus().post(ev);
        }

        if (key.equals("app_style")) {
            Events.EventsMessage ev = new Events.EventsMessage();
            ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
            GlobalBus.getBus().post(ev);
        }

    }


}
