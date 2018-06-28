package co.gbyte.weightlog.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {

    public static String getDateString(Context context, String format, Date time) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(time)
                   + DateFormat.getMediumDateFormat(context).format(time);
    }

    public static String getShortDateString(Context context, Date time) {
        return DateFormat.getDateFormat(context).format(time);
    }

    public static String getTimeString(Context context, Date time) {
        return DateFormat.getTimeFormat(context).format(time);
    }
}
