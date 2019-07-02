package ru.droidwelt.prototype8.management.inspect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;

public class InspInputActivity extends AppCompatActivity {

    final private static int EXIT_CODE_SCANNER = 2345;
    private EditText et_name, et_kvo2, et_bar, et_comment;
    private String s_name = "", s_kvo1 = "", s_kvo2 = "", s_bar = "", s_comment = "";
    private int _id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_inspinput);
        ImageButton ib_ok, ib_cancel, ib_scanner, ib_setkvo2;

        ib_ok = findViewById(R.id.ib_ok);
        ib_cancel = findViewById(R.id.ib_cancel);
        ib_scanner = findViewById(R.id.ib_scanner);
        ib_setkvo2 = findViewById(R.id.ib_setkvo2);
        et_name = findViewById(R.id.et_name);
        EditText et_kvo1 = findViewById(R.id.et_kvo1);
        et_kvo2 = findViewById(R.id.et_kvo2);
        et_bar = findViewById(R.id.et_bar);
        et_comment = findViewById(R.id.et_comment);
        Bundle extras = getIntent().getExtras();
        _id = extras != null ? extras.getInt("_id") : 0;

        if (_id >= 0) {
            setTitle(getString(R.string.header_inspect));

            s_name = extras != null ? extras.getString("RVV_NAME") : "";
            s_kvo1 = extras != null ? extras.getString("RVV_KVO1") : "";
            s_kvo2 = extras != null ? extras.getString("RVV_KVO2") : "";
            s_bar = extras != null ? extras.getString("RVV_BAR") : "";
            s_comment = extras != null ? extras.getString("RVV_COMMENT") : "";

            et_name.setText(s_name);
            et_kvo1.setText(s_kvo1);
            et_kvo2.setText(s_kvo2);
            et_bar.setText(s_bar);
            et_comment.setText(s_comment);
        } else {
            //	Log.i("LOG", "InspInputActivity NEW");
            setTitle(getString(R.string.add_new));
            et_name.setTextColor(getResources().getColor(R.color.text_edit));
            et_bar.setTextColor(getResources().getColor(R.color.text_edit));
            ib_setkvo2.setEnabled(false);
        }

        ib_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        ib_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().isEmpty()) {
                    new InfoUtils().DisplayToastError(getString(R.string.s_insp_noname));
                } else {
                    s_kvo2 = et_kvo2.getText().toString();
                    s_comment = et_comment.getText().toString();
                    s_bar = et_bar.getText().toString();
                    s_name = et_name.getText().toString();
                    s_kvo2 = new StringUtils().strnormalize(et_kvo2.getText().toString());
                    s_comment = new StringUtils().strnormalize(et_comment.getText().toString());
                    s_bar = new StringUtils().strnormalize(et_bar.getText().toString());
                    s_name = new StringUtils().strnormalize(et_name.getText().toString());
                    if (_id >= 0) { // UPDATE
                        String sLite = "UPDATE RVV SET RVV_KVO2='" + s_kvo2
                                + "',RVV_COMMENT='" + s_comment
                                + "',RVV_BAR='" + s_bar
                                + "',RVV_STATE='V' where _id="
                                + _id;
                        Appl.getDatabase().execSQL(sLite);

                    } else {// INSERT
                        String sLite =
                                "INSERT INTO RVV  ( RVV_NAME,RVV_KVO2,RVV_COMMENT,RVV_BAR,RVV_STATE) " +
                                        "VALUES ('" + s_name + "','" + s_kvo2 + "','" + s_comment + "','" + s_bar + "','N');";
                        Appl.getDatabase().execSQL(sLite);
                    }

                    Events.EventsMessage ev = new Events.EventsMessage();
                    ev.setMes_code(Events.EB_INSPECT_REFRESH_RECORDS);
                    GlobalBus.getBus().post(ev);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        ib_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    startActivityForResult(intent, EXIT_CODE_SCANNER);
                } else {
                    new InfoUtils().DisplayToastError(R.string.s_scanner_not_installed);
                }
            }
        });

        ib_setkvo2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                et_kvo2.setText(s_kvo1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EXIT_CODE_SCANNER) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                if (contents != null) et_bar.setText(contents);
            }
        }
    }
}
