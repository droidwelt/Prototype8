package ru.droidwelt.prototype8.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import ru.droidwelt.prototype8.R;

public class InfoUtils  {

    class CustomToast extends android.widget.Toast {

        private CustomToast() {
            super(Appl.getContext());
        }

        @SuppressWarnings("Annotator")
        private CustomToast(Context context, String text, int duration, int resId) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View rootView = Objects.requireNonNull(inflater).inflate(R.layout.toast_info, null) ;
            if (rootView != null) {
                ImageView toastImage;
                toastImage = rootView.findViewById(R.id.iv_mesedit_lbl);
                if (resId==0)  {
                    toastImage.setImageResource(R.drawable.ic_launcher);
                }  else {
                    toastImage.setImageResource(resId);
                }
                TextView toastText;
                toastText = rootView.findViewById(R.id.textView1);
                toastText.setText(text);
            }
            this.setView(rootView);
            this.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            this.setDuration(duration);
        }


        CustomToast makeText(Context context, String text, int duration, int resId) {
            return new CustomToast(context, text, duration, resId);
        }

    }



    public  void DisplayToastError(String result) {
        Toast toast3 = new CustomToast().makeText(Appl.context, result, Toast.LENGTH_LONG, R.mipmap.ic_cancel);
        toast3.show();
    }

    public  void DisplayToastError(int resID) {
        Toast toast3 = new CustomToast().makeText(Appl.context, Appl.context.getString(resID), Toast.LENGTH_LONG, R.mipmap.ic_cancel);
        toast3.show();
    }

    public  void DisplayToastOk(String result) {
        Toast toast3 = new CustomToast().makeText(Appl.context, result, Toast.LENGTH_LONG, R.mipmap.ic_checked);
        toast3.show();
    }

    public  void DisplayToastOk(int resID) {
        Toast toast3 = new CustomToast().makeText(Appl.context, Appl.context.getString(resID), Toast.LENGTH_LONG, R.mipmap.ic_checked);
        toast3.show();
    }


    public  void DisplayToastInfo(String result, int idimage, int duration) {
        Toast toast3 = new CustomToast().makeText(Appl.context, result, duration, idimage);//.LENGTH_SHORT
        toast3.show();
    }

}
