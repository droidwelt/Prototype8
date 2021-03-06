package ru.droidwelt.prototype8.choice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;


public class ChoiceLblActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Dialog);
        setContentView(R.layout.activity_choicelbl);
        setTitle(getString(R.string.s_message_choice_lbl));
        ImageButton lbl0, lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, lbl7,
                lbl8, lbl9, lbl10, lbl11, lbl12, lbl13, lbl14, lbl15, lbl16, lbl17, lbl18, lbl19, lbl20, lbl21, lbl22, lbl23;
        lbl0 = findViewById(R.id.lbl0);
        lbl1 = findViewById(R.id.lbl1);
        lbl2 = findViewById(R.id.lbl2);
        lbl3 = findViewById(R.id.lbl3);
        lbl4 = findViewById(R.id.lbl4);
        lbl5 = findViewById(R.id.lbl5);
        lbl6 = findViewById(R.id.lbl6);
        lbl7 = findViewById(R.id.lbl7);
        lbl8 = findViewById(R.id.lbl8);
        lbl9 = findViewById(R.id.lbl9);
        lbl10 = findViewById(R.id.lbl10);
        lbl11 = findViewById(R.id.lbl11);
        lbl12 = findViewById(R.id.lbl12);
        lbl13 = findViewById(R.id.lbl13);
        lbl14 = findViewById(R.id.lbl14);
        lbl15 = findViewById(R.id.lbl15);
        lbl16 = findViewById(R.id.lbl16);
        lbl17 = findViewById(R.id.lbl17);
        lbl18 = findViewById(R.id.lbl18);
        lbl19 = findViewById(R.id.lbl19);
        lbl20 = findViewById(R.id.lbl20);
        lbl21 = findViewById(R.id.lbl21);
        lbl22 = findViewById(R.id.lbl22);
        lbl23 = findViewById(R.id.lbl23);

        lbl0.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("0", 0));
        lbl1.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("1", 0));
        lbl2.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("2", 0));
        lbl3.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("3", 0));
        lbl4.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("4", 0));
        lbl5.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("5", 0));
        lbl6.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("6", 0));
        lbl7.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("7", 0));
        lbl8.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("8", 0));
        lbl9.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("9", 0));
        lbl10.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("10", 0));
        lbl11.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("11", 0));
        lbl12.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("12", 0));
        lbl13.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("13", 0));
        lbl14.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("14", 0));
        lbl15.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("15", 0));
        lbl16.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("16", 0));
        lbl17.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("17", 0));
        lbl18.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("18", 0));
        lbl19.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("19", 0));
        lbl20.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("20", 0));
        lbl21.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("21", 0));
        lbl22.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("22", 0));
        lbl23.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer("23", 0));

        lbl0.setOnClickListener(oclBtnOk);
        lbl1.setOnClickListener(oclBtnOk);
        lbl2.setOnClickListener(oclBtnOk);
        lbl3.setOnClickListener(oclBtnOk);
        lbl4.setOnClickListener(oclBtnOk);
        lbl5.setOnClickListener(oclBtnOk);
        lbl6.setOnClickListener(oclBtnOk);
        lbl7.setOnClickListener(oclBtnOk);
        lbl8.setOnClickListener(oclBtnOk);
        lbl9.setOnClickListener(oclBtnOk);
        lbl10.setOnClickListener(oclBtnOk);
        lbl11.setOnClickListener(oclBtnOk);
        lbl12.setOnClickListener(oclBtnOk);
        lbl13.setOnClickListener(oclBtnOk);
        lbl14.setOnClickListener(oclBtnOk);
        lbl15.setOnClickListener(oclBtnOk);
        lbl16.setOnClickListener(oclBtnOk);
        lbl17.setOnClickListener(oclBtnOk);
        lbl18.setOnClickListener(oclBtnOk);
        lbl19.setOnClickListener(oclBtnOk);
        lbl20.setOnClickListener(oclBtnOk);
        lbl21.setOnClickListener(oclBtnOk);
        lbl22.setOnClickListener(oclBtnOk);
        lbl23.setOnClickListener(oclBtnOk);
    }

    public void setlbl(String sn) {
        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_MSAEDIT_SET_LBL);
        ev.setMes_str(sn);
        GlobalBus.getBus().post(ev);
        finish();
    }


    OnClickListener oclBtnOk = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.lbl0:
                    setlbl("0");
                    break;

                case R.id.lbl1:
                    setlbl("1");
                    break;

                case R.id.lbl2:
                    setlbl("2");
                    break;

                case R.id.lbl3:
                    setlbl("3");
                    break;

                case R.id.lbl4:
                    setlbl("4");
                    break;

                case R.id.lbl5:
                    setlbl("5");
                    break;

                case R.id.lbl6:
                    setlbl("6");
                    break;

                case R.id.lbl7:
                    setlbl("7");
                    break;

                case R.id.lbl8:
                    setlbl("8");
                    break;

                case R.id.lbl9:
                    setlbl("9");
                    break;

                case R.id.lbl10:
                    setlbl("10");
                    break;

                case R.id.lbl11:
                    setlbl("11");
                    break;

                case R.id.lbl12:
                    setlbl("12");
                    break;

                case R.id.lbl13:
                    setlbl("13");
                    break;

                case R.id.lbl14:
                    setlbl("14");
                    break;

                case R.id.lbl15:
                    setlbl("15");
                    break;

                case R.id.lbl16:
                    setlbl("16");
                    break;

                case R.id.lbl17:
                    setlbl("17");
                    break;

                case R.id.lbl18:
                    setlbl("18");
                    break;

                case R.id.lbl19:
                    setlbl("19");
                    break;

                case R.id.lbl20:
                    setlbl("20");
                    break;

                case R.id.lbl21:
                    setlbl("21");
                    break;

                case R.id.lbl22:
                    setlbl("22");
                    break;

                case R.id.lbl23:
                    setlbl("23");
                    break;

                default:
                    break;
            }
        }
    };


}