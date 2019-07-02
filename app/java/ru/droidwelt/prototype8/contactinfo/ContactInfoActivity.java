package ru.droidwelt.prototype8.contactinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.MapsUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ContactInfoActivity extends AppCompatActivity {

    static final int CONTACTINFO_GALLERY_REQUEST = 2002;
    static final int CONTACTINFO_CAMERA_CAPTURE = 2003;
    static final int CONTACTINFO_PIC_CROP = 2004;
    static final int CONTACTINFO_PERMISSION_REQUEST = 2001;
    static final String imageFilename_photo = "proto8_photo.jpg";
    //  private static final String TAG = ContactInfoActivity.class.getSimpleName();

    private int resultType = RESULT_CANCELED;

    private Uri outputFileUri_photo; // куда сохраняется наше фото

    private Boolean contactIimageModified;
    private int contactImage_Width;
    private int contactImage_Height;

    private String sclt_id, sClt_Comment;
    private ContactInfoPresenter presenter, presenterImage;
    private ProgressBar progressBar;

    private EditText et_cltinfo_clt_comment;
    private SubsamplingScaleImageView iv_cltinfo_clt_image;
    private  String clt_lat,clt_lon,clt_zoom,clt_name,clt_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_contactinfo);
        GlobalBus.getBus().register(this);
        progressBar = findViewById(R.id.progressBar);
        contactIimageModified = false;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle(getString(R.string.s_contactinfo_header));
        sClt_Comment = "";

        et_cltinfo_clt_comment = findViewById(R.id.et_cltinfo_clt_comment);
        et_cltinfo_clt_comment.setSelected(false);
        iv_cltinfo_clt_image = findViewById(R.id.iv_cltinfo_clt_image);
        Intent intent = getIntent();
        sclt_id = intent.getStringExtra("clt_id");

        if (checkPremissoins()) {
            if (!sclt_id.isEmpty()) {
                requestClientInfo(sclt_id);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, CONTACTINFO_PERMISSION_REQUEST);
        }

        if (contactImage_Width > 0) {
            setVisibleInfo();
        }
    }


    protected boolean checkPremissoins() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                !((ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case CONTACTINFO_PERMISSION_REQUEST:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission & ReadExternalStoragePermission & WriteExternalStoragePermission) {
                        if (!sclt_id.isEmpty()) {
                            requestClientInfo(sclt_id);
                        }
                    } else {
                        finish();
                    }
                }
                break;
        }
    }

    //  создание Presenter и запрос данных
    protected void requestClientInfo(String sclt_id) {
        presenter = new ContactInfoPresenter();
        presenter.attachView(this);
        presenter.getContactInfoData(sclt_id);
    }

    //  создание Presenter и запрос картинки
    protected void requestClientInfoImage(String sclt_id) {
        presenterImage = new ContactInfoPresenter();
        presenterImage.attachView(this);
        presenterImage.getContactInfoImageData(sclt_id);
    }


    // вызывается из Presenter по получении ответа о записи
    public void displayContactInfo(ContactInfoDataClass cic) {
        final TextView tv_cltinfo_clt_name = findViewById(R.id.tv_cltinfo_clt_name);
        final TextView tv_cltinfo_clt_addr = findViewById(R.id.tv_cltinfo_clt_addr);
        final TextView tv_cltinfo_clt_phone = findViewById(R.id.tv_cltinfo_clt_phone);

        tv_cltinfo_clt_name.setText(cic.getClt_name());
        tv_cltinfo_clt_addr.setText(cic.getClt_addr());
        tv_cltinfo_clt_phone.setText(cic.getClt_phone());
        et_cltinfo_clt_comment.setText(cic.getClt_comment());
        sClt_Comment = et_cltinfo_clt_comment.getText().toString();

        // запрос картинки
        requestClientInfoImage(sclt_id);

        clt_lat = new StringUtils().strnormalize(cic.getClt_lat());
        clt_lon = new StringUtils().strnormalize(cic.getClt_lon());
        clt_zoom = new StringUtils().strnormalize(cic.getClt_zoom());
        clt_name = new StringUtils().strnormalize(cic.getClt_name());
        clt_address = new StringUtils().strnormalize(cic.getClt_addr());

        ImageButton ib_map = findViewById(R.id.ib_map3);
        if (new MapsUtils().verifyCoord(clt_lat, clt_lon, clt_zoom)) {
            ib_map.setImageResource(R.mipmap.main_maps);
            ib_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayMap ();
                }
            });
        }  else {
            ib_map.setImageResource(R.mipmap.ic_empty);
        }
    }


    // вызывается из Presenter по получении ответа о картинке
    public void displayContactInfoImage(ContactInfoDataClass cic) {
        if (contactImage_Width > 0) {
            iv_cltinfo_clt_image.setImage(ImageSource.uri(Appl.CONTACT_URI));
            setImageViewHeight();
        }
    }

    private void displayMap () {
        double dlat = new MapsUtils().strToDouble (clt_lat);
        double dlon = new MapsUtils().strToDouble (clt_lon);
        double dzoom = new MapsUtils().strToDouble (clt_zoom);
        new MapsUtils().showMaps( this, dlat, dlon, dzoom,clt_name,clt_address);
    }

    public void setImageViewHeight() {
        if (contactImage_Height > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_cltinfo_clt_image.getLayoutParams(); // получаем параметры
            LinearLayout ll = findViewById(R.id.ll_image);
            int h = ll.getHeight();
            int w = ll.getWidth();
            if (h < w) h = w;
            if (h > contactImage_Height) h = contactImage_Height;
            params.height = h;
            iv_cltinfo_clt_image.setLayoutParams(params);
        }
    }


    // подключение меню----------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_contactinfo, menu);
        return true;
    }

    public boolean verifyNeedToSave() {
        String s = new StringUtils().strnormalize(et_cltinfo_clt_comment.getText().toString());
        return (contactIimageModified | !(sClt_Comment.equals(s)));
    }


    // выбор из меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Сохранить
            case R.id.action_clientinfo_save:
                if (verifyNeedToSave()) {
                    saveClientInfo();
                }
                return true;

            // Очистить картинку
            case R.id.mi_edit_picture_clear: {
                if (contactImage_Width > 0) {
                    contactIimageModified = true;
                    iv_cltinfo_clt_image.setImage(ImageSource.resource(R.drawable.ic_photo_empty));
                    contactImage_Height = 0;
                    contactImage_Width = 0;
                    setImageViewHeight();
                }
            }
            return true;

            // Выбор картинки
            case R.id.mi_edit_picture_choice:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, CONTACTINFO_GALLERY_REQUEST);
                return true;

            // сделать фото
            case R.id.mi_edit_picture_takephoto:
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                deleteTempFile();
                outputFileUri_photo = Uri.fromFile(new File(Appl.TEMP_PATH, imageFilename_photo));
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri_photo);
                startActivityForResult(captureIntent, CONTACTINFO_CAMERA_CAPTURE);
                return true;

            // обрезать изображение
            case R.id.mi_edit_picture_crop:
                if (contactImage_Width > 0) {
                    Bitmap imageBig;
                    imageBig = new GraphicUtils().getImageBig(Appl.CONTACT_TEMP, 0);
                    outputFileUri_photo = getImageUri(this, imageBig);
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    //  cropIntent.setDataAndType( Uri.fromFile(new File(Appl.CONTACT_TEMP)), "image/*");
                    cropIntent.setDataAndType(outputFileUri_photo, "image/*");
                    cropIntent.putExtra("crop", "true");
                    cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    cropIntent.putExtra("noFaceDetection", true);
                    startActivityForResult(cropIntent, CONTACTINFO_PIC_CROP);
                }
                return true;


            // повернуть влево
            case R.id.mi_edit_picture_rotate_left:
                if (rotateImage(-1))
                    setVisibleInfo();
                return true;

            // повернуть вправо
            case R.id.mi_edit_picture_rotate_right:
                if (rotateImage(1))
                    setVisibleInfo();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CONTACTINFO_GALLERY_REQUEST:
                    try {
                        Uri selectedImageUri = data.getData();
                        if (saveUriToTemp(selectedImageUri, new PrefUtils().getImageMaxSize())) {
                            setImageViewHeight();
                            contactIimageModified = true;
                            setVisibleInfo();
                        } else {
                            contactImage_Width = 0;
                            contactImage_Height = 0;
                        }
                    } catch (Exception e) {
                        new InfoUtils().DisplayToastError(getString(R.string.s_no_request_image_from_gallery) + "\n" + e.toString());
                        //   Log.e(TAG, e.toString());
                    }
                    break;

                case CONTACTINFO_CAMERA_CAPTURE:
                    try {
                        if (saveUriToTemp(outputFileUri_photo, new PrefUtils().getImageMaxSize())) {
                            setImageViewHeight();
                            contactIimageModified = true;
                            setVisibleInfo();
                        } else {
                            contactImage_Width = 0;
                            contactImage_Height = 0;
                        }
                    } catch (Exception e) {
                        new InfoUtils().DisplayToastError(getString(R.string.s_no_request_image_from_camera) + "\n" + e.toString());
                        break;
                    }
                    break;

                case CONTACTINFO_PIC_CROP:
                    try {
                        Uri selectedImage = data.getData();
                        if (saveUriToTemp(selectedImage, new PrefUtils().getImageMaxSize())) {
                            setImageViewHeight();
                            contactIimageModified = true;
                            setVisibleInfo();
                        } else {
                            contactImage_Width = 0;
                            contactImage_Height = 0;
                        }
                    } catch (Exception e) {
                        new InfoUtils().DisplayToastError(getString(R.string.s_no_prepate_after_crop) + "\n" + e.toString());
                        break;
                    }
                    break;
            }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 75, bytes);//100
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*  mode=-1 влево, против часовой стрелки. mode=1 вправо, по часовой стрелке 	 */
    protected boolean rotateImage(int mode) {
        if (contactImage_Width > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            if (mode == -1) matrix.postRotate(-90);
            if (mode == 1) matrix.postRotate(90);
            Bitmap bmin = new GraphicUtils().getImageBig(Appl.CONTACT_TEMP, 0);
            if (bmin == null)
                return false;
            Bitmap bmout = Bitmap.createBitmap(bmin, 0, 0, bmin.getWidth(), bmin.getHeight(), matrix, true);

            contactImage_Width = bmout.getWidth();
            contactImage_Height = bmout.getHeight();
            contactIimageModified = true;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmout.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            byte[] decodedStringBig = bos.toByteArray();

            new GraphicUtils().byteArrayToFile(decodedStringBig, Appl.CONTACT_TEMP);
            return true;
        } else
            return false;
    }


    public void isContactInfoSaved(String sClt_Comment_saved) {
        contactIimageModified = false;
        contactImage_Width = 0;
        contactImage_Height = 0;
        sClt_Comment = sClt_Comment_saved;
        resultType = RESULT_OK;

        Events.EventsMessage ev = new Events.EventsMessage();
        ev.setMes_code(Events.EB_CLIENTINTO_UPDATE);
        GlobalBus.getBus().post(ev);
    }


    protected void saveClientInfo() {
        String sclt_comment_new = new StringUtils().strnormalize(et_cltinfo_clt_comment.getText().toString());
        String sclt_imagesize = "0";
        if (contactImage_Width > 0)
            sclt_imagesize = "1";
        new ContactInfoLoader().saveClientInfo(this, sclt_id, sclt_comment_new, sclt_imagesize);
    }


    public void setVisibleInfo() {
        if (contactImage_Width > 0) {
            iv_cltinfo_clt_image.setImage(ImageSource.uri(Appl.CONTACT_URI));
            setImageViewHeight();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {// защита от закрытия по Back Обработка нажатия
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                smart_exit();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void smart_exit() {
        if (verifyNeedToSave()) {

            new AlertDialog.Builder(this, R.style.AlertDialogColorButton)
                    .setTitle(getString(R.string.s_dlg_confirm))
                    .setMessage(getString(R.string.s_dlg_date_changed))
                    .setNegativeButton((getString(R.string.s_dlg_yes)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    saveClientInfo();
                                    resultType = RESULT_OK;
                                    //  finish();
                                }
                            })
                    .setPositiveButton((getString(R.string.s_dlg_no)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    setResult(RESULT_CANCELED);
                                    contactIimageModified = false;
                                    contactImage_Height = 0;
                                    contactImage_Width = 0;
                                    finish();
                                    new PrefUtils().animateFinish( ContactInfoActivity.this);
                                }
                            })
                    .create()
                    .show();
        } else {
            setResult(resultType);
            contactIimageModified = false;
            contactImage_Height = 0;
            contactImage_Width = 0;
            finish();
            new PrefUtils().animateFinish(this);
        }
    }

    public void deleteTempFile() {
        try {
            File file = new File(Appl.TEMP_PATH, imageFilename_photo);
            if (file.exists()) {
                if (!file.delete()) {
                    new InfoUtils().DisplayToastError("Не удалось удалить файл " + file.getName());
                }
            }

        } catch (Exception ignored) {
        }
        try {
            String imageFilename_crop = "proto8_crop.jpg";
            File file = new File(Appl.TEMP_PATH, imageFilename_crop);
            if (file.exists()) {
                if (!file.delete()) {
                    new InfoUtils().DisplayToastError("Не удалось удалить файл " + file.getName());
                }
            }
        } catch (Exception ignored) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
        if (presenterImage != null)
            presenterImage.detachView();
        GlobalBus.getBus().unregister(this);
        new NotificationUtils().cancelNotification(0);
    }


    @Subscribe
    public void getMessage(Events.EventsMessage ev) {
        if (ev.getMes_code().equals(Events.EB_CONTACTINFO_INDICATOR_ON))
            progressBar.setVisibility(View.VISIBLE);

        if (ev.getMes_code().equals(Events.EB_CONTACTINFO_INDICATOR_OFF))
            progressBar.setVisibility(View.INVISIBLE);
    }


    public boolean saveUriToTemp(Uri uri, int maxSize) {
        Bitmap bm = new GraphicUtils().getImageBig(uri, maxSize);

        if (bm != null) {
            contactImage_Width = bm.getWidth();
            contactImage_Height = bm.getHeight();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            byte[] resall = bos.toByteArray();
            new GraphicUtils().byteArrayToFile(resall, Appl.CONTACT_TEMP);
            return true;
        } else {
            return false;
        }
    }


    public int getContactImage_Width() {
        return contactImage_Width;
    }

    public void setContactImage_Width(int contactImage_Width) {
        this.contactImage_Width = contactImage_Width;
    }

    public int getContactImage_Height() {
        return contactImage_Height;
    }

    public void setContactImage_Height(int contactImage_Height) {
        this.contactImage_Height = contactImage_Height;
    }

    public Boolean getContactIimageModified() {
        return contactIimageModified;
    }

    public void setContactIimageModified(Boolean contactIimageModified) {
        this.contactIimageModified = contactIimageModified;
    }

}