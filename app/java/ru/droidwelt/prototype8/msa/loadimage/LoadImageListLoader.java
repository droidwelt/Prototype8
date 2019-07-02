package ru.droidwelt.prototype8.msa.loadimage;

import android.content.ContentValues;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.msa.MsaMainRecyclerAdapter;
import ru.droidwelt.prototype8.msa.model.MsaDataStructure;
import ru.droidwelt.prototype8.msa.retrofit.RetrofitClientMsa;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.util.Base64.DEFAULT;

public class LoadImageListLoader {


    public void getLoadImageList(final MsaMainRecyclerAdapter mmRa,
                                 final int position,
                                 final String filetype,
                                 final SubsamplingScaleImageView ssl_msa_view,
                                 final LinearLayout ll,
                                 final String smsa_id,
                                 final ImageView iv_mesitem_filetype) {

        if (new NetworkUtils().checkConnection(true)) {
            new NotificationUtils().createNotificationRead();

            RetrofitClientMsa.getInstance()
                    .getLoadImageJSON(smsa_id)
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
                            ArrayList<LoadImageListDataClass> loadimageList = ds.getLoadImageList();
                            if (loadimageList.size() > 0) {
                                LoadImageListDataClass cic = loadimageList.get(0);

                                byte[] decodedStringBig = Base64.decode(cic.getMsa_image(), DEFAULT);
                                int imgsize = decodedStringBig.length;
                                ContentValues ediMessage = new ContentValues();
                                ediMessage.put("MSA_IMAGE", decodedStringBig);
                                Appl.getDatabase().update("MSA", ediMessage, "MSA_ID='" + smsa_id + "'", null);

                                Events.EventsMessage ev = new Events.EventsMessage();
                                ev.setMes_code(Events.EB_MSAMAIN_SET_IMAGESIZE);
                                ev.setMes_str(smsa_id);
                                ev.setMes_int(imgsize);
                                GlobalBus.getBus().post(ev);
                                ev.setMes_code(Events.EB_MAIN_REFRESH_MAINMENU);
                                GlobalBus.getBus().post(ev);

                                if ((ssl_msa_view != null) && (filetype.equals("jpg"))) {
                                    mmRa.isReadyImageListData(ssl_msa_view, ll, smsa_id, iv_mesitem_filetype);
                                } else {
                                    Events.EventsMessage ev1 = new Events.EventsMessage();
                                    ev1.setMes_code(Events.EB_MSAMAIN_ITEM_CHANGED);
                                    ev1.setMes_int(position);
                                    GlobalBus.getBus().post(ev1);
                                }
                            }
                        }

                    });
        }
    }


}
