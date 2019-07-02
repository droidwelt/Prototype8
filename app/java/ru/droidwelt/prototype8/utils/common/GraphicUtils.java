package ru.droidwelt.prototype8.utils.common;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.droidwelt.prototype8.R;

public class GraphicUtils {




    public  Bitmap getImageBig(Uri uri, int maxSize) {
        try {
            return getImageBigBase(uri, maxSize);
        } catch (IOException e) {
            return null;
        }
    }

    public  Bitmap getImageBig(String fn, int maxSize) {
        try {
            return getImageBigBase(Uri.fromFile(new File(fn)), maxSize);
        } catch (IOException e) {
            return null;
        }
    }

    private  Bitmap getImageBigBase(Uri uri, int maxSize) throws IOException {
        InputStream input = Appl.getContext().getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null)
            input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        if (!(maxSize == 0)) {
            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
            double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0;
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        }

        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = Appl.getContext().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        if (input != null)
            input.close();
        return bitmap;
    }

    private  int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

















    public byte[] concatArray(byte[] a, byte[] b) {
    if (a == null) return b;
    if (b == null) return a;
    byte[] r = new byte[a.length + b.length];
    System.arraycopy(a, 0, r, 0, a.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
}


    public  String bytesToHex(byte[] data) {
        if (data==null)        {return null;}
        int len = data.length;
        StringBuilder str = new StringBuilder();
        for (byte aData : data) {
            if ((aData & 0xFF) < 16)
                str.append("0").append(Integer.toHexString(aData & 0xFF));
            else
                str.append(Integer.toHexString(aData & 0xFF));
        }
        return str.toString();
    }

    public  byte[] getByteArrayfromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public  Bitmap getBitmapfromByteArray(byte[] bitmap) {
        return BitmapFactory.decodeByteArray(bitmap , 0, bitmap.length);
    }

    public   Bitmap convertBase64StringToBitmap(String source) {
        byte[] rawBitmap = Base64.decode(source.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
    }
public  int getMyBarColor() {
    Drawable d = new ColorDrawable(Color.BLACK);
    if (Appl.MSA_MODEVIEW == 0) return Appl.BarColor_0;
    if (Appl.MSA_MODEVIEW == 2) return Appl.BarColor_2;
    if (Appl.MSA_MODEVIEW == 22) return Appl.BarColor_22;
    if (Appl.MSA_MODEVIEW == 3) return Appl.BarColor_3;
    if (Appl.MSA_MODEVIEW == 4) return Appl.BarColor_4;
    if (Appl.MSA_MODEVIEW == 10) return Appl.BarColor_10;
    return Color.BLACK;
}

    public  void byteArrayToFile(byte[] resall, String fn) {
        if (resall != null) {
            new FileUtils().deleteTempFile(fn);
            File f = new File(fn);

            try {
                if (!f.createNewFile())
                    return;
            } catch (IOException e) {
                e.printStackTrace();
                new InfoUtils().DisplayToastError(e.getMessage());
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                new InfoUtils().DisplayToastError(e.getMessage());
            }
            try {
                if (fos != null) {
                    fos.write(resall);
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                new InfoUtils().DisplayToastError(e.getMessage());
            }
        }
    }


    public  int getResIDbyName(String sx) {
        // mode = 0  показ нормальный, 1 - для фильтра или выбора
        String sn = new StringUtils().strnormalize(sx);
        String sres = "q_00";
        if (sn.equals("0")) sres = "q_00";
        if (sn.equals("1")) sres = "q_01";
        if (sn.equals("2")) sres = "q_02";
        if (sn.equals("3")) sres = "q_03";
        if (sn.equals("4")) sres = "q_04";
        if (sn.equals("5")) sres = "q_05";
        if (sn.equals("6")) sres = "q_06";
        if (sn.equals("7")) sres = "q_07";
        if (sn.equals("8")) sres = "q_08";
        if (sn.equals("9")) sres = "q_09";
        if (sn.equals("10")) sres = "q_10";
        if (sn.equals("11")) sres = "q_11";
        if (sn.equals("12")) sres = "q_12";
        if (sn.equals("13")) sres = "q_13";
        if (sn.equals("14")) sres = "q_14";
        if (sn.equals("15")) sres = "q_15";
        if (sn.equals("16")) sres = "q_16";
        if (sn.equals("17")) sres = "q_17";
        if (sn.equals("18")) sres = "q_18";
        if (sn.equals("19")) sres = "q_19";
        if (sn.equals("20")) sres = "q_20";
        if (sn.equals("21")) sres = "q_21";
        if (sn.equals("22")) sres = "q_22";
        if (sn.equals("23")) sres = "q_23";
        if ((sres.equals("q_00"))) sres = "q_filter";
        return Appl.getContext().getResources().getIdentifier(sres, "mipmap", Appl.getContext().getPackageName());
    }



    public  int getColorByString(String sClr) {
        if (sClr == null) return Color.parseColor("#FFFFFF"); // White
        else {
            if (sClr.equalsIgnoreCase("0")) return Color.parseColor("#EEEEEE"); // White
            if (sClr.equalsIgnoreCase("1")) return Color.parseColor("#ECEFF1"); // Blue Grey
            if (sClr.equalsIgnoreCase("2")) return Color.parseColor("#FBE9E7"); // Deep Orange
            if (sClr.equalsIgnoreCase("3")) return Color.parseColor("#FFF8E1"); // Amber
            if (sClr.equalsIgnoreCase("4")) return Color.parseColor("#F9FBE7"); // Lime
            if (sClr.equalsIgnoreCase("5")) return Color.parseColor("#F1F8E9"); // Light Green
            if (sClr.equalsIgnoreCase("6")) return Color.parseColor("#E8F5E9"); // Green
            if (sClr.equalsIgnoreCase("7")) return Color.parseColor("#E0F2F1"); // Teal
            if (sClr.equalsIgnoreCase("8")) return Color.parseColor("#E0F7FA"); // Cyan
            if (sClr.equalsIgnoreCase("9")) return Color.parseColor("#E1F5FE"); // Light Blue
            if (sClr.equalsIgnoreCase("A")) return Color.parseColor("#E8EAF6"); // Indigo
            if (sClr.equalsIgnoreCase("B")) return Color.parseColor("#E3F2FD"); // Blue
            if (sClr.equalsIgnoreCase("C")) return Color.parseColor("#EDE7F6"); // Deep Purple
            if (sClr.equalsIgnoreCase("D")) return Color.parseColor("#F3E5F5"); // Purple
            if (sClr.equalsIgnoreCase("E")) return Color.parseColor("#FCE4EC"); // Pink
            if (sClr.equalsIgnoreCase("F")) return Color.parseColor("#FFEBEE"); // Red
            return Color.parseColor("#FFFFFF"); // White
        }
    }


    @SuppressLint("NewApi")
    public  Drawable getLabelDrawableByNomer(String sx, int mode) {
        // mode = 0  показ нормальный, 1 - для фильтра или выбора
        String sn = new StringUtils().strnormalize(sx);
        if ((mode == 1) & (sn.equals(""))) return Appl.getContext().getDrawable(R.mipmap.q_filter);
        if ((mode == 1) & (sn.equals("0"))) return Appl.getContext().getDrawable(R.mipmap.q_filter);
        if (sn.equalsIgnoreCase("")) return Appl.getContext().getDrawable(R.mipmap.q_00);
        if (sn.equalsIgnoreCase("0")) return Appl.getContext().getDrawable(R.mipmap.q_00);
        if (sn.equalsIgnoreCase("1")) return Appl.getContext().getDrawable(R.mipmap.q_01);
        if (sn.equalsIgnoreCase("2")) return Appl.getContext().getDrawable(R.mipmap.q_02);
        if (sn.equalsIgnoreCase("3")) return Appl.getContext().getDrawable(R.mipmap.q_03);
        if (sn.equalsIgnoreCase("4")) return Appl.getContext().getDrawable(R.mipmap.q_04);
        if (sn.equalsIgnoreCase("5")) return Appl.getContext().getDrawable(R.mipmap.q_05);
        if (sn.equalsIgnoreCase("6")) return Appl.getContext().getDrawable(R.mipmap.q_06);
        if (sn.equalsIgnoreCase("7")) return Appl.getContext().getDrawable(R.mipmap.q_07);
        if (sn.equalsIgnoreCase("8")) return Appl.getContext().getDrawable(R.mipmap.q_08);
        if (sn.equalsIgnoreCase("9")) return Appl.getContext().getDrawable(R.mipmap.q_09);
        if (sn.equalsIgnoreCase("10")) return Appl.getContext().getDrawable(R.mipmap.q_10);
        if (sn.equalsIgnoreCase("11")) return Appl.getContext().getDrawable(R.mipmap.q_11);
        if (sn.equalsIgnoreCase("12")) return Appl.getContext().getDrawable(R.mipmap.q_12);
        if (sn.equalsIgnoreCase("13")) return Appl.getContext().getDrawable(R.mipmap.q_13);
        if (sn.equalsIgnoreCase("14")) return Appl.getContext().getDrawable(R.mipmap.q_14);
        if (sn.equalsIgnoreCase("15")) return Appl.getContext().getDrawable(R.mipmap.q_15);
        if (sn.equalsIgnoreCase("16")) return Appl.getContext().getDrawable(R.mipmap.q_16);
        if (sn.equalsIgnoreCase("17")) return Appl.getContext().getDrawable(R.mipmap.q_17);
        if (sn.equalsIgnoreCase("18")) return Appl.getContext().getDrawable(R.mipmap.q_18);
        if (sn.equalsIgnoreCase("19")) return Appl.getContext().getDrawable(R.mipmap.q_19);
        if (sn.equalsIgnoreCase("20")) return Appl.getContext().getDrawable(R.mipmap.q_20);
        if (sn.equalsIgnoreCase("21")) return Appl.getContext().getDrawable(R.mipmap.q_21);
        if (sn.equalsIgnoreCase("22")) return Appl.getContext().getDrawable(R.mipmap.q_22);
        if (sn.equalsIgnoreCase("23")) return Appl.getContext().getDrawable(R.mipmap.q_23);
        return Appl.getContext().getDrawable(R.mipmap.q_00);
    }





}

