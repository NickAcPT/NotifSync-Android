package me.nickac.notisyncreborn.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.nickac.notisyncreborn.SyncApplication;

public class NotificationActionWrapper extends BroadcastReceiver {

    public static final String ACTION_ID = "action-id";

    public static PendingIntent wrapSingle(NotificationAction action) {
        return wrap(action, true);
    }

    public static PendingIntent wrapMulti(NotificationAction action) {
        return wrap(action, false);
    }

    private static PendingIntent wrap(NotificationAction action, boolean oneShot) {

        Context ctx = SyncApplication.getContext();
        Intent intentAction = new Intent(ctx, NotificationActionWrapper.class);
        UUID id = UUID.randomUUID();

        intentAction.putExtra(ACTION_ID, id.toString());
        synchronized (actionWrapperMap) {
            actionWrapperMap.put(id, action);
        }
        return PendingIntent.getBroadcast(ctx, (int) id.getMostSignificantBits(), intentAction, oneShot ? PendingIntent.FLAG_ONE_SHOT : PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra(ACTION_ID);
        if (id != null) {
            //It's our notification.
            synchronized (actionWrapperMap) {
                UUID uuid = UUID.fromString(id);
                NotificationAction action = actionWrapperMap.getOrDefault(uuid, null);
                actionWrapperMap.remove(uuid);
                intent.removeExtra(ACTION_ID);
                if (action != null) {
                    action.doReceive(context, intent);
                }
            }
        }
    }

    public interface NotificationAction {
        void doReceive(Context context, Intent intent);
    }

    private static final Map<UUID, NotificationAction> actionWrapperMap;

    static {
        actionWrapperMap = new ConcurrentHashMap<>();
    }



}
