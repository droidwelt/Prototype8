package ru.droidwelt.prototype8.msa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.choice.ChoiceMsaActionActivity;
import ru.droidwelt.prototype8.msa.loadimage.LoadImageListLoader;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.OnSwipeTouchListener;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;


@SuppressWarnings("Annotator")
public class MsaMainRecyclerAdapter extends RecyclerView.Adapter<MsaMainRecyclerAdapter.ViewHolder> {

    private MsaMainActivity msamain;

    private MsaMainRecyclerAdapter mmRa;

    private  List<Integer> afio_id = new ArrayList<>();
    private  List<Bitmap> afio_image = new ArrayList<>();

    public void setMsaMainActivity(MsaMainActivity a) {
        msamain = a;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int lyRes;
        if (Appl.APP_STYLE.equalsIgnoreCase("C")) {
            lyRes = R.layout.activity_msa_item;
        } else {
            lyRes = R.layout.activity_msa_item_flat;
        }
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(lyRes, parent, false);
        return new ViewHolder(itemLayoutView);
    }


    @Override
    public int getItemCount() {
        return msamain.list_msa.size();
    }


    private void displayFileType(@NonNull ViewHolder viewHolder, int position) {
        MsaMainDataStructure myListItem = msamain.list_msa.get(position);
        String filetype = myListItem.msa_filetype;
        if (filetype.equals("")) {
            viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_empty);  // clip_empty   color_msa_rv_selected
        } else {
            if (viewHolder.MSA_IMAGESIZE > 0) {
                viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_other);
                if (filetype.equalsIgnoreCase("doc"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_doc);
                if (filetype.equalsIgnoreCase("docx"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_doc);
                if (filetype.equalsIgnoreCase("pdf"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_pdf);
                if (filetype.equalsIgnoreCase("zip"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_zip);
                if (filetype.equalsIgnoreCase("xls"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_xls);
                if (filetype.equalsIgnoreCase("xlsx"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_xls);
                if (filetype.equalsIgnoreCase("mp4"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_video);
                if (filetype.equalsIgnoreCase("mp3"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_audio);
                if (filetype.equalsIgnoreCase("jpg"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_image);
            } else {
                viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_other);
                if (filetype.equalsIgnoreCase("doc"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_doc_gray);
                if (filetype.equalsIgnoreCase("docx"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_doc_gray);
                if (filetype.equalsIgnoreCase("pdf"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_pdf_gray);
                if (filetype.equalsIgnoreCase("zip"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_zip_gray);
                if (filetype.equalsIgnoreCase("xls"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_xls_gray);
                if (filetype.equalsIgnoreCase("xlsx"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_xls_gray);
                if (filetype.equalsIgnoreCase("mp4"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_video_gray);
                if (filetype.equalsIgnoreCase("mp3"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_audio_gray);
                if (filetype.equalsIgnoreCase("jpg"))
                    viewHolder.iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_image_gray);
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        mmRa = this;
        MsaMainDataStructure myListItem = msamain.list_msa.get(position);
        viewHolder.MSA_ID = myListItem.msa_id;
        viewHolder.MSA_FILENAME = myListItem.msa_filename;
        viewHolder.MSA_FILETYPE = myListItem.msa_filetype;
        viewHolder.MSA_IMAGESIZE = myListItem.imagesize;
        viewHolder.tv_mesitem_title.setText(myListItem.msa_title);
        viewHolder.tv_mesitem_text.setText(myListItem.msa_text);
        viewHolder.tv_mesitem_fioname.setText(myListItem.fio_name);
        viewHolder.tv_mesitem_date.setText(myListItem.msa_date);
        viewHolder.ssl_msa_view.setImage(ImageSource.resource(R.mipmap.ic_1x1));

        LinearLayout.LayoutParams ly_param = (LinearLayout.LayoutParams) viewHolder.ly_ssl_adapter.getLayoutParams();
        ly_param.height = 1;
        viewHolder.ly_ssl_adapter.setLayoutParams(ly_param);

        String sClr = myListItem.msa_clr;
        if (viewHolder.iv_mesitem_layout != null)
            viewHolder.iv_mesitem_layout.setBackgroundColor(new GraphicUtils().getColorByString(sClr));  // внутренность

        if (Appl.APP_STYLE.equalsIgnoreCase("C")) {
            if (viewHolder.iv_mesitem_cv != null)
                viewHolder.iv_mesitem_cv.setCardBackgroundColor(new GraphicUtils().getColorByString(sClr));  // рамка
        }

        String MSA_LBL = myListItem.msa_lbl;
        viewHolder.iv_mesitem_lbl.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer(MSA_LBL, 0));

        displayFileType(viewHolder, position);

        if (myListItem.fio_id > 0) {
            Bitmap theImage = null;
            for (int i = 0; i < (afio_id.size() - 1); i++)
                if (afio_id.get(i) == myListItem.fio_id) {
                    theImage = afio_image.get(i);
                    if (theImage != null) {
                        viewHolder.iv_mesitem_image.setImageBitmap(theImage);
                       // int w = theImage.getWidth();
                    }
                    break;
                }
            if (theImage == null) {
                viewHolder.iv_mesitem_image.setImageResource(R.drawable.ic_avatar_unknown2);
            }
        } else {
            viewHolder.iv_mesitem_image.setImageResource(R.drawable.ic_avatar_unknown2);
        }
        viewHolder.itemView.setLongClickable(true);
    }


    private void load_image_from_SQLite(final SubsamplingScaleImageView ssl_msa_view,
                                        final LinearLayout ll,
                                        final String MSA_ID,
                                        final ImageView iv_mesitem_filetype) {

        byte[] resall = new MsaUtilsSQLite().getByteArrayFromMsaImaget(MSA_ID);

        if (resall != null) {
            final Bitmap image = BitmapFactory.decodeByteArray(resall, 0, resall.length);
            ssl_msa_view.setEnabled(true);
            LinearLayout.LayoutParams ly_param = (LinearLayout.LayoutParams) ll.getLayoutParams();
            int pic_height = image.getHeight();
            int new_height = ll.getWidth();
            if (pic_height < new_height)
                new_height = pic_height;
            ly_param.height = new_height;

            ll.setLayoutParams(ly_param);
            ssl_msa_view.setImage(ImageSource.bitmap(image));
            iv_mesitem_filetype.setImageResource(R.mipmap.ic_filetype_image);

         /*   new Thread(new Runnable() {
                public void run() {
                    ssl_msa_view.post(new Runnable() {
                        public void run() {
                            ssl_msa_view.setImage(ImageSource.bitmap(image));
                        }
                    });
                }
            }).start(); */
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_mesitem_title, tv_mesitem_text, tv_mesitem_fioname, tv_mesitem_date;
        ImageView iv_mesitem_lbl, iv_mesitem_filetype, iv_mesitem_image;
        LinearLayout iv_mesitem_layout, ly_ssl_adapter;
        android.support.v7.widget.CardView iv_mesitem_cv;
        String MSA_ID, MSA_FILETYPE, MSA_FILENAME;
        int MSA_IMAGESIZE;
        SubsamplingScaleImageView ssl_msa_view;

        ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

                afio_id.clear();
                afio_image.clear();

                String sSQL = "select FIO_ID,FIO_IMAGE from FIO  where FIO_TP in (1,2) order by FIO_ID";
                Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    afio_id.add(c.getInt(0));
                    if (c.getBlob(1) == null) afio_image.add(null);
                    else {
                        byte[] bt = c.getBlob(1);
                        afio_image.add(BitmapFactory.decodeByteArray(bt, 0, bt.length));
                    }
                    c.moveToNext();
                }
                c.close();



            tv_mesitem_title = itemLayoutView.findViewById(R.id.tv_mesitem_title);
            tv_mesitem_text = itemLayoutView.findViewById(R.id.tv_mesitem_text);
            tv_mesitem_fioname = itemLayoutView.findViewById(R.id.tv_mesitem_fioname);
            tv_mesitem_date = itemLayoutView.findViewById(R.id.tv_mesitem_date);
            iv_mesitem_lbl = itemLayoutView.findViewById(R.id.iv_mesitem_lbl);
            iv_mesitem_filetype = itemLayoutView.findViewById(R.id.iv_mesitem_filetype);
            iv_mesitem_image = itemLayoutView.findViewById(R.id.iv_mesitem_image);
            iv_mesitem_layout = itemLayoutView.findViewById(R.id.iv_mesitem_layout);
            ssl_msa_view = itemLayoutView.findViewById(R.id.ssl_msa_view);
            ly_ssl_adapter = itemLayoutView.findViewById(R.id.ly_ssl_adapter);

            if (Appl.APP_STYLE.equalsIgnoreCase("C")) {
                iv_mesitem_cv = itemLayoutView.findViewById(R.id.iv_mesitem_cv);
            }

            itemLayoutView.setOnTouchListener(new OnSwipeTouchListener(itemLayoutView.getContext()) {

                public void onSwipeTop() {
                    //  Toast.makeText(Appl.getContext(), "top", Toast.LENGTH_SHORT).show();
                }

                public void onSwipeRight() {
                    //   Toast.makeText(Appl.getContext(), "right", Toast.LENGTH_SHORT).show();
                    if (!MSA_ID.isEmpty() & (Appl.MSA_MODEVIEW == 22)) {
                        Appl.MSA_ID = MSA_ID;
                        Appl.MSA_POS = getAdapterPosition();
                        new MsaUtilsSQLite().change_record_state_SQLite(2);
                        Events.EventsMessage ev = new Events.EventsMessage();
                        ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
                        GlobalBus.getBus().post(ev);
                    }
                }

                public void onSwipeLeft() {
                    //   Toast.makeText(Appl.getContext(), "left", Toast.LENGTH_SHORT).show();
                    if (!MSA_ID.isEmpty() & (Appl.MSA_MODEVIEW == 2)) {
                        Appl.MSA_ID = MSA_ID;
                        Appl.MSA_POS = getAdapterPosition();
                        new MsaUtilsSQLite().change_record_state_SQLite(22);
                        Events.EventsMessage ev = new Events.EventsMessage();
                        ev.setMes_code(Events.EB_MSAMAIN_REFRESH_RECORDS);
                        GlobalBus.getBus().post(ev);
                    }
                }

                public void onSwipeBottom() {
                    //  Toast.makeText(Appl.getContext(), "bottom", Toast.LENGTH_SHORT).show();
                }

                public void onClick() {
                    if (!MSA_ID.isEmpty()) {
                        if ((Appl.MSA_MODEVIEW == 0) || (Appl.MSA_MODEVIEW == 10)) {
                            Appl.MSA_MODEEDIT = Appl.MSA_MODEVIEW;
                            Appl.MSA_ID = MSA_ID;
                            Appl.MSA_POS = getAdapterPosition();
                            Intent mesReposte = new Intent(msamain, MsaEditActivity.class);
                            msamain.startActivityForResult(mesReposte, Appl.EXIT_CODE_MSAEDIT);
                            new PrefUtils().animateStart(msamain);
                        } else {
                            if (!MSA_FILETYPE.equals("")) {  // A
                                if (MSA_IMAGESIZE > 0) {  // B , т.е. вложение загружено
                                    if (MSA_FILETYPE.equalsIgnoreCase("jpg"))
                                        load_image_from_SQLite(ssl_msa_view, ly_ssl_adapter, MSA_ID, iv_mesitem_filetype);

                                } else // B
                                {
                                    new LoadImageListLoader().getLoadImageList(mmRa, getAdapterPosition(), MSA_FILETYPE, ssl_msa_view, ly_ssl_adapter, MSA_ID, iv_mesitem_filetype);
                                }
                            }  // A
                        }
                    }
                }

                public void onLongClick() {
                    if (!MSA_ID.isEmpty()) {
                        Appl.MSA_ID = MSA_ID;
                        Appl.MSA_POS = getAdapterPosition();

                        Intent viewMenu = new Intent(msamain, ChoiceMsaActionActivity.class);

                        viewMenu.putExtra("MSA_ID", MSA_ID);
                        viewMenu.putExtra("MSA_FILETYPE", MSA_FILETYPE);
                        viewMenu.putExtra("MSA_IMAGESIZE", MSA_IMAGESIZE);
                        viewMenu.putExtra("MSA_FILENAME", MSA_FILENAME);
                        msamain.startActivity(viewMenu);
                    }
                }
            });
        }
    }


    public void isReadyImageListData(final SubsamplingScaleImageView ssl_msa_view, final LinearLayout ll, final String smsa_id, final ImageView iv_mesitem_filetype) {
        load_image_from_SQLite(ssl_msa_view, ll, smsa_id, iv_mesitem_filetype);
    }


}