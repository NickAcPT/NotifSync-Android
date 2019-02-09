package me.nickac.notisyncreborn;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.Person;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.types.TypeInfoPolicy;

import java.lang.reflect.Modifier;

import me.nickac.notisyncreborn.networking.NotificationSyncUtils;
import me.nickac.notisyncreborn.networking.RestNotificationService;
import me.nickac.notisyncreborn.networking.server.NotificationServer;

public class SyncApplication extends Application {
    private static YaGson gson;
    private static SyncApplication sApplication;
    private Person stubPerson = new Person.Builder()
            .setImportant(true)
            .setKey("stub-person")
            .setName("Stub Person")
            .setBot(true)
            .build();
    private MediaSessionCompat mediaSessionCompat;
    private NotificationServer notificationServer;
    private RestNotificationService restNotificationService;
    private NotificationSyncUtils syncUtils;

    public static SyncApplication getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static YaGson getGson() {
        return gson;
    }

    public NotificationServer getNotificationServer() {
        return notificationServer;
    }

    public MediaSessionCompat getMediaSessionCompat() {
        return mediaSessionCompat;
    }

    public Person getStubPerson() {
        return stubPerson;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        notificationServer.stop();
        sApplication = null;
    }

    private YaGsonBuilder getYaGsonBuilder() {
        return new YaGsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setTypeInfoPolicy(TypeInfoPolicy.DISABLED);
    }

    public NotificationSyncUtils getSyncUtils() {
        return syncUtils;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        syncUtils = new NotificationSyncUtils();
        gson = getYaGsonBuilder()
                .create();
        mediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "NOTISYNC");
        sApplication = this;
        notificationServer = new NotificationServer();
        notificationServer.start();
    }

    public void setupNotificationChannels() {
        NotificationChannel channel = new NotificationChannel("test-notif", "Test Notifications",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
