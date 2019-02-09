package me.nickac.notisyncreborn.networking.server;

import android.service.notification.StatusBarNotification;

import me.nickac.notisyncreborn.NotificationBroadcastReceiver;
import me.nickac.notisyncreborn.SyncApplication;
import me.nickac.notisyncreborn.model.RemoteAction;
import me.nickac.notisyncreborn.model.RemoteNotification;
import me.nickac.notisyncreborn.storage.NotificationStorage;
import spark.Spark;

import static spark.Spark.port;
import static spark.Spark.post;

public class NotificationServer {

    public void start() {
        port(11786);

        post("/dismissnotification/:id/:appPackage", (req, res) -> {
            int i = Integer.parseInt(req.params(":id"));
            String appPackage = req.params(":appPackage");

            RemoteNotification notif = NotificationStorage.getInstance()
                    .getNotificationByIdAndPackage(i, appPackage);

            if (notif != null) {
                StatusBarNotification nativeNotification = notif.getNativeNotification();
                if (nativeNotification != null) {
                    NotificationBroadcastReceiver receiver = NotificationBroadcastReceiver
                            .getInstance();
                    if (receiver != null) {
                        receiver.cancelNotification(nativeNotification.getKey());
                    }
                }
            }

            res.status(200);
            return "";
        });

        post("/invokeAction/:id/:appPackage/:index", (req, res) -> {
            int index = Integer.parseInt(req.params(":index"));
            int i = Integer.parseInt(req.params(":id"));
            String appPackage = req.params(":appPackage");

            RemoteNotification notif = NotificationStorage.getInstance()
                    .getNotificationByIdAndPackage(i, appPackage);

            try {
                RemoteAction[] actions = notif != null ? notif.getActions() : new RemoteAction[0];
                RemoteAction action = actions[index];

                action.invoke(SyncApplication.getContext());

            } catch (IndexOutOfBoundsException ignored) {
                res.status(404);
                return "";
            }

            res.status(200);
            return "";
        });

        post("/invokeReply/:id/:appPackage/:index", (req, res) -> {
            int index = Integer.parseInt(req.params(":index"));
            int i = Integer.parseInt(req.params(":id"));
            String appPackage = req.params(":appPackage");
            String content = req.body();

            RemoteNotification notif = NotificationStorage.getInstance()
                    .getNotificationByIdAndPackage(i, appPackage);

            try {
                RemoteAction[] actions = notif != null ? notif.getActions() : new RemoteAction[0];
                RemoteAction action = actions[index];

                action.sendReply(SyncApplication.getContext(), content);
            } catch (IndexOutOfBoundsException ignored) {
                res.status(404);
                return "";
            }

            res.status(200);
            return "";
        });
    }

    public void stop() {
        Spark.stop();
    }
}
