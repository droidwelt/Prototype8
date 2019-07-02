package ru.droidwelt.prototype8.utils.common;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class StringUtils {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public  String truncateString(String s, int len) {
        if (s.isEmpty()) {
            return "";
        } else {
            int slen = s.length();
            if (slen > len) {
                return s.substring(0, len - 1);
            } else {
                return s;
            }
        }
    }

    public  String strnormalize(String s) {
        if (s == null) return "";
        else if (s.isEmpty()) return "";
        else return s;
    }

    public  String convertToKbStr(int i) {
        String s = "";
        if (i <= 9999) s = i + " б";
        if (i > 9999) s = "" + i / 1024 + " кб";
        if (i > 9999999) s = "" + i / 1024 / 1024 + " Мб";
        return s;
    }

    public  float stringToFloat(String str) {
        Float f = (float) 0.0;
        assert str != null;
        if (!str.trim().equals("")) try {
            f = Float.valueOf(str);
        } catch (NumberFormatException ignored) {
        }
        return f;
    }

    @SuppressLint("SimpleDateFormat")
    public String getStringCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.mmm");
        return sdf.format(calendar.getTime());
    }


    public  String generate_GUID() {
        return UUID.randomUUID().toString();
    }


    public  String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
