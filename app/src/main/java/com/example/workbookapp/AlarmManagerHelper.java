package com.example.workbookapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerHelper {
    public static final int ALARM_REQUEST_CODE = 1234;

    public static void setCardExpirationCheckAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CardExpirationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // Đặt cờ FLAG_IMMUTABLE

        // Thời gian bắt đầu kiểm tra
        long startTime = System.currentTimeMillis();

        // Thời gian kiểm tra lại sau mỗi 1 phút
        long interval = 1 * 60 * 1000;

        // Cài đặt báo thức cho việc kiểm tra hết hạn
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, pendingIntent);
    }

    public static void cancelCardExpirationCheckAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CardExpirationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // Đặt cờ FLAG_IMMUTABLE

        // Hủy báo thức kiểm tra hết hạn
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
