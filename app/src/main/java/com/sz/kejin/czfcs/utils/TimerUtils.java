package com.sz.kejin.czfcs.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class TimerUtils {

    //时间选择器
    public static void showTime(final Activity activity, final TextView tvDdsj) {
        long lonTime = System.currentTimeMillis();
        Date date = new Date(lonTime);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        DatePickerDialog pickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                tvDdsj.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

//        pickerDialog.getDatePicker().setMinDate(lonTime);
        pickerDialog.show();
    }
    //时间选择器
    public static void showTimes(final Activity activity, final TextView tvDdsj) {
        long lonTime = System.currentTimeMillis();
        Date date = new Date(lonTime);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(date);
        tvDdsj.setText(format1);
    }
}
