package ru.droidwelt.prototype8.gallery;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.Objects;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.contactinfo.ContactInfoDataClass;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.MapsUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;

import static android.util.Base64.DEFAULT;

public class GalleryFragment extends Fragment {

    static final String PAGE_NUMBER = "arg_page_number";
    static final String CLT_ID = "clt_id";

    private GalleryActivity gal;
    int pageNumber;
    String clt_id, clt_name, clt_lat, clt_lon, clt_zoom, clt_address;


    GalleryFragment newInstance(GalleryActivity ga, int page, String clt_id) {
        GalleryFragment pageFragment = new GalleryFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(PAGE_NUMBER, page);
        arguments.putString(CLT_ID, clt_id);
        pageFragment.setArguments(arguments);
        pageFragment.gal = ga;
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNumber = getArguments().getInt(PAGE_NUMBER);
        }
        clt_id = getArguments().getString(CLT_ID);
    }


    @SuppressWarnings("Annotator")
    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gallery_fragment, null);
        TextView tvPage = view.findViewById(R.id.tvPage);
        String s = "Page " + pageNumber + "  CLT_ID " + clt_id;
        tvPage.setText(s);

        String frTag = getTag();
        new GalleryInfoLoader().getGalleryInfo(gal, frTag, clt_id);
        new GalleryInfoLoader().getGalleryInfoImage(gal, frTag, clt_id);
     //      new GalleryInfoLoader().getGalleryInfoFr(fr, frTag, clt_id);
     //      new GalleryInfoLoader().getGalleryInfoImageFr(fr, frTag, clt_id);
        // Log.i ("EEE___","onCreateView:"+s);
        return view;
    }


    public void isReadyContactInfoData(ContactInfoDataClass cic) {
        if (getView() != null) {
            TextView tv_cltinfo_clt_name = getView().findViewById(R.id.tv_galinfo_clt_name);
            TextView tv_cltinfo_clt_addr = getView().findViewById(R.id.tv_galinfo_clt_addr);
            TextView tv_galinfo_clt_phone = getView().findViewById(R.id.tv_galinfo_clt_phone);
            TextView tv_galinfo_clt_comment = getView().findViewById(R.id.tv_galinfo_clt_comment);

            clt_name = cic.getClt_name();
            clt_address = cic.getClt_addr();
            tv_cltinfo_clt_name.setText(cic.getClt_name());
            tv_cltinfo_clt_addr.setText(cic.getClt_addr());
            tv_galinfo_clt_phone.setText(cic.getClt_phone());
            tv_galinfo_clt_comment.setText(cic.getClt_comment());
            clt_lat = new StringUtils().strnormalize(cic.getClt_lat());
            clt_lon = new StringUtils().strnormalize(cic.getClt_lon());
            clt_zoom = new StringUtils().strnormalize(cic.getClt_zoom());

            ImageButton ib_map = getView().findViewById(R.id.ib_map);
            if (new MapsUtils().verifyCoord(clt_lat, clt_lon, clt_zoom)) {
                ib_map.setImageResource(R.mipmap.main_maps);
                ib_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        double dlat = new MapsUtils().strToDouble(clt_lat);
                        double dlon = new MapsUtils().strToDouble(clt_lon);
                        double dzoom = new MapsUtils().strToDouble(clt_zoom);
                        new MapsUtils().showMaps(getActivity(), dlat, dlon, dzoom, clt_name, clt_address);
                    }
                });
            } else {
                ib_map.setImageResource(R.mipmap.ic_empty);
            }
        }
    }


    public void isReadyContactInfoImageData(ContactInfoDataClass cic) {
        if (getView() != null) {
            Bitmap bm = null;
            int contactImage_Width = 0;
            int contactImage_Height = 0;
            String sImage = cic.getClt_image();
            if ((sImage != null) && (!sImage.equals(""))) {
                byte[] decodedStringBig = Base64.decode(sImage, DEFAULT);
                bm = BitmapFactory.decodeByteArray(decodedStringBig, 0, decodedStringBig.length);
                if (bm != null) {
                    contactImage_Width = bm.getWidth();
                    contactImage_Height = bm.getHeight();
                } else {
                    new InfoUtils().DisplayToastError("bm is null; decodedStringBig.length=" + decodedStringBig.length);
                }
            }

            if ((bm != null) && (contactImage_Width > 0)) {
                SubsamplingScaleImageView iv_galinfo_clt_image = getView().findViewById(R.id.iv_galinfo_clt_image);
                iv_galinfo_clt_image.setImage(ImageSource.bitmap(bm));
                setImageViewHeight(iv_galinfo_clt_image, contactImage_Height);
            }
        }
    }


    public void setImageViewHeight(SubsamplingScaleImageView iv, int contactImage_Height) {
        if ((contactImage_Height > 0) && (getView() != null)) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams(); // получаем параметры
            LinearLayout ll = getView().findViewById(R.id.ll_gal_image);
            int h = ll.getHeight();
            int w = ll.getWidth();
            if (h < w) h = w;
            if (h > contactImage_Height) h = contactImage_Height;
            params.height = h;
            iv.setLayoutParams(params);
        }
    }




}