package me.nickac.notisyncreborn;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import me.nickac.notisyncreborn.model.RemoteNotification;
import me.nickac.notisyncreborn.storage.NotificationStorage;

public class NotificationBroadcastReceiver extends NotificationListenerService {
    private static NotificationBroadcastReceiver instance;

    public static NotificationBroadcastReceiver getInstance() {
        return instance;
    }

    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d(TAG, message.substring(start, end));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        SyncApplication.getApplication().getSyncUtils()
                .removeNotification(sbn.getId(), sbn.getPackageName());
        NotificationStorage.getInstance().removeNotification(sbn.getId(), sbn.getPackageName());
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        RemoteNotification src = RemoteNotification.fromNotification(sbn);
        d("NotifSync", SyncApplication.getGson().toJson(src));

        NotificationStorage.getInstance().storeNotification(src);
        SyncApplication.getApplication().getSyncUtils().sendNotification(src);
        super.onNotificationPosted(sbn);
    }
}
