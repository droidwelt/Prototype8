package ru.droidwelt.prototype8.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.MapsUtils;
import ru.droidwelt.prototype8.utils.common.OnSwipeTouchListener;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;


public class ClientViewActivity extends AppCompatActivity {

    private int CLT_ID = -1;
    public ListView lv;
    private int LIST_POS = -1;
    OnSwipeTouchListener onSwipeTouchListener;
    private ClientViewCursorAdapter myAdapter;
    private List<Integer> LIST_CLT_ID;
    private  String clt_lat,clt_lon,clt_zoom,clt_name,clt_address;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_cltview);
        setTitle(getString(R.string.header_cltview));
        Bundle extras = getIntent().getExtras();
        CLT_ID = extras != null ? extras.getInt("CLT_ID") : 0;
        LIST_POS = extras != null ? extras.getInt("POS") : 0;

        if (extras != null) {
            LIST_CLT_ID = extras.getIntegerArrayList("LIST_CLT_ID");
        }

        String[] from = new String[]{"OPP_OPER", "OPP_DATE", "OPP_NAME", "OPP_SUM"};
        int[] to = new int[]{R.id.text_cltview_OPP_OPER,
                R.id.text_cltview_OPP_DATE,
                R.id.text_cltview_OPP_NAME,
                R.id.text_cltview_OPP_SUM};


        setmyAdapter(new ClientViewCursorAdapter(this, R.layout.activity_cltview_item, getClientViewRecords(), from, to));
        lv = findViewById(R.id.list);
        lv.setAdapter(getClientViewAdapter());
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        operClientData();
        creareOnSwipeTouchListener();
        LinearLayout ly_cltView = findViewById(R.id.ly_cltView);
        ly_cltView.setOnTouchListener(onSwipeTouchListener);
        ly_cltView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //  Toast.makeText(ClientViewActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void creareOnSwipeTouchListener() {
        onSwipeTouchListener = new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                // Toast.makeText(ClientViewActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                //  Toast.makeText(ClientViewActivity.this, "right", Toast.LENGTH_SHORT).show();
                moveToPrev();
            }

            public void onSwipeLeft() {
                //   Toast.makeText(ClientViewActivity.this, "left", Toast.LENGTH_SHORT).show();
                moveToNext();
            }

        /*    public void onSwipeBottom() {
                //  Toast.makeText(ClientViewActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

            public void onClick() {
              //  Toast.makeText(ClientViewActivity.this, "Click", Toast.LENGTH_SHORT).show();
            } */
        };
    }


    public void moveToNext() {
        if (LIST_POS < LIST_CLT_ID.size()) {
            LIST_POS = LIST_POS + 1;
            CLT_ID = LIST_CLT_ID.get(LIST_POS);
            operClientData();
        }
    }

    public void moveToPrev() {
        if (LIST_POS > 0) {
            LIST_POS = LIST_POS - 1;
            CLT_ID = LIST_CLT_ID.get(LIST_POS);
            operClientData();
        }
    }

    public void operClientData() {
        dipslay_client_Header();
        getClientViewAdapter().changeCursor(getClientViewRecords());
    }


    public void dipslay_client_Header() {
        String sSQL = "select * from CLT where CLT_ID=" + CLT_ID;
        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            try {
                TextView tv_clt_name = findViewById(R.id.et_cltview_clt_name);
                tv_clt_name.setText(c.getString(c.getColumnIndex("CLT_NAME")));
                TextView tv_clt_addr = findViewById(R.id.et_cltview_clt_addr);
                tv_clt_addr.setText(c.getString(c.getColumnIndex("CLT_ADDR")));
                TextView tv_clt_phone = findViewById(R.id.et_cltview_clt_phone);
                tv_clt_phone.setText(c.getString(c.getColumnIndex("CLT_PHONE")));
                TextView tv_clt_saldo = findViewById(R.id.et_cltview_clt_saldo);
                double plus = c.getDouble(c.getColumnIndex("CLT_AVANS"));
                double minus = c.getDouble(c.getColumnIndex("CLT_DOLG"));
                String sSaldo = "" + (plus - minus);
                tv_clt_saldo.setText(sSaldo);
                setTitle(tv_clt_name.getText());

                clt_lat = new StringUtils().strnormalize(c.getString(c.getColumnIndex("CLT_LAT")));
                clt_lon = new StringUtils().strnormalize(c.getString(c.getColumnIndex("CLT_LON")));
                clt_zoom = new StringUtils().strnormalize(c.getString(c.getColumnIndex("CLT_ZOOM")));
                clt_name = new StringUtils().strnormalize(c.getString(c.getColumnIndex("CLT_NAME")));
                clt_address = new StringUtils().strnormalize(c.getString(c.getColumnIndex("CLT_ADDR")));

                ImageButton ib_map = findViewById(R.id.ib_map2);
                if (new MapsUtils().verifyCoord(clt_lat, clt_lon, clt_zoom)) {
                    ib_map.setImageResource(R.mipmap.main_maps);
                    ib_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           displayMap ();
                        }
                    });
                }  else {
                    ib_map.setImageResource(R.mipmap.ic_empty);
                }
            } catch (Exception ignored) {
            }
        } else
            setTitle("??");
        c.close();
    }

    private void displayMap () {
        double dlat = new MapsUtils().strToDouble (clt_lat);
        double dlon = new MapsUtils().strToDouble (clt_lon);
        double dzoom = new MapsUtils().strToDouble (clt_zoom);
        new MapsUtils().showMaps( this, dlat, dlon, dzoom,clt_name,clt_address);
    }


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }  */



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent answerIntent = new Intent();
        answerIntent.putExtra("POS", LIST_POS);
        setResult(RESULT_OK, answerIntent);
        new PrefUtils().animateFinish(this);
    }



    public  Cursor getClientViewRecords() {
        String sSQL =
                "select  _id, OPP_OPER, OPP_DATE, OPP_NAME, OPP_SUM"
                        + " from OPP "
                        + " where CLT_ID=" + CLT_ID
                        + " order by OPP_DATE ";
        return Appl.getDatabase().rawQuery(sSQL, null);
    }



    public ClientViewCursorAdapter getClientViewAdapter() {
        return myAdapter;
    }

    public void setmyAdapter(ClientViewCursorAdapter operAdapter) {
        myAdapter = operAdapter;
    }

}
