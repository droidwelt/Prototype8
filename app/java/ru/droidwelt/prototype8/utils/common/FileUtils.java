package ru.droidwelt.prototype8.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileUtils {

    public  void createTempPath() {
        File PATH_TO = new File(Appl.APP_PATH); // куда
        if (!(PATH_TO.isDirectory() && PATH_TO.canExecute() && PATH_TO.canRead() && PATH_TO.canWrite())) {
            if (!PATH_TO.mkdir()) {
                new InfoUtils().DisplayToastError("Не удалось создать папку "+PATH_TO.getPath());
            }
        }

        PATH_TO = new File(Appl.TEMP_PATH); // куда
        if (!(PATH_TO.isDirectory() && PATH_TO.canExecute() && PATH_TO.canRead() && PATH_TO.canWrite())) {
            if (!PATH_TO.mkdir()) {
                new InfoUtils().DisplayToastError("Не удалось создать папку "+PATH_TO.getPath());
            }
        }

        PATH_TO = new File(Appl.DB_PATH); // куда
        if (!(PATH_TO.isDirectory() && PATH_TO.canExecute() && PATH_TO.canRead() && PATH_TO.canWrite())) {
            if (!PATH_TO.mkdir()) {
                new InfoUtils().DisplayToastError("Не удалось создать папку "+PATH_TO.getPath());
            }
        }
    }


    public  String getApkPath() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        } else {
            return Environment.getExternalStorageDirectory().toString() + "/Download/";
        }
    }


    public  boolean verifyMaxFileSize(String fn, boolean displayToast) {
        int maxFileSize = new PrefUtils().getDownload_maxsize();
        if (maxFileSize > 0) {
            File file = new File(fn);
            if (file.exists()) {
                if ((int) file.length() < maxFileSize) return true;
                else {
                    if (displayToast)
                        new InfoUtils().DisplayToastError("Размер вложения не может быть более " + maxFileSize + " Мб");
                    return false;
                }
            } else return false;
        } else return true;
    }


    public String createValidFileName(String s) {
        s = generValidFileName(new StringUtils().strnormalize(s));
        if (s.equals("")) s = new StringUtils().getRandomString(12);
        return s;
    }


    @SuppressLint("DefaultLocale")
    public String generValidFileName(String s_in) {
        if (s_in.isEmpty()) return "";
        StringBuilder s_out = new StringBuilder();
        try {
            String expression = "[.~+=?0123456789QWERTYUIOPASDFGHJKLZXCVBNM_-ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁ]";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            int l = s_in.length();
            for (int i = 0; i < l; i = i + 1) {
                String s = s_in.substring(i, i + 1) /*.toUpperCase()*/;
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) s_out.append(s);
                else s_out.append("_");
            }
        } catch (Exception e) {
            return "";
        }
        return s_out.toString();
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public void startIntentFromFile(String sFileName) {
        String mime = getMimeType(sFileName);
        String url = "file://" + Environment.getExternalStorageDirectory().toString() + "/Download/" + sFileName;
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/Download/", sFileName);
        Intent intent = new FileUtils().getDefaultViewIntent(Uri.fromFile(f));
        Uri data = Uri.parse(url);
        if (intent != null) {
            intent.setDataAndType(data, mime);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Appl.context.startActivity(intent);
        }
    }


    // http://stackoverflow.com/questions/6621789/how-to-open-an-excel-file-in-android
    public Intent getDefaultViewIntent(Uri uri) {
        PackageManager pm = Appl.context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String name = (new File(uri.getPath())).getName();
        intent.setDataAndType(uri, getMimeType(name));
        final List<ResolveInfo> lri = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (lri.size() > 0)
            return intent;
        return null;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteTempFile(String fn) {
        try {
            File file = new File(fn);
            if (file.exists())
                file.delete();
        } catch (Exception e) {
            //e.printStackTrace();
            new InfoUtils().DisplayToastError(e.getMessage());
        }
    }



    	/*
	  Extension MIME Type
.doc      application/msword
.dot      application/msword

.docx     application/vnd.openxmlformats-officedocument.wordprocessingml.document
.dotx     application/vnd.openxmlformats-officedocument.wordprocessingml.template
.docm     application/vnd.ms-word.document.macroEnabled.12
.dotm     application/vnd.ms-word.template.macroEnabled.12

.xls      application/vnd.ms-excel
.xlt      application/vnd.ms-excel
.xla      application/vnd.ms-excel

.xlsx     application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
.xltx     application/vnd.openxmlformats-officedocument.spreadsheetml.template
.xlsm     application/vnd.ms-excel.sheet.macroEnabled.12
.xltm     application/vnd.ms-excel.template.macroEnabled.12
.xlam     application/vnd.ms-excel.addin.macroEnabled.12
.xlsb     application/vnd.ms-excel.sheet.binary.macroEnabled.12

.ppt      application/vnd.ms-powerpoint
.pot      application/vnd.ms-powerpoint
.pps      application/vnd.ms-powerpoint
.ppa      application/vnd.ms-powerpoint

.pptx     application/vnd.openxmlformats-officedocument.presentationml.presentation
.potx     application/vnd.openxmlformats-officedocument.presentationml.template
.ppsx     application/vnd.openxmlformats-officedocument.presentationml.slideshow
.ppam     application/vnd.ms-powerpoint.addin.macroEnabled.12
.pptm     application/vnd.ms-powerpoint.presentation.macroEnabled.12
.potm     application/vnd.ms-powerpoint.template.macroEnabled.12
.ppsm     application/vnd.ms-powerpoint.slideshow.macroEnabled.12

.mdb      application/vnd.ms-access

	 */

    //http://stackoverflow.com/questions/4212861/what-is-a-correct-mime-type-for-docx-pptx-etc
    @SuppressLint("DefaultLocale")
    public static String getMimeType(String url) {
        String type;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        //	Log.i("getMimeType","extension="+extension);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
            if (type == null) {
                if (url.toLowerCase().endsWith("mp4")) {
                    type = "video/*";
                }
                if (url.toLowerCase().endsWith("mpg")) {
                    type = "video/*";
                }
                if (url.toLowerCase().endsWith("xls")) {
                    type = "application/vnd.ms-excel";
                }
                if (url.toLowerCase().endsWith("xlsx")) {
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                }
                if (url.toLowerCase().endsWith("doc")) {
                    type = "application/msword";
                }
                if (url.toLowerCase().endsWith("docx")) {
                    type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                }
            }
            return type;
        } else return "*/*";
    }


}
