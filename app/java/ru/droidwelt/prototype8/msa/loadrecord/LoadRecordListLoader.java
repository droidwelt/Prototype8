package ru.droidwelt.prototype8.msa.loadrecord;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import ru.droidwelt.prototype8.msa.model.MsaStateListClass;
import ru.droidwelt.prototype8.msa.retrofit.RetrofitClientMsa;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoadRecordListLoader {

    public void getLoadRecordList(final LoadRecordListPresenter cip, final Boolean displayNeedLoginMessage) {

        if (!new PrefUtils().verifyRootAddress())
            return;

        int rl = new PrefUtils().getKvoDownLoad();
        if (rl <= 0) rl = 100;
        final String recordlimit = Integer.toString(rl);

        Cursor c = Appl.getDatabase().rawQuery("select case when (MAX(MSA_NOMER) is null) then 0 else MAX(MSA_NOMER) end as MSA_NOMER from MSA where MSA_STATE in (2,22);", null);
        c.moveToFirst();
        int MSA_NOMERMAX = Integer.parseInt(c.getString(0));
        String smsa_nomermax = Integer.toString(MSA_NOMERMAX);
        c.close();

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            RetrofitClientMsa.getInstance()
                    .getLoadRecordJSON(smsa_nomermax, recordlimit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MsaDataStructure>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            new NotificationUtils().cancelNotificationRead();
                            new InfoUtils().DisplayToastError(Appl.getContext().getString(R.string.s_error_api) + "\n" + e.getMessage());
                        }

                        @Override
                        public void onNext(MsaDataStructure ds) {
                            new NotificationUtils().cancelNotificationRead();
                            ArrayList<MsaStateListClass> stateList = ds.getStateList();
                            if (stateList.get(0).getIerr().equals("1")) {
                                ArrayList<LoadRecordListDataClass> recordList = ds.getLoadRecordList();

                                // загрузка в базу SQLite
                                final SQLiteDatabase mydb = Appl.getDatabase();
                                Appl.getDatabase().rawQuery("PRAGMA journal_mode = 'MEMORY'; ", null);
                                Appl.getDatabase().rawQuery("PRAGMA synchronous = 'OFF'; ", null);

                                for (int i = 0; i < recordList.size(); i++) {
                                    LoadRecordListDataClass pdc = recordList.get(i);
                                    String FIO_ID = pdc.getFio_id();
                                    String MSA_ID = pdc.getMsa_id();
                                    String MSA_DATE = pdc.getMsa_date();
                                    String MSA_TITLE = pdc.getMsa_title();
                                    String MSA_TEXT = pdc.getMsa_text();
                                    String MSA_FILETYPE = pdc.getMsa_filetype();
                                    String MSA_FILENAME = pdc.getMsa_filename();
                                    String MSA_CLR = pdc.getMsa_clr();
                                    String MSA_LBL = pdc.getMsa_lbl();
                                    String MSA_NOMER = pdc.getMsa_nomer();

                                    String sLite =
                                            "INSERT INTO MSA  (MSA_ID, MSA_NOMER, MSA_CLR, MSA_LBL, FIO_ID, MSA_STATE, MSA_DATE, " +
                                                    "MSA_TITLE ,MSA_TEXT, MSA_FILETYPE, MSA_FILENAME) " +
                                                    "VALUES ('" + MSA_ID + "'," + MSA_NOMER + ",'" + MSA_CLR + "','" + MSA_LBL + "'," + FIO_ID + ",2,'" + MSA_DATE + "','" +
                                                    MSA_TITLE + "','" + MSA_TEXT + "','" + MSA_FILETYPE + "','" + MSA_FILENAME + "');";
                                    mydb.execSQL(sLite);
                                }

                                Appl.getDatabase().rawQuery("PRAGMA journal_mode = 'DELETE'; ", null);
                                Appl.getDatabase().rawQuery("PRAGMA synchronous = 'ON'; ", null);

                                if (!(cip == null))
                                    cip.isReadyRecordListData();
                            } else {
                                if (displayNeedLoginMessage)
                                    if (!(cip == null))
                                        cip.needLogin();
                            }
                        }
                    });
        }
    }


}
