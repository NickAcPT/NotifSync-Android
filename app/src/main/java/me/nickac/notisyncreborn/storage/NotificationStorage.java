package me.nickac.notisyncreborn.storage;

import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import me.nickac.notisyncreborn.model.RemoteNotification;

public class NotificationStorage {
    private static NotificationStorage instance;
    private Map<Pair<Integer, String>, RemoteNotification> notificationMap = new HashMap<>();

    public static NotificationStorage getInstance() {
        if (instance == null)
            instance = new NotificationStorage();
        return instance;
    }

    @Nullable
    public RemoteNotification getNotificationByIdAndPackage(int id, String appPackage) {
        return notificationMap.getOrDefault(new Pair<>(id, appPackage), null);
    }

    public void storeNotification(RemoteNotification notification) {
        notificationMap
                .put(new Pair<>(notification.getId(), notification.getAppPackage()), notification);
    }

    public void removeNotification(RemoteNotification notification) {
        removeNotification(notification.getId(), notification.getAppPackage());
    }

    public void removeNotification(int id, String appPackage) {
        notificationMap.remove(new Pair<>(id, appPackage));
    }
}
