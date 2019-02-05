package me.nickac.notisyncreborn;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.Person;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.types.TypeInfoPolicy;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.nickac.notifsync.utils.BitmapTypeAdapter;
import me.nickac.notisyncreborn.model.RemoteAction;
import me.nickac.notisyncreborn.model.RemoteNotification;
import me.nickac.notisyncreborn.networking.NotificationSyncUtils;
import me.nickac.notisyncreborn.networking.RestNotificationService;
import me.nickac.notisyncreborn.utils.RetrofitUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.nickac.notisyncreborn.utils.RetrofitUtils.prepareBitmaps;

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
    private RestNotificationService restNotificationService;
    private NotificationSyncUtils syncUtils;

    public static SyncApplication getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
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
        sApplication = null;
    }

    public static YaGson getGson() {
        return gson;
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
    }

    public void setupNotificationChannels() {
        NotificationChannel channel = new NotificationChannel("test-notif", "Test Notifications",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
