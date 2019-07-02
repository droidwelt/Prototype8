package ru.droidwelt.prototype8.msa.saverecord;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import ru.droidwelt.prototype8.msa.retrofit.RetrofitClientMsa;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import ru.droidwelt.prototype8.utils.fcm.PushNotification;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SaveRecordListLoader {


    public void setSaveRecordAllList() {
        String sSQL = "select MSA_ID from MSA where MSA_STATE=4 order by MSA_DATE;";
        Cursor cc = Appl.getDatabase().rawQuery(sSQL, null);
        cc.moveToFirst();
        while (!cc.isAfterLast()) {
            String xmsa_id = cc.getString(cc.getColumnIndex("MSA_ID"));
            setSaveRecordList(xmsa_id);
            cc.moveToNext();        }
        cc.close();
    }


    public void setSaveRecordList(final String sMSA_ID) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationWrite();

            String sSQL = "select MSA_ID,MSA_CLR,MSA_LBL,MSA_TITLE,MSA_TEXT,FIO_ID," +
                    "MSA_FILETYPE,MSA_FILENAME,length(MSA_IMAGE) as IMAGESIZE " +
                    "from MSA where MSA_ID='" + sMSA_ID + "'";
            Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
            c.moveToFirst();
            int index = c.getColumnIndex("MSA_TITLE");
            String sMSA_TITLE = new StringUtils().strnormalize(c.getString(index));
            index = c.getColumnIndex("MSA_TEXT");
            String sMSA_TEXT = new StringUtils().strnormalize(c.getString(index));
            index = c.getColumnIndex("MSA_CLR");
            String sMSA_CLR = new StringUtils().strnormalize(c.getString(index));
            index = c.getColumnIndex("MSA_LBL");
            String sMSA_LBL = new StringUtils().strnormalize(c.getString(index));
            index = c.getColumnIndex("FIO_ID");
            int iFIO_ID = Integer.parseInt(new StringUtils().strnormalize(c.getString(index)));
            String sFIO_ID = Integer.toString(iFIO_ID);
            index = c.getColumnIndex("MSA_FILETYPE");
            String sMSA_FILETYPE = new StringUtils().strnormalize(c.getString(index));
            index = c.getColumnIndex("MSA_FILENAME");
            String sMSA_FILENAME = new StringUtils().strnormalize(c.getString(index));
            index = c.getColumnIndex("IMAGESIZE");
            String sMSA_IMAGESIZE = new StringUtils().strnormalize(c.getString(index));
            int iMSA_IMAGESIZE = 0;
            if (!sMSA_IMAGESIZE.equals("")) iMSA_IMAGESIZE = Integer.parseInt(sMSA_IMAGESIZE);
            c.close();

            StringBuilder smsb_list = new StringBuilder();
            sSQL = "select distinct X.* from " +
                    "(" +
                    "  select  F.FIO_ID " +
                    "  from MSB B " +
                    "  left join FIO F on B.FIO_ID=F.FIO_ID " +
                    "  where F.FIO_TP=2 and MSA_ID='" + sMSA_ID + "' " +
                    "     union all  " +
                    "  select  W.FIO_IDMB " +
                    "  from FIO W " +
                    "  where W.FIO_IDGR in " +
                    "  (select  B.FIO_ID " +
                    "  from MSB B " +
                    "  left join FIO F on B.FIO_ID=F.FIO_ID " +
                    "  where F.FIO_TP=1 and MSA_ID='" + sMSA_ID + "') " +
                    "  ) X ";

            c = Appl.getDatabase().rawQuery(sSQL, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int xfio_id = c.getInt(c.getColumnIndex("FIO_ID"));
                smsb_list.append("insert into MSB (MSA_ID,MSB_ID,FIO_ID) " + " values (CONVERT(uniqueidentifier,'")
                        .append(sMSA_ID).append("'),").append("NewID(),").append(xfio_id).append(") ");
                c.moveToNext();
            }
            c.close();

            RequestBody MSA_ID = RequestBody.create(MediaType.parse("text/plain"), sMSA_ID);
            RequestBody MSA_CLR = RequestBody.create(MediaType.parse("text/plain"), sMSA_CLR);
            RequestBody MSA_LBL = RequestBody.create(MediaType.parse("text/plain"), sMSA_LBL);
            RequestBody MSA_TITLE = RequestBody.create(MediaType.parse("text/plain"), sMSA_TITLE);
            RequestBody MSA_TEXT = RequestBody.create(MediaType.parse("text/plain"), sMSA_TEXT);
            RequestBody FIO_ID = RequestBody.create(MediaType.parse("text/plain"), sFIO_ID);
            RequestBody MSA_FILETYPE = RequestBody.create(MediaType.parse("text/plain"), sMSA_FILETYPE);
            RequestBody MSA_FILENAME = RequestBody.create(MediaType.parse("text/plain"), sMSA_FILENAME);
            RequestBody MSA_IMAGESIZE = RequestBody.create(MediaType.parse("text/plain"), sMSA_IMAGESIZE);
            RequestBody MSB_LIST = RequestBody.create(MediaType.parse("text/plain"), smsb_list.toString());

            byte[] imageInByteBig = new MsaUtilsSQLite().getByteArrayFromMsaImageSmart(sMSA_ID, iMSA_IMAGESIZE);
            RequestBody mpfileBig = RequestBody.create(MediaType.parse("image/*"), imageInByteBig);
            MultipartBody.Part mpfileToUpload = MultipartBody.Part.createFormData("msa_image", "msa_image", mpfileBig);

            RetrofitClientMsa.getInstance()
                    .setSaveRecordJSON(mpfileToUpload, MSA_ID, MSA_CLR, MSA_LBL, MSA_TITLE, MSA_TEXT, FIO_ID, MSA_FILETYPE, MSA_FILENAME, MSA_IMAGESIZE, MSB_LIST)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MsaDataStructure>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            new NotificationUtils().cancelNotificationWrite();
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                        }

                        @Override
                        public void onNext(MsaDataStructure ds) {

                            StringBuilder sFio_list = new StringBuilder();
                            String sSQL = "select distinct X.* from " +
                                    "(" +
                                    "  select  F.FIO_ID " +
                                    "  from MSB B " +
                                    "  left join FIO F on B.FIO_ID=F.FIO_ID " +
                                    "  where F.FIO_TP=2 and MSA_ID='" + sMSA_ID + "' " +
                                    "     union all  " +
                                    "  select  W.FIO_IDMB " +
                                    "  from FIO W " +
                                    "  where W.FIO_IDGR in " +
                                    "  (select  B.FIO_ID " +
                                    "  from MSB B " +
                                    "  left join FIO F on B.FIO_ID=F.FIO_ID " +
                                    "  where F.FIO_TP=1 and MSA_ID='" + sMSA_ID + "') " +
                                    "  ) X ";

                            Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
                            c.moveToFirst();
                            while (!c.isAfterLast()) {
                                int xfio_id = c.getInt(c.getColumnIndex("FIO_ID"));
                                if (xfio_id != Appl.FIO_ID) {
                                    if (sFio_list.toString().equals(""))
                                        sFio_list = new StringBuilder("" + xfio_id);
                                    else sFio_list.append(",").append(xfio_id);
                                }
                                c.moveToNext();
                            }
                            c.close();

                            if (!sFio_list.toString().equals("")) {
                                List<String> FCM_Send_Codes = new ArrayList<>();
                                sSQL = "select DEV_FCM from DEV D where DEV_FCM<>'' and D.FIO_ID in (" + sFio_list + ") ;";  // ????
                                c = Appl.getDatabase().rawQuery(sSQL, null);
                                c.moveToFirst();
                                while (!c.isAfterLast()) {
                                    String sDEV_FCM = c.getString(c.getColumnIndex("DEV_FCM"));
                                    FCM_Send_Codes.add(sDEV_FCM);
                                    c.moveToNext();
                                }
                                c.close();

                                sSQL = "select MSA_TITLE,MSA_TEXT from MSA where MSA_ID='" + sMSA_ID + "'";
                                c = Appl.getDatabase().rawQuery(sSQL, null);
                                c.moveToFirst();
                                int index = c.getColumnIndex("MSA_TITLE");
                                String sMSA_TITLE = new StringUtils().strnormalize(c.getString(index));
                                index = c.getColumnIndex("MSA_TEXT");
                                String sMSA_TEXT = new StringUtils().strnormalize(c.getString(index));
                                c.close();

                                new PushNotification().send_FCM_message(sMSA_TITLE, sMSA_TEXT, FCM_Send_Codes);
                            }

                            sSQL = "update MSA set MSA_STATE=3 where MSA_ID='" + sMSA_ID + "'";
                            Appl.getDatabase().execSQL(sSQL);

                            new NotificationUtils().cancelNotificationWrite();
                            Events.EventsMessage ev = new Events.EventsMessage();
                            ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
                            GlobalBus.getBus().post(ev);
                            ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
                            GlobalBus.getBus().post(ev);
                        }
                    });
        }
    }


}
