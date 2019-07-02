package ru.droidwelt.prototype8.msa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.choice.ChoiceClrActivity;
import ru.droidwelt.prototype8.choice.ChoiceFioActivity;
import ru.droidwelt.prototype8.choice.ChoiceLblActivity;
import ru.droidwelt.prototype8.msa.saverecord.SaveRecordListLoader;
import ru.droidwelt.prototype8.msa.sqlite.MsaExportSQLite;
import ru.droidwelt.prototype8.msa.sqlite.MsaImportSQLite;
import ru.droidwelt.prototype8.msa.sqlite.MsaUtilsSQLite;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.FileUtils;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.common.InfoUtils;
import ru.droidwelt.prototype8.utils.common.InputTextFilter;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.NotificationUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.common.StringUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;


public class MsaEditActivity extends AppCompatActivity {

    static final int MAX_IMAGE_SIZE = 2048;

    static final int MSA_EDIT_GALLERY_REQUEST = 3001;
    static final int MSA_EDIT_CAMERA_CAPTURE = 3002;
    static final int MSA_EDIT_PIC_CROP = 3003;
    static final int MSA_EDIT_PICK_ACTION = 3004;

    private Uri outputFileUri_photo; // куда сохраняется наше фото
    private Uri outputFileUri_crop; // куда сохраняется наше фото
    private final String imageFilename_photo = "proto_photo.jpg";
    public String MSA_TITLE_FIRST, MSA_TEXT_FIRST, MSA_CLR, MSA_LBL, MSA_CLR_FIRST, MSA_LBL_FIRST;
    public EditText et_msa_title, et_msa_text, et_msa_filename;
    public TextView tv_msa_fio, tv_msa_datetime, tv_send, tv_msa_filetype, tv_msa_imagesize;
    private LinearLayout ly_ssl;
    private LinearLayout ly_edit;
    public SubsamplingScaleImageView ssl_msa_edit;
    public  DisplayMetrics displaymetrics;
    public  String MSA_FILETYPE, MSA_FILENAME;

    public  int MSA_IMAGESIZE;
    public ImageButton ib_mesedit_lbl, ib_mesedit_clr;

    public  Bitmap imageBig;
    public  boolean imageBigModified;

    public  MenuItem mi_edit_picture_clear;
    public  MenuItem mi_edit_picture_export;
    public  MenuItem mi_edit_picture_crop;
    public  MenuItem mi_edit_deletedraft;
    public  MenuItem mi_edit_picture_rotate_left;
    public  MenuItem mi_edit_picture_rotate_right;
    public  MenuItem mi_edit_picture_reduce;
    public  MenuItem mi_edit_picture_import;
    public  MenuItem mi_edit_picture_choice;
    public  MenuItem mi_edit_picture_takephoto;
    public  MenuItem mi_edit_attachment_clear;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setTitle(R.string.s_message_edit);
        setContentView(R.layout.activity_msaedit);
        GlobalBus.getBus().register(this);
        imageBig = null;
        imageBigModified = false;
   /*     imageBig_Heigth = 0;
        imageBig_Width = 0;
        imageBig_Size = 0;*/
        MSA_TITLE_FIRST = "";
        MSA_TEXT_FIRST = "";
        MSA_FILETYPE = "";
        MSA_FILENAME = "";
        MSA_CLR = "0";
        MSA_LBL = "0";
        MSA_CLR_FIRST = "0";
        MSA_LBL_FIRST = "0";
        MSA_IMAGESIZE = 0;
        displaymetrics = getMyDisplayMetrics();

        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Appl.BarColor_0));

        ly_ssl = findViewById(R.id.ly_ssl);
        ly_edit = findViewById(R.id.ly_edit);
        et_msa_text = findViewById(R.id.et_mesedit_text);
        et_msa_title = findViewById(R.id.et_mesedit_title);
        tv_msa_filetype = findViewById(R.id.tv_mesedit_filetype);
        et_msa_filename = findViewById(R.id.et_mesedit_filename);
        tv_msa_imagesize = findViewById(R.id.tv_mesedit_imagesize);
        tv_msa_fio = findViewById(R.id.tv_mesedit_fio);
        tv_msa_datetime = findViewById(R.id.tv_mesedit_datetime);
        tv_send = findViewById(R.id.tv_mesedit_send);
        tv_send.setOnClickListener(onClickButtonListtiner);
        ssl_msa_edit = findViewById(R.id.ssl_msa_edit);
        ib_mesedit_lbl = findViewById(R.id.ib_mesedit_lbl);
        ib_mesedit_lbl.setOnClickListener(onClickButtonListtiner);
        ib_mesedit_clr = findViewById(R.id.ib_mesedit_clr);
        ib_mesedit_clr.setOnClickListener(onClickButtonListtiner);

        ImageView ib_mesedit_fio = findViewById(R.id.ib_mesedit_fio);
        ib_mesedit_fio.setOnClickListener(onClickButtonListtiner);

        load_msa_record_from_SQLite();
        display_send_list();

        et_msa_text.setFilters(new InputFilter[]{new InputTextFilter().filter});
        et_msa_title.setFilters(new InputFilter[]{new InputTextFilter().filter});
        et_msa_filename.setFilters(new InputFilter[]{new InputTextFilter().filter});

        et_msa_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_msa_title.getText().length() < 98)
                    et_msa_title.setBackgroundColor(new GraphicUtils().getColorByString(MSA_CLR));
                else
                    et_msa_title.setBackgroundColor(getColor(R.color.background_text_overhead));
            }
        });

        et_msa_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_msa_text.getText().length() < 3990)
                    et_msa_text.setBackgroundColor(new GraphicUtils().getColorByString(MSA_CLR));
                else
                    et_msa_text.setBackgroundColor(getColor(R.color.background_text_overhead));
            }
        });
    }


    public void display_send_list() {
        tv_send.setText(new MsaUtilsSQLite().message_get_send_list());
    }


    // подключение меню----------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_msa_edit, menu);
        mi_edit_picture_clear = menu.findItem(R.id.mi_edit_picture_clear);
        mi_edit_picture_crop = menu.findItem(R.id.mi_edit_picture_crop);
        mi_edit_picture_export = menu.findItem(R.id.mi_edit_picture_export);
        mi_edit_deletedraft = menu.findItem(R.id.mi_edit_deletedraft);
        mi_edit_picture_rotate_left = menu.findItem(R.id.mi_edit_picture_rotate_left);
        mi_edit_picture_rotate_right = menu.findItem(R.id.mi_edit_picture_rotate_right);
        mi_edit_picture_reduce = menu.findItem(R.id.mi_edit_picture_reduce);
        mi_edit_picture_import = menu.findItem(R.id.mi_edit_picture_import);
        mi_edit_picture_choice = menu.findItem(R.id.mi_edit_picture_choice);
        mi_edit_picture_takephoto = menu.findItem(R.id.mi_edit_picture_takephoto);
        mi_edit_attachment_clear = menu.findItem(R.id.mi_edit_attachment_clear);

        setVisibleInfo();
        return true;
    }


    OnClickListener onClickButtonListtiner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.ib_mesedit_fio:
                    Intent mesChoice = new Intent(MsaEditActivity.this, ChoiceFioActivity.class);
                    startActivity(mesChoice);
                    break;

                case R.id.tv_mesedit_send:
                    Intent mesChoice1 = new Intent(MsaEditActivity.this, ChoiceFioActivity.class);
                    startActivity(mesChoice1);
                    break;

                case R.id.ib_mesedit_lbl:
                    select_msa_lbl();
                    break;

                case R.id.ib_mesedit_clr:
                    select_msa_clr();
                    break;

                default:
                    break;
            }
        }
    };


    public Boolean vefify_Message(Boolean vfy_fio_list) {
        if (!vfy_fio_list) return true;
        else {
            if (tv_send.getText().toString().equals("")) {
                new InfoUtils().DisplayToastError(Appl.context.getString(R.string.action_devlist_isempty));
                return false;
            } else
                return true;
        }
    }


    // защита от закрытия по Back Обработка нажатия
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                smart_exit();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void deleteTempFile() {
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
            String imageFilename_crop = "proto_crop.jpg";
            File file = new File(Appl.TEMP_PATH, imageFilename_crop);
            if (file.exists()) {
                if (!file.delete()) {
                    new InfoUtils().DisplayToastError("Не удалось удалить файл " + file.getName());
                }
            }
        } catch (Exception ignored) {
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 75, bytes);//100
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void clear_attachment() {
        new AlertDialog.Builder(this, R.style.AlertDialogColorButton)
                .setTitle(getString(R.string.dlg_confirm_req))
                .setMessage(getString(R.string.s_message_clear_attachment_confirm))
                .setNegativeButton((getString(R.string.dlg_prg_no)), null)
                .setPositiveButton((getString(R.string.dlg_prg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                clearAttachment_from_SQLite();
                                setVisibleInfo();
                            }

                        }).create().show();
    }

    public void delete_current_message() {
        new AlertDialog.Builder(this, R.style.AlertDialogColorButton)
                .setTitle(getString(R.string.dlg_confirm_req))
                .setMessage(getString(R.string.s_message_delete_confirm_one))
                .setNegativeButton((getString(R.string.dlg_prg_no)), null)
                .setPositiveButton((getString(R.string.dlg_prg_yes)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                new MsaUtilsSQLite().message_delete_from_SQLite();
                                setResult(RESULT_OK);
                                finish();
                                new PrefUtils().animateFinish(MsaEditActivity.this);
                            }

                        }).create().show();
    }


    private void export_attacment() {
        if ((!MSA_FILETYPE.equals("")) & (MSA_IMAGESIZE > 0)) {
            String fn = new FileUtils().createValidFileName(et_msa_filename.getText().toString());
            MsaExportSQLite ex = new MsaExportSQLite();
            ex.sFileName = fn;
            ex.execute();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // скопировать этот черновик
            case R.id.mi_edit_copytoraft:
                message_save_to_SQLite_header(Appl.MSA_MODEVIEW);
                message_save_to_SQLite_image();
                Appl.MSA_ID = new MsaUtilsSQLite().copy_one_to_folder_SQLite(Appl.MSA_ID, Appl.MSA_MODEVIEW);
                load_msa_record_from_SQLite();
                return true;

            // копировать в избранное
            case R.id.mi_edit_copytofav:
                message_save_to_SQLite_header(Appl.MSA_MODEVIEW);
                message_save_to_SQLite_image();
                Appl.MSA_ID = new MsaUtilsSQLite().copy_one_to_folder_SQLite(Appl.MSA_ID, Appl.MSA_MODEVIEW);
                load_msa_record_from_SQLite();
                return true;

            // сохранить черновик
            case R.id.mi_edit_savedraft:
                //tiv_msa_edit.invalidate();
                if (vefify_Message(false)) {
                    message_save_to_SQLite_header(Appl.MSA_MODEVIEW);
                    message_save_to_SQLite_image();
                    load_msa_record_from_SQLite();
                }
                return true;

            // импорт  выбрать файл для вложения
            case R.id.mi_edit_picture_import:
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickIntent.setType("files/*");
                startActivityForResult(pickIntent, MSA_EDIT_PICK_ACTION);
                return true;

            // удалить черновик
            case R.id.mi_edit_deletedraft:
                delete_current_message();
                return true;

            // послать в исходящие = Послать сообщение
            case R.id.mi_edit_send:
                ssl_msa_edit.invalidate();
                if (vefify_Message(true)) {
                    message_save_to_SQLite_header(4);
                    message_save_to_SQLite_image();
                    load_msa_record_from_SQLite();
                    if (new PrefUtils().getUploadImmediately()) {
                        new SaveRecordListLoader().setSaveRecordList(Appl.MSA_ID);
                    }
                    setResult(RESULT_OK);
                    finish();
                    new PrefUtils().animateFinish(MsaEditActivity.this);
                }
                return true;

            // выбор изображения
            case R.id.mi_edit_picture_choice:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, MSA_EDIT_GALLERY_REQUEST);
                return true;

            // сделать фото
            case R.id.mi_edit_picture_takephoto:
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                deleteTempFile();
                outputFileUri_photo = Uri.fromFile(new File(Appl.TEMP_PATH, imageFilename_photo));
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri_photo);
                startActivityForResult(captureIntent, MSA_EDIT_CAMERA_CAPTURE);
                return true;

            // обрезать изображение
            case R.id.mi_edit_picture_crop:
                outputFileUri_photo = getImageUri(this, imageBig);
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(outputFileUri_photo, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                cropIntent.putExtra("output", outputFileUri_crop);
                cropIntent.putExtra("noFaceDetection", true);
                startActivityForResult(cropIntent, MSA_EDIT_PIC_CROP);
                return true;

            // экспортировать изображение из черновика
            case R.id.mi_edit_picture_export:
                export_attacment();
                return true;

            // очистить изображение
            case R.id.mi_edit_picture_clear:
                imageBig = (null);
                MSA_FILETYPE = "";
                MSA_FILENAME = "";
                MSA_IMAGESIZE = 0;
                imageBigModified = true;
                setVisibleInfo();
                return true;

            // повернуть влево
            case R.id.mi_edit_picture_rotate_left:
                rotateImage(-1);
                setVisibleInfo();
                return true;

            // повернуть вправо
            case R.id.mi_edit_picture_rotate_right:
                rotateImage(1);
                setVisibleInfo();
                return true;

            // уменьшить размер  изображения
            case R.id.mi_edit_picture_reduce:
                if (imageBig != null) {
                    if (imageBig.getByteCount() > 5000) imageinfo();
                }
                return true;

            // Стереть вложение
            case R.id.mi_edit_attachment_clear:
                clear_attachment();
                return true;

            // цвет заголовка изображения
            case R.id.mi_edit_set_clr:
                select_msa_clr();
                return true;

            // значок сообщения
            case R.id.mi_edit_set_lbl:
                select_msa_lbl();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void select_msa_clr() {
        Intent intentClr = new Intent(MsaEditActivity.this, ChoiceClrActivity.class);
        intentClr.putExtra("MSA_CLR", MSA_CLR);
        startActivity(intentClr);
    }


    protected void select_msa_lbl() {
        Intent intentLbl = new Intent(MsaEditActivity.this, ChoiceLblActivity.class);
        startActivity(intentLbl);
    }


    protected void smart_exit() {
        String MSA_TITLE = new StringUtils().strnormalize(et_msa_title.getText().toString());
        String MSA_TEXT = new StringUtils().strnormalize(et_msa_text.getText().toString());

        if ((!MSA_TITLE.equalsIgnoreCase(MSA_TITLE_FIRST))
                || (!MSA_TEXT.equalsIgnoreCase(MSA_TEXT_FIRST))
                || (!MSA_CLR.equalsIgnoreCase(MSA_CLR_FIRST))
                || (!MSA_LBL.equalsIgnoreCase(MSA_LBL_FIRST))
                || (imageBigModified)) {
            new AlertDialog.Builder(this, R.style.AlertDialogColorButton)
                    .setTitle(getString(R.string.dlg_confirm_req))
                    .setMessage(getString(R.string.dlg_date_changed))
                    .setNegativeButton((getString(R.string.dlg_prg_yes)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    if (vefify_Message(false)) {
                                        message_save_to_SQLite_header(Appl.MSA_MODEVIEW);
                                        message_save_to_SQLite_image();
                                        load_msa_record_from_SQLite();
                                        setResult(RESULT_OK);
                                        finish();
                                        new PrefUtils().animateFinish(MsaEditActivity.this);
                                    }
                                }
                            })
                    .setPositiveButton((getString(R.string.dlg_prg_no)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    setResult(RESULT_OK);
                                    finish();
                                    new PrefUtils().animateFinish(MsaEditActivity.this);
                                }
                            })
                    .create()
                    .show();
        } else {
            setResult(RESULT_OK);
            finish();
            new PrefUtils().animateFinish(MsaEditActivity.this);
        }
    }

    /*  mode=-1 влево, против часовой стрелки. mode=1 вправо, по часовой стрелке 	 */
    protected void rotateImage(int mode) {
        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        if (mode == -1) matrix.postRotate(-90);
        if (mode == 1) matrix.postRotate(90);
        imageBig = Bitmap.createBitmap(imageBig, 0, 0, imageBig.getWidth(), imageBig.getHeight(), matrix, true);
        imageBigModified = true;
    }

    public void createImageFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = new FileOutputStream(new File(Appl.TEMP_PATH, "ImageToReduce.jpg"));
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void imageinfo() {
        createImageFromBitmap(imageBig);

        Intent reduceintent = new Intent(MsaEditActivity.this, MsaReduceImageActivity.class);
        startActivity(reduceintent);
    }


    public Bitmap getThumbnail(Uri uri, int maxSize) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null)
            input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        if (maxSize > 0) {

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
            double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0;
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);

            /*   int originalSize = (onlyBoundsOptions.outHeight*onlyBoundsOptions.outWidth);
            double a =  maxSize*maxSize;
            double b = originalSize;
            double ratio = b/a;
           if (ratio<1.0)
                ratio=1.0;
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);} */
        }

        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        if (input != null)
            input.close();
        return bitmap;
    }

    private int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case MSA_EDIT_GALLERY_REQUEST:
                    try {
                        final Uri imageUri = returnedIntent.getData();
                        imageBig = getThumbnail(imageUri, MAX_IMAGE_SIZE);
                        imageBigModified = true;
                        MSA_FILENAME = "";
                        MSA_FILETYPE = "jpg";
                        setVisibleInfo();
                    } catch (Exception e) {
                        new InfoUtils().DisplayToastError(Appl.context.getString(R.string.s_no_request_image_from_gallery));
                        break;
                    }
                    break;

                case MSA_EDIT_CAMERA_CAPTURE:
                    try {
                        imageBig = getThumbnail(outputFileUri_photo, MAX_IMAGE_SIZE);
                        imageBigModified = true;
                        MSA_FILENAME = "";
                        MSA_FILETYPE = "jpg";
                        setVisibleInfo();
                    } catch (Exception e) {
                        new InfoUtils().DisplayToastError(e.toString());
                        break;
                    }
                    break;

                case MSA_EDIT_PIC_CROP:
                    try {
                        outputFileUri_crop = returnedIntent.getData(); // ???????????????
                        imageBig = (Media.getBitmap(getContentResolver(), outputFileUri_crop));
                    } catch (Exception e) {
                        new InfoUtils().DisplayToastError(Appl.context.getString(R.string.s_no_prepate_after_crop));
                        break;
                    }
                    imageBigModified = true;
                    setVisibleInfo();
                    break;

                case MSA_EDIT_PICK_ACTION:
                    if (returnedIntent.getData() != null) {
                        String fn = returnedIntent.getData().getLastPathSegment();
                        if (new FileUtils().verifyMaxFileSize(returnedIntent.getData().getPath(), true)) {
                            int i = fn.lastIndexOf(".");
                            if (i > 0) {
                                message_save_to_SQLite_header(0);
                                fn = fn.substring(i + 1, fn.length());
                                MsaImportSQLite ld = new MsaImportSQLite();
                                ld.MSA_FILETYPE = fn;
                                ld.MSA_FULLFILENAME = returnedIntent.getData().getSchemeSpecificPart();
                                ld.MSA_FILENAME = returnedIntent.getData().getLastPathSegment();
                                ld.execute();
                            }
                        }
                    }
                    break;

            }
        }
    }


    public void setVisibleInfo() {
        if (imageBig == null) {
            ssl_msa_edit.setEnabled(false);
            if (MSA_FILETYPE.equals("")) {
                ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_empty));
            } else {
                ssl_msa_edit.setEnabled(MSA_FILETYPE.equalsIgnoreCase("jpg"));
                if (MSA_IMAGESIZE > 0) {
                    ssl_msa_edit.setEnabled(true); //////////////
                    ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_other));
                    if (MSA_FILETYPE.equalsIgnoreCase("doc"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_doc));
                    if (MSA_FILETYPE.equalsIgnoreCase("docx"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_doc));
                    if (MSA_FILETYPE.equalsIgnoreCase("pdf"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_pdf));
                    if (MSA_FILETYPE.equalsIgnoreCase("zip"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_zip));
                    if (MSA_FILETYPE.equalsIgnoreCase("xls"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_xls));
                    if (MSA_FILETYPE.equalsIgnoreCase("xlsx"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_xls));
                    if (MSA_FILETYPE.equalsIgnoreCase("mp4"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_video));
                    if (MSA_FILETYPE.equalsIgnoreCase("mp3"))
                        ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_filetype_audio));

                } else {
                    ssl_msa_edit.setImage(ImageSource.resource(R.mipmap.ic_import));
                    setLayoutImageHeight();
                }
            }
        } else {
            ssl_msa_edit.setEnabled(true);
            setLayoutImageHeight();

            new Thread(new Runnable() {
                public void run() {
                    ssl_msa_edit.post(new Runnable() {
                        public void run() {
                            ssl_msa_edit.setImage(ImageSource.bitmap(imageBig));
                        }
                    });
                }
            }).start();
        }

        if (imageBig != null) {
            MSA_IMAGESIZE = imageBig.getByteCount();
            tv_msa_imagesize.setText(new StringUtils().convertToKbStr(MSA_IMAGESIZE));
        } else {
            if (MSA_IMAGESIZE == 0) tv_msa_imagesize.setText("");
            else tv_msa_imagesize.setText(new StringUtils().convertToKbStr(MSA_IMAGESIZE));
        }

        if (mi_edit_picture_clear != null) {
            mi_edit_picture_clear.setVisible(imageBig != null);
            mi_edit_picture_crop.setVisible(imageBig != null);
            mi_edit_picture_export.setVisible((MSA_IMAGESIZE > 0) & (!MSA_FILETYPE.equals("")));
            mi_edit_picture_rotate_left.setVisible(imageBig != null);
            mi_edit_picture_rotate_right.setVisible(imageBig != null);
            mi_edit_picture_reduce.setVisible(imageBig != null);
            mi_edit_picture_import.setVisible(imageBig == null);
            mi_edit_picture_takephoto.setVisible((MSA_FILETYPE.equals("") | (MSA_FILETYPE.equalsIgnoreCase("jpg"))));
            mi_edit_picture_choice.setVisible((MSA_FILETYPE.equals("") | (MSA_FILETYPE.equalsIgnoreCase("jpg"))));
        }

        et_msa_filename.setText(MSA_FILENAME);
        tv_msa_filetype.setText(MSA_FILETYPE);
        ly_edit.setBackgroundColor(new GraphicUtils().getColorByString(MSA_CLR));

        ib_mesedit_lbl.setImageResource(new GraphicUtils().getResIDbyName(MSA_LBL));
    }

    public void setLayoutImageHeight() {
        LinearLayout.LayoutParams ly_param = (LinearLayout.LayoutParams) ly_ssl.getLayoutParams();
        if (!(imageBig == null)) {
            int pic_height = imageBig.getHeight();
            int new_height = ly_ssl.getWidth();
            if (pic_height < new_height)
                new_height = pic_height;
            ly_param.height = new_height;
        } else {
            ly_param.height = 10;
        }
        ly_ssl.setLayoutParams(ly_param);
    }


    public DisplayMetrics getMyDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }


    public void clearAttachment_from_SQLite() {
        MSA_FILETYPE = "";
        MSA_FILENAME = "";
        MSA_IMAGESIZE = 0;
        String sSQL = "update MSA set MSA_IMAGE=NULL,MSA_FILENAME=NULL,MSA_FILETYPE=NULL where MSA_ID='" + Appl.MSA_ID + "';";
        Appl.getDatabase().execSQL(sSQL);
    }


    public void message_save_to_SQLite_header(int msa_state_to_save) {
        String MSA_TITLE = new StringUtils().strnormalize(et_msa_title.getText().toString());
        if (MSA_TITLE.equals("")) {
            MSA_TITLE = new StringUtils().getStringCurrentDateTime();
            et_msa_title.setText(MSA_TITLE);
        }
        MSA_FILENAME = new StringUtils().strnormalize(et_msa_filename.getText().toString());
        MSA_FILETYPE = new StringUtils().strnormalize(tv_msa_filetype.getText().toString());
        if ((!MSA_FILETYPE.equals("")) & (MSA_FILENAME.equals(""))) {
            MSA_FILENAME = MSA_TITLE;
            et_msa_filename.setText(MSA_FILENAME);
        }

        String MSA_TEXT = new StringUtils().strnormalize(et_msa_text.getText().toString());
        String sSQL = " update MSA set "
                + " MSA_DATE=datetime ('now', 'localtime'),"
                + " MSA_STATE = " + msa_state_to_save + ","
                + " MSA_CLR = '" + MSA_CLR + "',"
                + " MSA_LBL = '" + MSA_LBL + "',"
                + " MSA_TITLE = '" + new StringUtils().truncateString(MSA_TITLE, 99) + "',"
                + " MSA_FILENAME = '" + new StringUtils().truncateString(MSA_FILENAME, 100) + "', "
                + " MSA_TEXT = '" + new StringUtils().truncateString(MSA_TEXT, 3999) + "', "
                + " MSA_FILETYPE = '" + new StringUtils().strnormalize(MSA_FILETYPE) + "' "
                + " where MSA_ID='" + Appl.MSA_ID + "'; ";
        Appl.getDatabase().execSQL(sSQL);
    }


    public void message_save_to_SQLite_image() {
        if ((MSA_FILETYPE.equalsIgnoreCase("jpg")) & (imageBigModified)) {
            if (imageBig != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBig.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                byte[] imageInByte = stream.toByteArray();
                ContentValues editMessage = new ContentValues();
                editMessage.put("MSA_IMAGE", imageInByte);
                Appl.getDatabase().update("MSA", editMessage,
                        "MSA_ID='" + Appl.MSA_ID + "'", null);
            } else {
                String sSQL = " update MSA set MSA_IMAGE=NULL where MSA_ID='" + Appl.MSA_ID + "'; ";
                Appl.getDatabase().execSQL(sSQL);
            }
        }
    }


    public void load_msa_record_from_SQLite() {
        String sSQL =
                "select A._id as _id, MSA_ID,MSA_CLR,MSA_LBL,MSA_TITLE,"
                        + "MSA_TEXT,A.FIO_ID,MSA_FILETYPE,MSA_FILENAME,FIO_NAME,MSA_DATE,"
                        + "case when length(MSA_IMAGE) is NULL then 0 else length(MSA_IMAGE) end as MSA_IMAGESIZE_MSSQL "
                        + "from MSA A "
                        + "left join FIO F on A.FIO_ID=F.FIO_ID "
                        + "where MSA_ID='" + Appl.MSA_ID + "'";
        Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            MSA_TITLE_FIRST = new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_TITLE")));
            MSA_TEXT_FIRST = new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_TEXT")));
            MSA_FILENAME = new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_FILENAME")));
            MSA_FILETYPE = new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_FILETYPE")));
            MSA_CLR = new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_CLR")));
            MSA_LBL = new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_LBL")));
            MSA_CLR_FIRST = MSA_CLR;
            MSA_LBL_FIRST = MSA_LBL;
            MSA_IMAGESIZE = c.getInt(c.getColumnIndex("MSA_IMAGESIZE_MSSQL"));
            imageBigModified = false;
            et_msa_title.setText(MSA_TITLE_FIRST);
            et_msa_text.setText(MSA_TEXT_FIRST);
            et_msa_filename.setText(MSA_FILENAME);
            tv_msa_filetype.setText(MSA_FILETYPE);
            tv_msa_fio.setText(new StringUtils().strnormalize(c.getString(c.getColumnIndex("FIO_NAME"))));
            tv_msa_datetime.setText(new StringUtils().strnormalize(c.getString(c.getColumnIndex("MSA_DATE"))));
            c.close();

            if (MSA_FILETYPE.equalsIgnoreCase("jpg") & (MSA_IMAGESIZE > 0)) {
                byte[] resall = new MsaUtilsSQLite().getByteArrayFromMsaImaget(Appl.MSA_ID);
                if (resall != null)
                    imageBig = BitmapFactory.decodeByteArray(resall, 0, resall.length);
            }
        } else {
            tv_msa_fio.setText(Appl.FIO_NAME);
            c.close();
        }
        setVisibleInfo();
    }

    public void reduceImage(int w2, int h2) {
        Bitmap myimageBig = imageBig;
        imageBig = Bitmap.createScaledBitmap(myimageBig, w2, h2, false);
        setVisibleInfo();
        imageBigModified = true;
    }

    public void afterImportFile(String filename, String filetype, int bytesread) {
        MSA_FILETYPE = filetype;
        MSA_FILENAME = filename;
        MSA_IMAGESIZE = bytesread;
        et_msa_filename.setText(MSA_FILENAME);
        tv_msa_filetype.setText(MSA_FILETYPE);
        message_save_to_SQLite_header(0);
        setVisibleInfo();
    }

    public void setLbl(String sn) {
        MSA_LBL = sn;
        ib_mesedit_lbl.setImageDrawable(new GraphicUtils().getLabelDrawableByNomer(sn, 1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
        new NotificationUtils().cancelNotification(0);
    }

    @Subscribe
    public void getMessage(Events.EventsMessage ev) {
        if (ev.getMes_code().equals(Events.EB_MSAEDIT_SET_BACKGROUND_CLR)) {
            MSA_CLR = ev.getMes_str();
            ly_edit.setBackgroundColor(new GraphicUtils().getColorByString(MSA_CLR));
        }

        if (ev.getMes_code().equals(Events.EB_MSAEDIT_SET_SEND_LIST)) {
            display_send_list();
        }

        if (ev.getMes_code().equals(Events.EB_MSAEDIT_REDUCE_IMAGE)) {
            reduceImage(ev.getMes_x(), ev.getMes_y());
        }

        if (ev.getMes_code().equals(Events.EB_MSAEDIT_INPORT_FILE)) {
            afterImportFile(ev.getMes_s1(), ev.getMes_s2(), ev.getMes_int());
        }

        if (ev.getMes_code().equals(Events.EB_MSAEDIT_SET_LBL)) {
            setLbl(ev.getMes_str());
        }


    }


}
