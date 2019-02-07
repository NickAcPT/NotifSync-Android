package me.nickac.notisyncreborn;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import me.nickac.notisyncreborn.model.RemoteNotification;

public class NotificationBroadcastReceiver extends NotificationListenerService {


    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d(TAG, message.substring(start, end));
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        SyncApplication.getApplication().getSyncUtils().removeNotification(sbn.getId(), sbn.getPackageName());
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        RemoteNotification src = RemoteNotification.fromNotification(sbn);
        d("NotifSync", SyncApplication.getGson().toJson(src));

        SyncApplication.getApplication().getSyncUtils().sendNotification(src);
        super.onNotificationPosted(sbn);
    }
}
