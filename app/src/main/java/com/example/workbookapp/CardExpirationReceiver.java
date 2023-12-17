package com.example.workbookapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class CardExpirationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth", context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId", 0);
        int day = 24 * 60 * 60 * 1000; // 1 day
        int timeAlmost = 1 * day;
        String title = "";
        String message = "";

        ArrayList<Card> cardAlmostExpires = dbHelper.getCardAlmostExpiredByUserId(userId, timeAlmost);
        for (Card card: cardAlmostExpires){
            Table table = dbHelper.getTableById(card.getTableId());
            if(table != null){
                title = "Thông báo thẻ gần hết hạn";
                message = "Thẻ '" + card.getName() + "' trong bảng '" + table.getName() + "' của bạn còn " + timeAlmost / day +  " ngày nữa là hết hạn";
                NotificationHelper.showNotification(context, title, message);
                dbHelper.addNotify(title, message, userId);
            }
        }

        boolean result;
        ArrayList<Card> cardExpires = dbHelper.getCardExpiredByUserId(userId);
        for (Card card: cardExpires){
            result = dbHelper.expiredCard(card.getId());
            if(result){
                Table table = dbHelper.getTableById(card.getTableId());
                if(table != null){
                    title = "Thông báo thẻ hết hạn";
                    message = "Thẻ '" + card.getName() + "' trong bảng '" + table.getName() + "' của bạn đã hết hạn";
                    NotificationHelper.showNotification(context, title, message);
                    dbHelper.addNotify(title, message, userId);
                }
            }
        }

        int notifyCount = dbHelper.notifyCount(userId);
        if(notifyCount == 0){
            AHBottomNavigationHolder.getInstance().getBnvNavigate().setNotification("", 2);
        }else {
            AHBottomNavigationHolder.getInstance().getBnvNavigate().setNotification(String.valueOf(notifyCount), 2);
        }
    }
}

