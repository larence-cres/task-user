package com.task.user.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static String parseDateTimeFromFormat(long millis, String format) {
        // INIT
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());

        // SET TIME
        c.setTimeInMillis(millis);
        c.setTimeZone(TimeZone.getTimeZone("GMT"));

        // SET CALENDAR TO SAVE TIME ZONE IN DATE FORMAT
        dateFormat.setCalendar(c);

        // RETURN
        return dateFormat.format(c.getTime());
    }

}
