package com.example.workbookapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "WorkbookID";
    private static final String CHANNEL_NAME = "Workbook";
    private static final String CHANNEL_DESCRIPTION = "Lập trình Android";
    private static final int YOUR_REQUEST_CODE = 100; // Đổi thành mã yêu cầu quyền thích hợp

    // Hàm tạo kênh thông báo
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // Hàm hiển thị thông báo
    public static void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        if (notificationManager != null) {
            // Kiểm tra và yêu cầu quyền kênh thông báo nếu cần
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);

                if (channel != null && channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
                            .putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID);
                    context.startActivity(intent);
                    return; // Trả về để không hiển thị thông báo ngay lúc này
                }
            }

            createNotificationChannel(context); // Tạo kênh thông báo nếu chưa tồn tại

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo_active_icon) // Thay đổi icon tại đây
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager.notify(/* ID của thông báo */ 1, builder.build());
        }
    }

    public static void requestOverlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, YOUR_REQUEST_CODE);
        }
    }
}
