package com.example.heroPetShop.ultil;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.Notification.MyApplication;
import com.example.heroPetShop.R;

public class NotificationHelper {

    public static void showNotification(Context context, String title, String message) {
        // Thêm âm thanh tùy chỉnh
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Tạo Intent để mở một Activity khi người dùng nhấn vào thông báo
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Sửa lỗi: Thêm FLAG_IMMUTABLE
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Biểu tượng nhỏ
                .setContentTitle(title)                   // Tiêu đề thông báo
                .setContentText(message)                  // Nội dung thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Độ ưu tiên
                .setSound(soundUri)                       // Âm thanh thông báo
                .setContentIntent(pendingIntent)          // Hành động khi nhấn vào thông báo
                .setAutoCancel(true);                     // Tự động hủy khi người dùng nhấn vào

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }

}
