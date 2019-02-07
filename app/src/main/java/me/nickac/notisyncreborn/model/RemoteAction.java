package me.nickac.notisyncreborn.model;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import me.nickac.notisyncreborn.SyncApplication;
import me.nickac.notisyncreborn.utils.IconUtils;

public class RemoteAction {

    private Bitmap icon;
    private String title;
    private transient PendingIntent pendingIntent;
    private RemoteInput[] inputs;

    private RemoteAction(Notification.Action action) {
        this(action, "");
    }

    private RemoteAction(Notification.Action action, String fallBackResPackage) {
        this.pendingIntent = action.actionIntent;
        this.title = action.title.toString();
        this.icon = IconUtils.getBitmapFromIcon(SyncApplication.getContext(), action.getIcon(), fallBackResPackage);
        if (action.getRemoteInputs() != null)
            inputs = Arrays.stream(action.getRemoteInputs()).map(RemoteInput::new).toArray(RemoteInput[]::new);
        else
            inputs = new RemoteInput[]{};
    }

    static RemoteAction fromAction(Notification.Action action) {
        return fromAction(action, "");
    }

    static RemoteAction fromAction(Notification.Action action, String fallbackResPackage) {
        return new RemoteAction(action, fallbackResPackage);
    }

    private static Bundle mapToBundle(Map<String, Object> data) throws Exception {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String)
                bundle.putString(entry.getKey(), (String) entry.getValue());
            else if (entry.getValue() instanceof Double) {
                bundle.putDouble(entry.getKey(), ((Double) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                bundle.putInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                bundle.putFloat(entry.getKey(), ((Float) entry.getValue()));
            }
        }
        return bundle;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public RemoteInput[] getInputs() {
        return inputs;
    }

    public void invoke(Context context) throws PendingIntent.CanceledException {
        if (pendingIntent == null)
            return;
        Intent intent = new Intent();

        pendingIntent.send(context, 0, intent);
    }

    public void sendReply(Context context, String msg) throws PendingIntent.CanceledException {
        if (pendingIntent == null || inputs.length == 0)
            return;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        ArrayList<android.app.RemoteInput> actualInputs = new ArrayList<>();

        for (RemoteInput input : inputs) {
            bundle.putCharSequence(input.getResultKey(), msg);
            android.app.RemoteInput.Builder builder = new android.app.RemoteInput.Builder(input.getResultKey());
            builder.setLabel(input.getLabel());
            builder.setChoices(input.getChoices());
            builder.setAllowFreeFormInput(input.isAllowFreeFormInput());
            try {
                builder.addExtras(mapToBundle(input.getExtras()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            actualInputs.add(builder.build());
        }

        android.app.RemoteInput[] inputs = actualInputs.toArray(new android.app.RemoteInput[0]);
        android.app.RemoteInput.addResultsToIntent(inputs, intent, bundle);
        pendingIntent.send(context, 0, intent);
    }


    @Override
    protected void finalize() throws Throwable {
        if (icon != null)
            icon.recycle();
    }
}
