package com.task.user.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kotlin.jvm.JvmStatic;

public class PrettyDateView extends AppCompatTextView {

    public PrettyDateView(Context context) {
        super(context);
    }

    public PrettyDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrettyDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @BindingAdapter({"bind:date", "bind:format"})
    @JvmStatic
    public static void setFormattedDate(AppCompatTextView textView, String date, String format) {
        try {
            textView.setText(parseDateTimeFromFormat(Long.parseLong(date), format));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setDateFormat(String date, String format) {
        setFormattedDate(this, date, format);
    }

    private static String parseDateTimeFromFormat(long millis, String format) {
        // INIT
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar c = Calendar.getInstance(Locale.getDefault());

        // SET TIME
        c.setTimeInMillis(millis);

        // SET CALENDAR TO SAVE TIME ZONE IN DATE FORMAT
        dateFormat.setCalendar(c);

        // SET
        return dateFormat.format(c.getTime());
    }

}
