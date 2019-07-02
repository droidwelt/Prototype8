package ru.droidwelt.prototype8.contactinfo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.FileUtils;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;

import static android.util.Base64.DEFAULT;

public class ContactInfoPresenter {

    private ContactInfoActivity view;

    public void attachView(ContactInfoActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void getContactInfoData(String sClt_id) {
        new ContactInfoLoader().getContactInfo(view, this, sClt_id);
    }

    public void getContactInfoImageData(String sClt_id) {
        new ContactInfoLoader().getContactImageInfo(view, this, sClt_id);
    }

    public void isReadyContactInfoData(ContactInfoActivity act, ContactInfoDataClass cic) {
        if (view != null) {
            view.displayContactInfo(cic);
        }
    }


    public void isReadyContactInfoImageData(ContactInfoActivity act, ContactInfoDataClass cic) {
        if (view != null) {
            String sImage = cic.getClt_image();
            new FileUtils().deleteTempFile(Appl.CONTACT_TEMP);
            if (!(sImage.isEmpty())) {
                byte[] decodedStringBig = Base64.decode(sImage, DEFAULT);

                new GraphicUtils().byteArrayToFile(decodedStringBig, Appl.CONTACT_TEMP);

                Bitmap bm = BitmapFactory.decodeByteArray(decodedStringBig, 0, decodedStringBig.length);
                act.setContactImage_Width(bm.getWidth());
                act.setContactImage_Height(bm.getHeight());
            }
            view.displayContactInfoImage(cic);
        }
    }


}
