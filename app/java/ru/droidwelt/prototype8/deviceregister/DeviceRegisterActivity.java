package ru.droidwelt.prototype8.deviceregister;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;


public class DeviceRegisterActivity extends AppCompatActivity {

    private Button bt_register_dev;
    private boolean device_registred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Dialog);
        setContentView(R.layout.activity_deviceregister);
        setTitle(getString(R.string.header_register_device));
        bt_register_dev = findViewById(R.id.button_register_dev);
        bt_register_dev.setOnClickListener(onclickButton);
        new DeviceRegisterVfyLoader().registerDeviceVfy(this, Appl.DEVICE_FCM);
    }

    public void displayDevice(ArrayList<DeviceRegisterVfyDataClass> ds) {
        int dev_id = Integer.valueOf( ds.get(0).getDev_id());
        device_registred = (dev_id > 0);

        TextView tv_register_dev = findViewById(R.id.textView_register_dev);
        TextView tv_register_fcm = findViewById(R.id.textView_register_fcm);
        String s = getString(R.string.s_reg_fcm) +  Appl.DEVICE_FCM;
        tv_register_fcm.setText(s);
        TextView tv_register_model = findViewById(R.id.textView_register_model);
        s = getString(R.string.s_reg_model) + android.os.Build.MODEL;
        tv_register_model.setText(s);
        if (device_registred) {
            s = getString(R.string.s_reg_registred);
            tv_register_dev.setText(s);
            bt_register_dev.setText(R.string.header_register_device_undo);
            new PrefUtils().setFcmCode(Appl.DEVICE_FCM);
        } else {
            tv_register_dev.setText(R.string.s_reg_notregistred);
            bt_register_dev.setText(R.string.header_register_device);
            new PrefUtils().setFcmCode("");
        }
    }


    android.view.View.OnClickListener onclickButton = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.button_register_dev:
                    new DeviceRegisterSetLoader().registerDeviceSet(DeviceRegisterActivity.this,device_registred );
                    break;

                default:
                    break;
            }
        }
    };





}
