package ru.droidwelt.prototype8.utils.common;

import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ru.droidwelt.prototype8.R;

public class FcmUtils {


    public  void verityFcmCode() {
        String sStored = new PrefUtils().getFcmCode();
        if ((!sStored.isEmpty()) && (!Appl.DEVICE_FCM.isEmpty()) && (!Appl.DEVICE_FCM.equals(sStored))) {
            new InfoUtils().DisplayToastOk(Appl.getContext().getString(R.string.s_message_fcm_changed));
        }
    }


    public  void getDeviceFCM(Activity a) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(a, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Appl.DEVICE_FCM = instanceIdResult.getToken();
            }
        });
    }


}
