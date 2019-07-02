package ru.droidwelt.prototype8.utils.fcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.main.MainActivity;
import ru.droidwelt.prototype8.msa.loadrecord.LoadRecordListLoader;
import ru.droidwelt.prototype8.utils.common.Appl;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
       // Log.e("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map data = remoteMessage.getData();
        if (remoteMessage.getData().size() > 0) {
            //   Log.d("EEEEEE", "remoteMessage.getData(): " + message.getData().toString());
            String title = "";
            if (data.get("TITLE") != null)
                title = data.get("TITLE").toString();
            String text = "";
            if (data.get("TEXT") != null)
                text = data.get("TEXT").toString();
            generateNotification(Appl.getContext(), title, text);

            try {
                new LoadRecordListLoader().getLoadRecordList(null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (remoteMessage.getNotification() != null) {
            //  Log.d("EEEEEE", "remoteMessage.getNotification(): " + message.getNotification().toString());
            //  Log.d("EEE", "remoteMessage.getNotification().getTitle(): " + message.getNotification().getTitle());
            String title = "";
            if (data.get("TITLE") != null)
                title = remoteMessage.getNotification().getTitle();
            String text = "";
            if (data.get("TEXT") != null)
                text = remoteMessage.getNotification().getBody();
            generateNotification(Appl.getContext(), title, text);

            try {
                new LoadRecordListLoader().getLoadRecordList(null,false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private static void generateNotification(Context context, String MSA_TITLE, String MSA_TEXT) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);  // TYPE_NOTIFICATION

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(MSA_TEXT).setBigContentTitle(MSA_TITLE))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification))
                .setLights(Color.BLUE, 500, 500)
                .setContentTitle(MSA_TITLE)
                .setContentText(MSA_TEXT)
                .setAutoCancel(true)
                .setTicker(MSA_TITLE /*context.getString(R.string.s_message_sendind)*/)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{100, 250});

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0 /*PendingIntent.FLAG_ONE_SHOT*/);
        nBuilder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, nBuilder.build());
    }
}
