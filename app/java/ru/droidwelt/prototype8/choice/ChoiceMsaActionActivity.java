package ru.droidwelt.prototype8.choice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.saverecord.SaveRecordListLoader;
import ru.droidwelt.prototype8.msa.sqlite.MsaExportSQLite;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.FileUtils;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;

public class ChoiceMsaActionActivity extends AppCompatActivity {


    private String MSA_ID;
    private String MSA_FILETYPE;
    private String MSA_FILENAME;
    private int MSA_IMAGESIZE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Dialog);
        setContentView(R.layout.activity_choicemsaaction);
        setTitle(getString(R.string.s_message_action));
        LinearLayout ly_msa_picture_export, ly_msa_send_selected, ly_msa_move_to_draft, ly_msa_copy_to_draft,
                ly_msa_copy_to_fav, ly_msa_copy_to_draft_and_open, ly_msa_delete_selected;
        ly_msa_picture_export = findViewById(R.id.ly_msa_picture_export);
        ly_msa_send_selected = findViewById(R.id.ly_msa_send_selected);
        ly_msa_move_to_draft = findViewById(R.id.ly_msa_move_to_draft);
        ly_msa_copy_to_draft = findViewById(R.id.ly_msa_copy_to_draft);
        ly_msa_copy_to_fav = findViewById(R.id.ly_msa_copy_to_fav);
        ly_msa_copy_to_draft_and_open = findViewById(R.id.ly_msa_copy_to_draft_and_open);
        ly_msa_delete_selected = findViewById(R.id.ly_msa_delete_selected);

        Bundle extras = getIntent().getExtras();

        MSA_ID = extras != null ? extras.getString("MSA_ID", "") : null;
        MSA_FILETYPE = extras != null ? extras.getString("MSA_FILETYPE", "") : null;
        MSA_FILENAME = extras != null ? extras.getString("MSA_FILENAME", "") : null;
        MSA_IMAGESIZE = extras != null ? extras.getInt("MSA_IMAGESIZE", 0) : 0;

        if (Appl.MSA_MODEVIEW != 4) ly_msa_send_selected.setVisibility(View.GONE);
        if (!((Appl.MSA_MODEVIEW == 3) | (Appl.MSA_MODEVIEW == 4)))
            ly_msa_move_to_draft.setVisibility(View.GONE);
        if (!(Appl.MSA_MODEVIEW == 10)) ly_msa_copy_to_fav.setVisibility(View.GONE);
        if (!((Appl.MSA_MODEVIEW == 2) | (Appl.MSA_MODEVIEW == 4) | (Appl.MSA_MODEVIEW == 4)))
            ly_msa_copy_to_draft_and_open.setVisibility(View.GONE);
        if (MSA_IMAGESIZE == 0) ly_msa_picture_export.setVisibility(View.GONE);

        ly_msa_picture_export.setOnClickListener(oclBtnOk);
        ly_msa_send_selected.setOnClickListener(oclBtnOk);
        ly_msa_move_to_draft.setOnClickListener(oclBtnOk);
        ly_msa_copy_to_draft.setOnClickListener(oclBtnOk);
        ly_msa_copy_to_fav.setOnClickListener(oclBtnOk);
        ly_msa_copy_to_draft_and_open.setOnClickListener(oclBtnOk);
        ly_msa_delete_selected.setOnClickListener(oclBtnOk);
    }


    OnClickListener oclBtnOk = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.ly_msa_picture_export:
                    if ((!MSA_FILETYPE.equals("")) && (MSA_IMAGESIZE > 0)) {
                        String fn = new FileUtils().createValidFileName(MSA_FILENAME);
                        MsaExportSQLite ex = new MsaExportSQLite();
                        ex.sFileName = fn;
                        ex.sFileType = MSA_FILETYPE;
                        ex.execute();
                        finish();
                    }
                    break;

                case R.id.ly_msa_send_selected:
                    if (new NetworkUtils().checkConnection(true)) {
                        new SaveRecordListLoader().setSaveRecordList(Appl.MSA_ID);
                        finish();
                    }
                    break;

                case R.id.ly_msa_move_to_draft: {
                    new MsaUtilsSQLite().move_one_to_draft_SQLite(MSA_ID);
                    Events.EventsMessage ev = new Events.EventsMessage();
                    ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
                    GlobalBus.getBus().post(ev);
                }
                finish();
                break;

                case R.id.ly_msa_copy_to_draft:
                    new MsaUtilsSQLite().copy_one_to_folder_SQLite(MSA_ID, 0);
                    if (Appl.MSA_MODEEDIT == 0) {
                        Events.EventsMessage ev = new Events.EventsMessage();
                        ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
                        GlobalBus.getBus().post(ev);
                    }
                    finish();
                    break;

                case R.id.ly_msa_copy_to_fav:
                    new MsaUtilsSQLite().copy_one_to_folder_SQLite(MSA_ID, 10);
                    break;

                case R.id.ly_msa_copy_to_draft_and_open: {
                    Events.EventsMessage ev = new Events.EventsMessage();
                    ev.setMes_code(Events.EB_MSA_COPY_ONE_RECORD_TO_DRAFT);
                    ev.setMes_str(MSA_ID);
                    GlobalBus.getBus().post(ev);
                }
                finish();
                break;

                case R.id.ly_msa_delete_selected: {
                    Events.EventsMessage ev = new Events.EventsMessage();
                    ev.setMes_code(Events.EB_MSA_DELETE_ONE_RECORD);
                    ev.setMes_str(MSA_ID);
                    GlobalBus.getBus().post(ev);
                }
                finish();
                break;

                default:
                    break;
            }
        }
    };


}