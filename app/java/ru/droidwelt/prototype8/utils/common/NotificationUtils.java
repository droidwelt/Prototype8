package ru.droidwelt.prototype8.utils.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import ru.droidwelt.prototype8.R;


public class NotificationUtils {

    public  void createNotificationRead() {
        createNotification(Appl.getContext().getString(R.string.s_getting_json_message), 11);
    }

    public  void createNotificationWrite() {
        createNotification(Appl.getContext().getString(R.string.s_setting_json_message), 12);
    }


    private  void createNotification(String s, int id) {

        int iconID;
        if (id == 12) {
            iconID = R.drawable.ib_write_animate;
        } else {
            iconID = R.drawable.ib_read_animate;
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(Appl.context,"")
                        .setSmallIcon(iconID)
                        .setColor(Color.TRANSPARENT)
                        .setContentTitle(Appl.context.getString(R.string.app_name))
                        .setContentText(s)
                        .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager)
                Appl.context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(id, notification);
    }


    public  void cancelNotification(int id) {

        NotificationManager notificationManager = (NotificationManager)
                Appl.context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (id > 0) {
            notificationManager.cancel(id);
        } else {
            notificationManager.cancel(11);
            notificationManager.cancel(12);
        }
    }


    public  void cancelNotificationRead() {
        cancelNotification(11);
    }

    public  void cancelNotificationWrite() {
        cancelNotification(12);
    }
}