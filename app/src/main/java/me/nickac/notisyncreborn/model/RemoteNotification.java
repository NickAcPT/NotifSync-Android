package me.nickac.notisyncreborn.model;

import android.graphics.Bitmap;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import java.util.Arrays;
import java.util.Map;

import me.nickac.notisyncreborn.SyncApplication;
import me.nickac.notisyncreborn.utils.IconUtils;
import me.nickac.notisyncreborn.utils.MiscUtils;

public class RemoteNotification {

    private final int color;
    private int id;
    private String appPackage;
    private String appName;
    private long postTime;
    private long when;
    private RemoteAction[] actions;
    private Bitmap smallIcon;
    private Bitmap largeIcon;
    private Map<String, Object> extras;
    private RemoteMessageInfo messageInfo;

    private RemoteNotification(StatusBarNotification sbn) {
        id = sbn.getId();
        appPackage = sbn.getPackageName();
        appName = MiscUtils.getNameForPackage(appPackage);
        color = sbn.getNotification().color;
        postTime = sbn.getPostTime();
        when = sbn.getNotification().when;
        if (sbn.getNotification().actions != null)
            actions = Arrays.stream(sbn.getNotification().actions)
                    .map(action -> RemoteAction.fromAction(action, sbn.getPackageName()))
                    .toArray(RemoteAction[]::new);
        smallIcon = IconUtils.getBitmapFromIcon(SyncApplication.getContext(),
                sbn.getNotification().getSmallIcon(), sbn.getPackageName());
        largeIcon = IconUtils.getBitmapFromIcon(SyncApplication.getContext(),
                sbn.getNotification().getLargeIcon(), sbn.getPackageName());
        extras = MiscUtils.bundleToMap(sbn.getNotification().extras);

        NotificationCompat.MessagingStyle messagingStyle = NotificationCompat.MessagingStyle
                .extractMessagingStyleFromNotification(sbn.getNotification());
        if (messagingStyle != null) {
            messageInfo = RemoteMessageInfo.from(messagingStyle);
        }
    }

    public static RemoteNotification fromNotification(StatusBarNotification sbn) {
        return new RemoteNotification(sbn);
    }

    public String getAppName() {
        return appName;
    }

    public int getColor() {
        return color;
    }

    public RemoteMessageInfo getMessageInfo() {
        return messageInfo;
    }

    public int getId() {
        return id;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public long getPostTime() {
        return postTime;
    }

    public long getWhen() {
        return when;
    }

    public RemoteAction[] getActions() {
        return actions;
    }

    public Bitmap getSmallIcon() {
        return smallIcon;
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }
}
